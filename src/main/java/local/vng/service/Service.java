package local.vng.service;

import local.vng.dictionary.Message;
import local.vng.dictionary.Type;
import local.vng.sort.Sort;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static local.vng.setting.Settings.UNSORTED_FILENAME;

public class Service {

    Sort sort = new Sort();
    FileSplitter fileSplitter = new FileSplitter();
    FileJoiner fileJoiner = new FileJoiner();

    public File mergeFileList(List<File> inputFiles, Type type, long maxFileSize, long bufferSize) {
        List<File> result = new ArrayList<>();
        String dir = inputFiles.get(0).getAbsoluteFile().getParent() + "/";

        if (inputFiles.size() > 1) {
            do {
                result = combineFilesByTwo(inputFiles, type);
                inputFiles = result;
            } while (result.size() != 1);

            return handleUnsorted(dir, type, result.get(0), maxFileSize, bufferSize);

        } else {
            try {
                if (Files.size(inputFiles.get(0).toPath()) > maxFileSize) {
                    return mergeFileList(fileSplitter.splitBigFile(inputFiles.get(0), bufferSize, type.getComparator(), type.getConvertFun()), type, maxFileSize, bufferSize);
                } else {
                    return sort.sort(inputFiles.get(0), type.getComparator(), type.getConvertFun());
                }
            } catch (FileNotFoundException e) {
                System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
                System.out.println(e.getMessage());
            }
        }
        return result.get(0);
    }

    public File mergeFileListDesc(List<File> inputFiles, Type type, long maxFileSize, long bufferSize) {
        File ascFile = this.mergeFileList(inputFiles, type, maxFileSize, bufferSize);
        File descFile = new File(ascFile.getAbsoluteFile().getParent() + "\\" + UUID.randomUUID() + ".txt");

        try (
                ReversedLinesFileReader reversedReader = new ReversedLinesFileReader(ascFile);
                BufferedWriter bwResult = new BufferedWriter(new FileWriter(descFile));
        ) {
            String line = reversedReader.readLine();
            while (line != null) {
                bwResult.write(line);
                bwResult.newLine();
                line = reversedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
            System.out.println(e.getMessage());
        }
        ascFile.delete();
        return descFile;
    }

    private List<File> combineFilesByTwo(List<File> inputFiles, Type type) {
        List<File> mergedFiles = new ArrayList<>();
        List<File> result = new ArrayList<>();

        for (int i = 0, j = 1; j < inputFiles.size(); i += 2, j += 2) {
            result.add(fileJoiner.mergeFiles(inputFiles.get(i), inputFiles.get(j), type.getComparator(), type.getConvertFun()));
            mergedFiles.add(inputFiles.get(i));
            mergedFiles.add(inputFiles.get(j));
        }

        if ((inputFiles.size() % 2) != 0) {
            result.add(inputFiles.get(inputFiles.size() - 1));
        }

        for (File elem : mergedFiles) {
            if (elem.getName().startsWith("tmp") || elem.getName().startsWith("split")) {
                elem.delete();
            }
        }
        return result;
    }

    private File handleUnsorted(String dir, Type type, File sortedResult, long maxFileSize, long bufferSize) {
        File unsorted = new File(dir + UNSORTED_FILENAME);
        File sorted = null;

        try {
            if (Files.size(unsorted.toPath()) > maxFileSize) {
                sorted = mergeFileList(fileSplitter.splitBigFile(unsorted, bufferSize, type.getComparator(), type.getConvertFun()), type, maxFileSize, bufferSize);
            } else {
                sorted = sort.sort(unsorted, type.getComparator(), type.getConvertFun());
            }
        } catch (FileNotFoundException e) {
            System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
            System.out.println(e.getMessage());
        }

        File resultFile = fileJoiner.mergeFiles(sortedResult, sorted, type.getComparator(), type.getConvertFun());
        unsorted.delete();
        sortedResult.delete();
        sorted.delete();
        return resultFile;
    }

}
