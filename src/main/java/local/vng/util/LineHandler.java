package local.vng.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;

public final class LineHandler {

    private LineHandler() {
    }

    public static boolean skipCorrupted(String line, BufferedWriter bwCorrupted) throws IOException {
        if ((line.length() == 0) || (line.contains(" "))) {
            bwCorrupted.write(line);
            bwCorrupted.newLine();
            return true;
        }
        return false;
    }

    public static <T> boolean skipUnsorted(String line, T elem, T lastElem, Comparator<T> comparator, BufferedWriter bwUnsorted) throws IOException {
        if ((lastElem != null) && (comparator.compare(elem, lastElem) < 0)) {
            bwUnsorted.write(line);
            bwUnsorted.newLine();
            return true;
        }
        return false;
    }

    public static  <T> T writeMinElem(String line, T elem, BufferedWriter bwResult) throws IOException {
        bwResult.write(line);
        bwResult.newLine();
        return elem;
    }
}
