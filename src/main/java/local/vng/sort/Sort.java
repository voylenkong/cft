package local.vng.sort;

import local.vng.dictionary.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static local.vng.setting.Settings.CORRUPTED_FILENAME;

public class Sort {

    public<T> File sort(File inputFile, Comparator<T> comparator, Function<String, T> convertFun) {
        File fileOutputResult = new File(inputFile.getAbsoluteFile().getParent() + "/" + UUID.randomUUID() + ".txt");
        File fileOutputCorrupted = new File(inputFile.getAbsoluteFile().getParent() + "/" + CORRUPTED_FILENAME);
        try (
                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bwResult = new BufferedWriter(new FileWriter(fileOutputResult));
                BufferedWriter bwCorrupted = new BufferedWriter(new FileWriter(fileOutputCorrupted, true));
        ) {
            String line = br.readLine();
            List<T> elemList = new ArrayList<>();
            while (line != null) {
                if (line.contains(" ")) {
                    bwCorrupted.write(line);
                    bwCorrupted.newLine();
                    line = br.readLine();
                    continue;
                }
                elemList.add(convertFun.apply(line));
                line = br.readLine();
            }
            for (T elem : this.sortElements(elemList, comparator)) {
                bwResult.write(elem.toString());
                bwResult.newLine();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(Message.MSG_RW_FNF_ERROR.getDescription());
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println(Message.MSG_RW_IO_ERROR.getDescription());
            System.out.println(e.getMessage());
        }
        return fileOutputResult;
    }

    public<T> List<T> sortElements(List<T> inputList, Comparator<T> comparator) {
        if (inputList == null) {
            return new ArrayList<>();
        }
        if (inputList.size() < 2) {
            return inputList;
        }
        List<T> list1 = inputList.subList(0, inputList.size() / 2);
        List<T> list2 = inputList.subList((inputList.size() / 2), inputList.size());
        list1 = sortElements(list1, comparator);
        list2 = sortElements(list2, comparator);
        return mergeElementLists(list1, list2, comparator);
    }

    public <T> List<T> mergeElementLists(List<T> list1, List<T> list2, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int i = 0;
        int j = 0;
        while ((i < list1.size()) & (j < list2.size())) {
            if (comparator.compare(list1.get(i), list2.get(j)) < 0) {
                result.add(list1.get(i));
                i++;
            } else {
                result.add(list2.get(j));
                j++;
            }
        }
        result.addAll(list1.subList(i, list1.size()));
        result.addAll(list2.subList(j, list2.size()));
        return result;
    }

}
