package local.vng.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static local.vng.setting.Settings.DEFAULT_BUFFER_SIZE;
import static local.vng.setting.Settings.DEFAULT_MAX_FILE_SIZE;

@Getter
@AllArgsConstructor
public enum Message {
    MSG_RW_IO_ERROR("Input/output error."),
    MSG_RW_FNF_ERROR("File not found."),
    MSG_CLI_KEY_INT_STR("There have to be one obligatory key -i or -s."),
    MSG_CLI_KEY_FILESIZE("There isn't correct value of file size in key -f. Set default value = " + DEFAULT_MAX_FILE_SIZE),
    MSG_CLI_KEY_BUFFERSIZE("There isn't correct value of buffer size in key -b. Set default value = " + DEFAULT_BUFFER_SIZE),
    MSG_CLI_OUTPUTFILE("Incorrect output filename."),
    MSG_CLI_INPUTFILE("Incorrect input file/s."),
    MSG_MENU_DIR_FALSE("Directory isn't exist."),
    MSG_MENU_DIR_EMPTY("Directory is empty."),
    MSG_RESULT_INTERMEDIATE_MERGE("Files will be merged: "),
    MSG_RESULT_INTERMEDIATE_SPLIT("File will be split (big file): "),
    MSG_RESULT("Result in file: "),
    MSG_RESULT_CORRUPTED("Data with corrupted string (contains \" \") in file corruptedData.txt");
    private final String description;
}
