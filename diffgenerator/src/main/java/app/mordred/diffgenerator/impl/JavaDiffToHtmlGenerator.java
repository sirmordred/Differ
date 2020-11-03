package app.mordred.diffgenerator.impl;

import java.io.IOException;

import app.mordred.diffgenerator.impl.java.JavaDirDiffToHtmlImpl;
import app.mordred.diffgenerator.impl.java.JavaFileDiffToHtmlImpl;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;
import app.mordred.diffgenerator.util.DiffToHtmlParameters.DiffType;

public class JavaDiffToHtmlGenerator {

	public DiffToHtmlResult generateHtml(DiffToHtmlParameters params) throws IOException {
		if (params.getDiffType() == DiffType.DIRECTORIES) {
			return new JavaDirDiffToHtmlImpl(params).runDiffToHtml();
		} else {
			return new JavaFileDiffToHtmlImpl(params).runDiffToHtml();
		}
	}
}
