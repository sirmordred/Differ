package app.mordred.diffgenerator.util;

public class Constants {

    public static final int EXIT_CODE_ERROR = 1;

    public static final int EXIT_CODE_OK = 0;

    public static final int UNIFIED_CONTEXT_LINES = 3;

    public static String workingDir = FileHelper.getWorkingDir();

    public static final Long MAX_ALLOWED_FILESIZE_DIFFERENCE_IN_BYTES = 500000l;
}
