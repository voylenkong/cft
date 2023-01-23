package local.vng;

import local.vng.dictionary.Message;
import local.vng.dictionary.Type;
import local.vng.option.OptionParser;
import local.vng.service.Service;
import local.vng.util.Prepare;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

import static java.util.Objects.nonNull;

public class App {
    public static void main(String[] args) {

        OptionParser optionParser = new OptionParser();
        Options options = optionParser.createOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            Type type = optionParser.getType(cmd);
            File output = optionParser.getOutput(cmd);
            List<File> inputs = optionParser.getInputs(cmd);
            boolean ascOrder = optionParser.getAscOrder(cmd);
            long maxFileSize = optionParser.getMaxFileSize(cmd);
            long bufferSize = optionParser.getBufferSize(cmd);

            if (nonNull(type) && nonNull(output) && !inputs.isEmpty()) {
                Prepare.prepare(output);
                Service service = new Service();
                File result;

                if (ascOrder) {
                    result = service.mergeFileList(inputs, type, maxFileSize, bufferSize);
                } else {
                    result = service.mergeFileListDesc(inputs, type, maxFileSize, bufferSize);
                }
                result.renameTo(output);
                System.out.println(Message.MSG_RESULT.getDescription() + output.getAbsolutePath());
                System.out.println(Message.MSG_RESULT_CORRUPTED.getDescription());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

}
