package app.mordred.diffgenerator;

import app.mordred.diffgenerator.util.DiffToHtmlParameters;
import app.mordred.diffgenerator.util.FileHelper;
import app.mordred.diffgenerator.util.cli.CliParser;
import app.mordred.diffgenerator.util.cli.DiffToHtmlCommandLine;

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

    public static final int EXIT_CODE_ERROR = 1;

    public static final int EXIT_CODE_OK = 0;

    private static final int UNIFIED_CONTEXT_LINES = 3;

    public static final String PROGRAM_NAME = "cronn-diff-to-html";

    private static String workingDir = FileHelper.getWorkingDir();

    private static final Long MAX_ALLOWED_FILESIZE_DIFFERENCE_IN_BYTES = 500000l;

    private Main() {}

    public static void main(String[] args) throws Exception {
        DiffToHtmlCommandLine cli;
        cli = new CliParser(workingDir).parse(args);
        if(cli.isHelpOnly()) {
            System.exit(EXIT_CODE_OK);
        }

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
                .build();
        int status = new CronnDiffToHtml().generateDiffToHtmlReport(parameters);
        System.exit(status);

    }

    public static String getWorkingDir() {
        return workingDir;
    }

    public static void setWorkingDir(String workingDir) {
        Main.workingDir = workingDir;
    }

    private static int tooManyFilesAmount = 1000;

    public static int getTooManyFilesAmount() {
        return tooManyFilesAmount;
    }
    static void setTooManyFilesAmount(int amount) {
        tooManyFilesAmount = amount;
    }
}
