package app.mordred.diffgenerator.impl.java;

import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_ERROR;
import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_OK;
import static org.apache.commons.lang3.StringUtils.CR;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import app.mordred.diffgenerator.html.FileDiffHtmlBuilder;
import app.mordred.diffgenerator.impl.DiffToHtmlResult;
import app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;
import app.mordred.diffgenerator.util.FileHelper;
import app.mordred.diffgenerator.util.SimpleFileInfo;

public class JavaFileDiffToHtmlImpl {
	
	private static final String BINARY_FILES_DIFFER_PREFIX = "Binary files ";
	
	private static final String BINARY_FILES_SUFFIX = " differ";

	private static final String BINARY_LINE_SPLIT_STR = " and ";

	private static final String IDENTICAL_FILES_PREFIX = "Files ";

	private static final String IDENTICAL_FILES_SUFFIX = " are identical";

	private static final String IDENTICAL_LINE_SPLIT_STR = " and ";

	protected DiffToHtmlParameters params;

	int resultCode = EXIT_CODE_OK;

	public JavaFileDiffToHtmlImpl(DiffToHtmlParameters params) {
		this.params = params;
	}

	public DiffToHtmlResult runDiffToHtml() throws IOException {
		String html = appendFileDiffToBuilder(new FileDiffHtmlBuilder(params), params).toString();
		return new DiffToHtmlResult(html, resultCode);
	}

	FileDiffHtmlBuilder appendFileDiffToBuilder(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params)
			throws IOException {
		setFileInfos(htmlBuilder, params);

		if (isInputFilesAreIdentical(params)) {
			return appendIdenticalFilesToBuilder(htmlBuilder, params);

		} else if (FileHelper.isFileBinary(params.getInputLeftPath())) {
			return appendBinaryFilesDiffToBuilder(htmlBuilder, params);

		} else if (FileHelper.isFileSizeDifferenceTooBig(params.getInputLeftPath(),params.getInputRightPath(),params.getMaxAllowedDifferenceInByte())) {
			return appendFileSizeTooBigToBuilder(htmlBuilder);
		}
		else {
			return appendTextFilesDiffToBuilder(htmlBuilder, params);
		}
	}

	private FileDiffHtmlBuilder appendFileSizeTooBigToBuilder(FileDiffHtmlBuilder htmlBuilder) {
		htmlBuilder.appendAttentionLine("Files differ but filesize difference too big to parse.");
		resultCode = EXIT_CODE_ERROR;
		return  htmlBuilder;
	}

	private FileDiffHtmlBuilder appendIdenticalFilesToBuilder(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params) {
		htmlBuilder.appendInfoLine(createFilesIdenticalMessage(params));
		resultCode = EXIT_CODE_OK;
		return htmlBuilder;
	}

	private FileDiffHtmlBuilder appendBinaryFilesDiffToBuilder(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params) {
		htmlBuilder.appendAttentionLine(createBinaryFilesDifferMessage(params));
		resultCode = EXIT_CODE_ERROR;
		return htmlBuilder;
	}

	private FileDiffHtmlBuilder appendTextFilesDiffToBuilder(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params)
			throws IOException {
		htmlBuilder = new JavaDiffUtils2HtmlWrapper().appendDiffToBuilder(htmlBuilder, params);
		resultCode = EXIT_CODE_ERROR; //TODO change this return codes
		return htmlBuilder;
	}

	private void setFileInfos(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params) throws IOException {
		setLeftFileInfo(htmlBuilder, params);
		setRightFileInfo(htmlBuilder, params);
	}

	protected void setLeftFileInfo(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params) throws IOException {
		String fileLeftPath = params.getInputLeftPath();
		if (Files.exists(Paths.get(fileLeftPath))) {
			String fileLeftLastModified = Files.getLastModifiedTime(Paths.get(fileLeftPath)).toString();
			htmlBuilder.setFileLeftInfo(new SimpleFileInfo(fileLeftPath, fileLeftLastModified));
		}
	}

	void setRightFileInfo(FileDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters params) throws IOException {
		String fileRightPath = params.getInputRightPath();
		if (Files.exists(Paths.get(fileRightPath))) {
			String fileRightLastModified = Files.getLastModifiedTime(Paths.get(fileRightPath)).toString();
			htmlBuilder.setFileRightInfo(new SimpleFileInfo(fileRightPath, fileRightLastModified));
		}
	}

	boolean isInputFilesAreIdentical(DiffToHtmlParameters params) throws IOException {
		if(!FileHelper.isFileBinary(params.getInputLeftPath())) {
			String text1 = new String(Files.readAllBytes(Paths.get(params.getInputLeftPath())));
			String text2 = new String(Files.readAllBytes(Paths.get(params.getInputRightPath())));
			if(params.isIgnoreWhiteSpaces() || params.isIgnoreSpaceChange()) {
				String replacement = params.isIgnoreWhiteSpaces() ? "" : " ";
				text1 = text1.replaceAll("\\s+", replacement);
				text2 = text2.replaceAll("\\s+", replacement);
			}
			if(params.isIgnoreLineEndings()) {
				text1 = text1.replace(CR, "");
				text2 = text2.replace(CR, "");
			}
			return text1.equals(text2);
		} else {
			return FileUtils.contentEquals(new File(params.getInputLeftPath()), new File(params.getInputRightPath()));
		}
	}

	private String createFilesIdenticalMessage(DiffToHtmlParameters params) {
		return IDENTICAL_FILES_PREFIX + params.getInputLeftPath() + IDENTICAL_LINE_SPLIT_STR + params.getInputRightPath() + IDENTICAL_FILES_SUFFIX;
	}

	private String createBinaryFilesDifferMessage(DiffToHtmlParameters params) {
		return BINARY_FILES_DIFFER_PREFIX + params.getInputLeftPath() + BINARY_LINE_SPLIT_STR + params.getInputRightPath() + BINARY_FILES_SUFFIX;
	}
}
