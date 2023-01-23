package local.vng.service;

import local.vng.dictionary.Message;
import local.vng.sort.Sort;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static local.vng.dictionary.Message.MSG_RESULT_INTERMEDIATE_SPLIT;
import static local.vng.setting.Settings.CORRUPTED_FILENAME;
import static local.vng.util.LineHandler.skipCorrupted;

public class FileSplitter {

    Sort sort = new Sort();

    public <T> List<File> splitBigFile(File inputFile, long maxBufferSize, Comparator<T> comparator, Function<String, T> convertFun) {
        System.out.println(MSG_RESULT_INTERMEDIATE_SPLIT.getDescription() + inputFile.getName());
        List<File> result = new ArrayList<>();
        String dir = inputFile.getAbsoluteFile().getParent();
        File fileOutputCorrupted = new File(dir + "/" + CORRUPTED_FILENAME);
        try (
                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bwCorrupted = new BufferedWriter(new FileWriter(fileOutputCorrupted, true));
        ) {
            String line = br.readLine();

            List<T> buffer = new ArrayList<>();
            while (line != null) {

                if (skipCorrupted(line, bwCorrupted)) {
                    line = br.readLine();
                    continue;
                }

                if (buffer.size() >= maxBufferSize) {
                    result.add(this.writeToSplitFile(dir, buffer, comparator));
                    buffer.clear();
                }
                buffer.add(convertFun.apply(line));
                line = br.readLine();
            }
            result.add(this.writeToSplitFile(dir, buffer, comparator));

        } catch (FileNotFoundException e) {
            System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
            System.out.println(e.getMessage());
        }

        if (inputFile.getName().startsWith("unsorted")) {
            inputFile.delete();
        }
        return result;
    }

    public <T> File writeToSplitFile(String dir, List<T> inputList, Comparator<T> comparator) {
        File fileOutputResult = new File(dir + "/split" + UUID.randomUUID() + ".txt");
        try (
                FileWriter fwResult = new FileWriter(fileOutputResult);
                BufferedWriter bwResult = new BufferedWriter(fwResult);
        ) {
            for (T elem : sort.sortElements(inputList, comparator)) {
                bwResult.write(elem.toString());
                bwResult.newLine();
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

}
