package app.mordred.diffgenerator;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import app.mordred.diffgenerator.impl.DiffToHtmlResult;
import app.mordred.diffgenerator.impl.java.JavaDirDiffToHtmlImpl;
import app.mordred.diffgenerator.impl.java.JavaFileDiffToHtmlImpl;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;

import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_ERROR;
import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_OK;
import static app.mordred.diffgenerator.util.Constants.TAG;

public class DiffGenerator {

    private static final String NEWLINE = System.lineSeparator();

    private static final String SYSOUT_MSG_DIRECTORIES_IDENTICAL = NEWLINE + "Directories are identical!";

    private static final String SYSOUT_MSG_FILES_IDENTICAL = NEWLINE + "Files are identical!";

    private static final String SYSOUT_MSG_DIRECTORIES_DIFFER = NEWLINE + "Directories differ!";

    private static final String SYSOUT_MSG_FILES_DIFFER = NEWLINE + "Files differ!";

    private static final String SYSOUT_MSG_OUTPUT_WRITTEN_TO = NEWLINE + "Output written to: file://";

    private DiffGenerator() {}

    private static DiffToHtmlParameters checkAndFixUserInput(DiffToHtmlParameters userParams) {
        File inputLeftFile = new File(userParams.getInputLeftPath());
        File inputRightFile = new File(userParams.getInputRightPath());
        if (!inputLeftFile.exists() || !inputRightFile.exists()) {
            // TODO add log here
            return null;
        }
        if (inputLeftFile.isFile() && inputRightFile.isFile()) {
            userParams.setDiffType(DiffToHtmlParameters.DiffType.FILES);
        } else if (inputLeftFile.isDirectory() && inputRightFile.isDirectory()) {
            userParams.setDiffType(DiffToHtmlParameters.DiffType.DIRECTORIES);
        } else {
            //TODO inform user
            return null;
        }

        return userParams;
    }


    public static int generateDiffToHtml(DiffToHtmlParameters params) throws IOException {
        DiffToHtmlParameters fixedUserParams = checkAndFixUserInput(params);
        if (fixedUserParams == null) {
            return EXIT_CODE_ERROR;
        }
        DiffToHtmlResult res;
        if (fixedUserParams.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            res = new JavaDirDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        } else {
            res = new JavaFileDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        }
        String path = fixedUserParams.getOutputPath();
        // TODO write it into application dir if its not specified
        Files.write(Paths.get(path), res.getHtml().getBytes());
        int status = res.getResultCode();
        if (fixedUserParams.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            System.out.println();
            Log.v(TAG, status == EXIT_CODE_OK ?
                    SYSOUT_MSG_DIRECTORIES_IDENTICAL : SYSOUT_MSG_DIRECTORIES_DIFFER);
        } else {
            Log.v(TAG, status == EXIT_CODE_OK ?
                    SYSOUT_MSG_FILES_IDENTICAL : SYSOUT_MSG_FILES_DIFFER);
        }
        return fixedUserParams.isOnlyReports() ? EXIT_CODE_OK : status;
    }
}
