package app.mordred.diffgenerator.util;

public class Constants {

    public static final String TAG = "DiffGenerator";

    public static final int EXIT_CODE_ERROR = 1;

    public static final int EXIT_CODE_OK = 0;

    public static final String NEWLINE = System.lineSeparator();

    public static final String SYSOUT_MSG_DIRECTORIES_IDENTICAL = NEWLINE + "Directories are identical!";

    public static final String SYSOUT_MSG_FILES_IDENTICAL = NEWLINE + "Files are identical!";

    public static final String SYSOUT_MSG_DIRECTORIES_DIFFER = NEWLINE + "Directories differ!";

    public static final String SYSOUT_MSG_FILES_DIFFER = NEWLINE + "Files differ!";

    public static final int BYTE_BUFFER_SIZE_DETECT_BINARY = 16000;

}
