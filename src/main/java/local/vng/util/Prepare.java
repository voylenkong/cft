package local.vng.util;

import java.io.File;

import static local.vng.setting.Settings.CORRUPTED_FILENAME;

public class Prepare {

    public static void prepare(File output) {
        if (output.exists()) {
            output.delete();
        }
        File corrupted = new File(output.getAbsoluteFile().getParent() + "\\" + CORRUPTED_FILENAME);
        if (corrupted.exists()) {
            corrupted.delete();
        }
    }
}
