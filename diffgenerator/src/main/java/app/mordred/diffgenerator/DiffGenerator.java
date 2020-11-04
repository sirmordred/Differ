package app.mordred.diffgenerator;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import app.mordred.diffgenerator.impl.DiffToHtmlResult;
import app.mordred.diffgenerator.impl.java.JavaDirDiffToHtmlImpl;
import app.mordred.diffgenerator.impl.java.JavaFileDiffToHtmlImpl;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;

import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_ERROR;
import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_OK;
import static app.mordred.diffgenerator.util.Constants.SYSOUT_MSG_DIRECTORIES_DIFFER;
import static app.mordred.diffgenerator.util.Constants.SYSOUT_MSG_DIRECTORIES_IDENTICAL;
import static app.mordred.diffgenerator.util.Constants.SYSOUT_MSG_FILES_DIFFER;
import static app.mordred.diffgenerator.util.Constants.SYSOUT_MSG_FILES_IDENTICAL;
import static app.mordred.diffgenerator.util.Constants.TAG;

public class DiffGenerator {

    private DiffGenerator() {}

    private static DiffToHtmlParameters checkAndFixUserInput(DiffToHtmlParameters userParams,
                                                             boolean checkOutput) {
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

        if (checkOutput) {
            if (!userParams.getOutputPath().endsWith(".html")) {
                userParams.setOutputPath(userParams.getOutputPath() + ".html");
            }
        }

        return userParams;
    }


    public static int generateAndSaveDiff(DiffToHtmlParameters params) throws IOException {
        DiffToHtmlParameters fixedUserParams = checkAndFixUserInput(params, true);
        if (fixedUserParams == null) {
            return EXIT_CODE_ERROR;
        }
        DiffToHtmlResult res;
        if (fixedUserParams.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            res = new JavaDirDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        } else {
            res = new JavaFileDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        }
        File outputFile = new File(fixedUserParams.getOutputPath());
        FileWriter outputFileWriter = new FileWriter(outputFile);
        outputFileWriter.write(res.getHtml());
        outputFileWriter.close();

        int status = res.getResultCode();
        if (fixedUserParams.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            Log.v(TAG, status == EXIT_CODE_OK ?
                    SYSOUT_MSG_DIRECTORIES_IDENTICAL : SYSOUT_MSG_DIRECTORIES_DIFFER);
        } else {
            Log.v(TAG, status == EXIT_CODE_OK ?
                    SYSOUT_MSG_FILES_IDENTICAL : SYSOUT_MSG_FILES_DIFFER);
        }
        return fixedUserParams.isOnlyReports() ? EXIT_CODE_OK : status;
    }

    public static String generateDiff(DiffToHtmlParameters params) throws IOException {
        DiffToHtmlParameters fixedUserParams = checkAndFixUserInput(params, false);
        if (fixedUserParams == null) {
            return null;
        }
        DiffToHtmlResult res;
        if (fixedUserParams.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            res = new JavaDirDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        } else {
            res = new JavaFileDiffToHtmlImpl(fixedUserParams).runDiffToHtml();
        }
        //TODO also return status code somehow

        return res.getHtml();
    }
}
