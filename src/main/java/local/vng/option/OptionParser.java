package local.vng.option;

import local.vng.dictionary.Message;
import local.vng.dictionary.Type;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static local.vng.setting.Settings.*;

public class OptionParser {

    public Type getType(CommandLine cmd) {
        if ((!cmd.hasOption("i") && !cmd.hasOption("s")) || (cmd.hasOption("i") && cmd.hasOption("s"))) {
            System.out.println(Message.MSG_CLI_KEY_INT_STR.getDescription());
            return null;
        }
        Type type = null;
        if (cmd.hasOption("i")) {
            type = Type.TYPE_INT;
        }
        if (cmd.hasOption("s")) {
            type = Type.TYPE_STRING;
        }
        return type;
    }

    public File getOutput(CommandLine cmd) {
        File output = new File(cmd.getArgs()[0]);
        if (!(new File(output.getPath()).isDirectory()) && !(output.getName().matches(".+\\..+$"))) {
            System.out.println(Message.MSG_CLI_OUTPUTFILE.getDescription());
            return null;
        }
        return output;
    }

    public List<File> getInputs(CommandLine cmd) {
        int len = cmd.getArgs().length;
        String[] inputArgs = new String[len - 1];
        for (int i = 0; i + 1 < len; i++) {
            inputArgs[i] = cmd.getArgs()[i + 1];
        }
        List<File> files = new ArrayList<>();
        if (new File(inputArgs[0]).isDirectory()) {
            File dirFile = new File(inputArgs[0]);
            if (dirFile.listFiles() == null) {
                System.out.println(Message.MSG_MENU_DIR_FALSE.getDescription());
                return files;
            } else {
                files = Stream.of(dirFile.listFiles())
                        .filter(file -> !file.isDirectory())
                        .collect(Collectors.toList());
            }
            if (files.isEmpty()) {
                System.out.println(Message.MSG_MENU_DIR_EMPTY.getDescription());
                return files;
            }
        } else {
            for (String elem : inputArgs) {
                File file = new File(elem);
                if (file.exists()) {
                    files.add(file);
                } else {
                    System.out.println(Message.MSG_CLI_INPUTFILE.getDescription());
                    return files;
                }
            }
        }
        return files;
    }

    public boolean getAscOrder(CommandLine cmd) {
        return !cmd.hasOption("d");
    }

    public long getMaxFileSize(CommandLine cmd) {
        long fileSize = DEFAULT_MAX_FILE_SIZE;
        if (cmd.hasOption("f")) {
            try {
                fileSize = Long.parseLong(cmd.getOptionValue("f"));
            } catch (NumberFormatException e) {
                System.out.println(Message.MSG_CLI_KEY_FILESIZE.getDescription());
                System.out.println(e.getMessage());
            }
            if (fileSize < MIN_MAX_FILE_SIZE) {
                fileSize = DEFAULT_MAX_FILE_SIZE;
            }
            return fileSize;
        }
        return fileSize;
    }

    public long getBufferSize(CommandLine cmd) {
        long bufferSize = DEFAULT_BUFFER_SIZE;
        if (cmd.hasOption("b")) {
            try {
                bufferSize = Long.parseLong(cmd.getOptionValue("b"));
            } catch (NumberFormatException e) {
                System.out.println(Message.MSG_CLI_KEY_BUFFERSIZE.getDescription());
                System.out.println(e.getMessage());
            }
            if (bufferSize < MIN_BUFFER_SIZE) {
                bufferSize = DEFAULT_BUFFER_SIZE;
            }
            return bufferSize;
        }
        return bufferSize;
    }

    public Options createOptions() {
        Options options = new Options();
        Option opInt = Option
                .builder("i")
                .longOpt("integer")
                .required(false)
                .desc("Set integer sequence")
                .build();
        options.addOption(opInt);
        Option opStr = Option
                .builder("s")
                .longOpt("string")
                .required(false)
                .desc("Set string sequence")
                .build();
        options.addOption(opStr);
        Option opDesc = Option
                .builder("d")
                .longOpt("desc")
                .required(false)
                .desc("Set descending sort order")
                .build();
        options.addOption(opDesc);
        Option opAsc = Option
                .builder("a")
                .longOpt("asc")
                .required(false)
                .desc("Set ascending sort order")
                .build();
        options.addOption(opAsc);
        Option opSize = Option
                .builder("f")
                .longOpt("file")
                .hasArg()
                .required(false)
                .desc("Set the max file size in case size of input file is big")
                .build();
        options.addOption(opSize);
        Option opBuf = Option
                .builder("b")
                .longOpt("buffer")
                .hasArg()
                .required(false)
                .desc("Set the buffer size in case size of input file is big")
                .build();
        options.addOption(opBuf);

        return options;
    }

}