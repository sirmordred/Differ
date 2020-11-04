package app.mordred.diffgenerator.impl.java;

import static app.mordred.diffgenerator.util.Constants.EXIT_CODE_ERROR;
import static app.mordred.diffgenerator.util.DiffToHtmlParameters.DiffSide.LEFT;
import static app.mordred.diffgenerator.util.DiffToHtmlParameters.DiffSide.RIGHT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import app.mordred.diffgenerator.html.DirectoryDiffHtmlBuilder;
import app.mordred.diffgenerator.html.FileDiffHtmlBuilder;
import app.mordred.diffgenerator.impl.DiffToHtmlResult;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;
import app.mordred.diffgenerator.util.DiffToHtmlParameters.DiffType;
import app.mordred.diffgenerator.util.FileHelper;

public class JavaDirDiffToHtmlImpl extends JavaFileDiffToHtmlImpl {

	private static final String UNIQUE_FILE_PREFIX = "Only in ";

	private static final String UNIQUE_LINE_SPLIT_STR = ": ";
	public static final String tooManyDifferencesErrorMsg = "Empty directory or too many unique files. Abort!";


	public JavaDirDiffToHtmlImpl(DiffToHtmlParameters params) {
		super(params);
	}

	@Override
	public DiffToHtmlResult runDiffToHtml() throws IOException {
		ArrayList<File> leftSortedFilesAndDirs = getSortedFilesAndDirs(params.getInputLeftPath());
		ArrayList<File> rightSortedFilesAndDirs = getSortedFilesAndDirs(params.getInputRightPath());

		if (dirsToDiffNotEmpty(leftSortedFilesAndDirs, rightSortedFilesAndDirs) && fileNumberToDiffNotTooDifferent(leftSortedFilesAndDirs, rightSortedFilesAndDirs, params.getMaxAllowedFileInDir())) {
			DirectoryDiffHtmlBuilder dirDiffHtmlBuilder = new DirectoryDiffHtmlBuilder(params);
			traverseLeftDirectory(dirDiffHtmlBuilder, leftSortedFilesAndDirs);
			traverseRightDirectory(dirDiffHtmlBuilder, rightSortedFilesAndDirs);
			return new DiffToHtmlResult(dirDiffHtmlBuilder.toString(), resultCode);
		} else {
			FileDiffHtmlBuilder fileDiffHtmlBuilder = new FileDiffHtmlBuilder(params);
			System.out.println(tooManyDifferencesErrorMsg);
			fileDiffHtmlBuilder.appendAttentionLine(tooManyDifferencesErrorMsg);
			return new DiffToHtmlResult(fileDiffHtmlBuilder.toString(), EXIT_CODE_ERROR);
		}
	}

	private boolean dirsToDiffNotEmpty(ArrayList<File> leftSortedFilesAndDirs, ArrayList<File> rightSortedFilesAndDirs) {
		return leftSortedFilesAndDirs.size() > 2 && rightSortedFilesAndDirs.size() > 2;
	}

	private boolean fileNumberToDiffNotTooDifferent(ArrayList<File> leftSortedFilesAndDirs, ArrayList<File> rightSortedFilesAndDirs, int maxAllowedFileCountInDir) {
		int leftSize = leftSortedFilesAndDirs.size();
		int rightSize = rightSortedFilesAndDirs.size();
		if (leftSize > maxAllowedFileCountInDir || rightSize > maxAllowedFileCountInDir) {
			int minSize = Math.min(leftSize, rightSize);
			int maxSize = Math.max(leftSize, rightSize);
			return minSize > maxSize / 2;
		}
		return true;
	}

	private ArrayList<File> getSortedFilesAndDirs(String dirPath) {
		ArrayList<File> files = new ArrayList<>(FileUtils.listFilesAndDirs(new File(dirPath), TrueFileFilter.TRUE, TrueFileFilter.TRUE));
		Collections.sort(files);
		return files;
	}

	private void traverseLeftDirectory(DirectoryDiffHtmlBuilder dirDiffHtmlBuilder, ArrayList<File> filesAndDirs) throws IOException {
		for (File fileLeft : filesAndDirs) {
			String fileLeftPath = FilenameUtils.separatorsToUnix(fileLeft.getPath());
			String fileRightPath = fileLeftPath.replace(params.getInputLeftPath(), params.getInputRightPath());
			DiffToHtmlParameters fileDiffParams = createFileDiffParams(fileLeftPath, fileRightPath);

			if (Files.exists(Paths.get(fileRightPath))) {
				if (Files.isRegularFile(Paths.get(fileLeftPath)) && !isInputFilesAreIdentical(fileDiffParams)) {
					makeDifferingFilesEntry(dirDiffHtmlBuilder, fileDiffParams);
				}
			} else {
				makeUniqueFileEntry(dirDiffHtmlBuilder, fileDiffParams, LEFT);
			}
		}
	}

	private void traverseRightDirectory(DirectoryDiffHtmlBuilder dirDiffHtmlBuilder, ArrayList<File> filesAndDirs) throws IOException {
		for (File fileRight : filesAndDirs) {
			String fileRightPath = FilenameUtils.separatorsToUnix(fileRight.getPath());
			String fileLeftPath = fileRightPath.replace(params.getInputRightPath(), params.getInputLeftPath());
			DiffToHtmlParameters fileDiffParams = createFileDiffParams(fileLeftPath, fileRightPath);
			if (!Files.exists(Paths.get(fileLeftPath))) {
				makeUniqueFileEntry(dirDiffHtmlBuilder, fileDiffParams, RIGHT);
			}
		}
	}

	private DiffToHtmlParameters createFileDiffParams(String fileLeftPath, String fileRightPath) {
		DiffToHtmlParameters diffToHtmlParameters = DiffToHtmlParameters.builder(params)
				.withInputLeftPath(fileLeftPath)
				.withInputRightPath(fileRightPath)
				.build();
		diffToHtmlParameters.setDiffType(DiffType.FILES);
		return diffToHtmlParameters;
	}

	private void makeDifferingFilesEntry(DirectoryDiffHtmlBuilder dirDiffHtmlBuilder, DiffToHtmlParameters diffParams) throws IOException {
		FileDiffHtmlBuilder htmlTableBuilder = appendFileDiffToBuilder(new FileDiffHtmlBuilder(params), diffParams);
		String fileLeftPath = diffParams.getInputLeftPath();

		if (FileHelper.isFileBinary(fileLeftPath)) {
			dirDiffHtmlBuilder.appendChangedBinaryFile(fileLeftPath, htmlTableBuilder.createDiffTable());
		} else {
			dirDiffHtmlBuilder.appendChangedTextFile(fileLeftPath, htmlTableBuilder.createDiffTable());
		}
		resultCode = EXIT_CODE_ERROR;
	}

	private void makeUniqueFileEntry(DirectoryDiffHtmlBuilder htmlBuilder, DiffToHtmlParameters diffParams, DiffToHtmlParameters.DiffSide diffSide)
			throws IOException {
		if(params.isIgnoreUniqueFiles()) {
			return;
		}
		FileDiffHtmlBuilder htmlTableBuilder = new FileDiffHtmlBuilder(diffParams);
		String filePath;
		if(diffSide == LEFT) {
			filePath = diffParams.getInputLeftPath();
			setLeftFileInfo(htmlTableBuilder, diffParams);
		} else {
			filePath = diffParams.getInputRightPath();
			setRightFileInfo(htmlTableBuilder, diffParams);
		}


		if (Files.isDirectory(Paths.get(filePath)) || FileHelper.isFileBinary(filePath)) {
			htmlTableBuilder.appendInfoLine(createUniqueFileMessage(filePath));
		} else {
			htmlTableBuilder.appendTextFile(new String(Files.readAllBytes(Paths.get(filePath))));
		}

		if(diffSide == LEFT) {
			htmlBuilder.appendUniqueFileLeft(filePath, htmlTableBuilder.createDiffTable());
		} else {
			htmlBuilder.appendUniqueFileRight(filePath, htmlTableBuilder.createDiffTable());
		}
		resultCode = EXIT_CODE_ERROR;
	}

	private String createUniqueFileMessage(String fileLeftPath) {
		return UNIQUE_FILE_PREFIX + FilenameUtils.getPathNoEndSeparator(fileLeftPath) + UNIQUE_LINE_SPLIT_STR
				+ FilenameUtils.getName(fileLeftPath);
	}
}
