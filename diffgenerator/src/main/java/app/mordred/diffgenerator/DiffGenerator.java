package app.mordred.diffgenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import app.mordred.diffgenerator.impl.DiffToHtmlResult;
import app.mordred.diffgenerator.impl.JavaDiffToHtmlGenerator;
import app.mordred.diffgenerator.util.Constants;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;

import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_OK;
import static app.mordred.diffgenerator.util.Constants.MAX_ALLOWED_FILESIZE_DIFFERENCE_IN_BYTES;
import static app.mordred.diffgenerator.util.Constants.UNIFIED_CONTEXT_LINES;
import static app.mordred.diffgenerator.util.Constants.workingDir;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_DETECT_ENCODING;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_IGNORE_LINE_ENDINGS;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_IGNORE_SPACE_CHANGE;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_IGNORE_UNIQUE_FILES;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_IGNORE_WHITESPACES;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_LINEWISE_DIFF;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_MAX_ALLOWED_FILESIZE_DIFFERENCE;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_ONLY_REPORTS;
import static app.mordred.diffgenerator.util.cli.CliParser.OPT_UNIFIED_CONTEXT;

public class DiffGenerator {

    private static final String NEWLINE = System.lineSeparator();

    private static final String SYSOUT_MSG_DIRECTORIES_IDENTICAL = NEWLINE + "Directories are identical!";

    private static final String SYSOUT_MSG_FILES_IDENTICAL = NEWLINE + "Files are identical!";

    private static final String SYSOUT_MSG_DIRECTORIES_DIFFER = NEWLINE + "Directories differ!";

    private static final String SYSOUT_MSG_FILES_DIFFER = NEWLINE + "Files differ!";

    private static final String SYSOUT_MSG_OUTPUT_WRITTEN_TO = NEWLINE + "Output written to: file://";

    private DiffGenerator() {}

    public static void main(String[] args) throws Exception {

        DiffToHtmlParameters parameters = DiffToHtmlParameters.builder()
                .withDiffType(cli.isInputsFiles() ? DiffToHtmlParameters.DiffType.FILES : DiffToHtmlParameters.DiffType.DIRECTORIES)
                .withInputLeftPath(cli.getInputLeft())
                .withInputRightPath(cli.getInputRight())
                .withOutputPath(cli.getOutput())
                .withIgnoreUniqueFiles(cli.hasOption(OPT_IGNORE_UNIQUE_FILES))
                .withIgnoreWhiteSpaces(cli.hasOption(OPT_IGNORE_WHITESPACES))
                .withIgnoreSpaceChange(cli.hasOption(OPT_IGNORE_SPACE_CHANGE))
                .withIgnoreLineEndings(cli.hasOption(OPT_IGNORE_LINE_ENDINGS))
                .withDetectTextFileEncoding(cli.hasOption(OPT_DETECT_ENCODING))
                .withOnlyReports(cli.hasOption(OPT_ONLY_REPORTS))
                .withUnifiedContext(Integer
                        .parseInt(cli.getOptionValue(OPT_UNIFIED_CONTEXT, Integer.toString(UNIFIED_CONTEXT_LINES))))
                .withMaxAllowedDifferenceInByte(Long.parseLong(cli.getOptionValue(OPT_MAX_ALLOWED_FILESIZE_DIFFERENCE,
                        Long.toString(MAX_ALLOWED_FILESIZE_DIFFERENCE_IN_BYTES))))
                .withLinewiseDiff(cli.hasOption(OPT_LINEWISE_DIFF))
                .withMaxAllowedFileInDir(/*TODO add value here*/)
                .build();
        int status = generateDiffToHtml(parameters);
        System.exit(status);

    }

    public static String getWorkingDir() {
        return workingDir;
    }

    public static void setWorkingDir(String workingDir) {
        Constants.workingDir = workingDir;
    }


    public static int generateDiffToHtml(DiffToHtmlParameters params) throws IOException {
        DiffToHtmlResult res = new JavaDiffToHtmlGenerator().generateHtml(params);
        String path = params.getOutputPath();
        // TODO write it into application dir if its not specified
        Files.write(Paths.get(path), res.getHtml().getBytes());
        int status = res.getResultCode();
        if (params.getDiffType() == DiffToHtmlParameters.DiffType.DIRECTORIES) {
            System.out.println(status == EXIT_CODE_OK ?
                    SYSOUT_MSG_DIRECTORIES_IDENTICAL : SYSOUT_MSG_DIRECTORIES_DIFFER);
        } else {
            System.out.println(status == EXIT_CODE_OK ?
                    SYSOUT_MSG_FILES_IDENTICAL : SYSOUT_MSG_FILES_DIFFER);
        }
        return params.isOnlyReports() ? EXIT_CODE_OK : status;
    }
}
