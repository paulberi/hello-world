package se.metria.skogsanalys.service.rapport;

import java.util.HashMap;
import java.util.Map;

public class PdfServiceParams {
	private String url;
	private Map<String, String> authOptions = new HashMap<>();
	private Map<String, String> pdfOptions = new HashMap<>();

	public PdfServiceParams(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setAuthOption(String option, String value) {
		authOptions.put(option, value);
	}

	public void setPdfOption(String option, String value) {
		pdfOptions.put(option, value);
	}

	public Map<String, String> getAuthOptions() {
		return authOptions;
	}

	public Map<String, String> getPdfOptions() {
		return pdfOptions;
	}
}
