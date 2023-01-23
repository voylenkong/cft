package local.vng.service;

import local.vng.dictionary.Message;

import java.io.*;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.Function;

import static local.vng.dictionary.Message.MSG_RESULT_INTERMEDIATE_MERGE;
import static local.vng.setting.Settings.CORRUPTED_FILENAME;
import static local.vng.setting.Settings.UNSORTED_FILENAME;
import static local.vng.util.LineHandler.*;

public class FileJoiner {

    public <T> File mergeFiles(File inputFile1, File inputFile2, Comparator<T> comparator, Function<String, T> convertFun) {
        System.out.println(MSG_RESULT_INTERMEDIATE_MERGE.getDescription() + inputFile1.getName() + " " + inputFile2.getName());
        File fileOutputUnsorted = new File(inputFile1.getAbsoluteFile().getParent() + "/" + UNSORTED_FILENAME);
        File fileOutputCorrupted = new File(inputFile1.getAbsoluteFile().getParent() + "/" + CORRUPTED_FILENAME);
        File fileOutputResult = new File(inputFile1.getAbsoluteFile().getParent() + "/tmp" + UUID.randomUUID() + ".txt");
        try (
                BufferedReader br1 = new BufferedReader(new FileReader(inputFile1));
                BufferedReader br2 = new BufferedReader(new FileReader(inputFile2));

                BufferedWriter bwResult = new BufferedWriter(new FileWriter(fileOutputResult));
                BufferedWriter bwUnsorted = new BufferedWriter(new FileWriter(fileOutputUnsorted, true));
                BufferedWriter bwCorrupted = new BufferedWriter(new FileWriter(fileOutputCorrupted, true));
        ) {
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            if ((line1 == null) & (line2 == null)) {
                return fileOutputResult;
            }

            T elem1;
            T elem2;
            T lastElem = null;

            while ((line1 != null) & (line2 != null)) {
                if (skipCorrupted(line1, bwCorrupted)) {
                    line1 = br1.readLine();
                    continue;
                }
                if (skipCorrupted(line2, bwCorrupted)) {
                    line2 = br2.readLine();
                    continue;
                }

                elem1 = convertFun.apply(line1);
                elem2 = convertFun.apply(line2);
                if (skipUnsorted(line1, elem1, lastElem, comparator, bwUnsorted)) {
                    line1 = br1.readLine();
                    continue;
                }
                if (skipUnsorted(line2, elem2, lastElem, comparator, bwUnsorted)) {
                    line2 = br2.readLine();
                    continue;
                }

                if (comparator.compare(elem1, elem2) < 0) {
                    lastElem = writeMinElem(line1, elem1, bwResult);
                    line1 = br1.readLine();
                }
                if (comparator.compare(elem1, elem2) > 0) {
                    lastElem = writeMinElem(line2, elem2, bwResult);
                    line2 = br2.readLine();
                }
                if (comparator.compare(elem1, elem2) == 0) {
                    writeMinElem(line1, elem1, bwResult);
                    lastElem = writeMinElem(line2, elem2, bwResult);
                    line1 = br1.readLine();
                    line2 = br2.readLine();
                }
            }

            if ((line1 != null) & (line2 == null)) {
                appendTailToResult(line1, lastElem, comparator, convertFun, br1, bwResult, bwUnsorted, bwCorrupted);
            }
            if ((line1 == null) & (line2 != null)) {
                appendTailToResult(line2, lastElem, comparator, convertFun, br2, bwResult, bwUnsorted, bwCorrupted);
            }
        } catch (FileNotFoundException e) {
            System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
            System.out.println(e.getMessage());
        }
        return fileOutputResult;
    }

    private <T> void appendTailToResult(
            String line,
            T lastElem,
            Comparator<T> comparator,
            Function<String, T> convertFun,
            BufferedReader br,
            BufferedWriter bwResult,
            BufferedWriter bwUnsorted,
            BufferedWriter bwCorrupted
    ) throws IOException {
        while (line != null) {
            if (skipCorrupted(line, bwCorrupted)) {
                line = br.readLine();
                continue;
            }
            T elem = convertFun.apply(line);
            if (skipUnsorted(line, elem, lastElem, comparator, bwUnsorted)) {
                line = br.readLine();
                continue;
            }
            bwResult.write(line);
            bwResult.newLine();
            lastElem = elem;
            line = br.readLine();
        }
    }

}
