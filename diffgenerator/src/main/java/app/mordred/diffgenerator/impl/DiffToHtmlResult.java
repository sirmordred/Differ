package app.mordred.diffgenerator.impl;

import java.util.List;

public final class DiffToHtmlResult {

	private final String html;
	private final int resultCode;
	private List<String> rawDiff;

	public DiffToHtmlResult(String html, int resultCode, List<String> rawDiff) {
		this.html = html;
		this.resultCode = resultCode;
		this.rawDiff = rawDiff;
	}

	public String getHtml() {
		return html;
	}

	public int getResultCode() {
		return resultCode;
	}

	public List<String> getRawDiff() {
		return rawDiff;
	}

}
