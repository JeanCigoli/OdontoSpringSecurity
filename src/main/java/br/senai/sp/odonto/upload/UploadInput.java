package br.senai.sp.odonto.upload;

public class UploadInput {

	private String filename;
	private String mimeType;
	private String base64;

	public UploadInput() {
	}

	public UploadInput(String filename, String mineType, String base64) {
		this.filename = filename;
		this.mimeType = mineType;
		this.base64 = base64;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mineType) {
		this.mimeType = mineType;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
