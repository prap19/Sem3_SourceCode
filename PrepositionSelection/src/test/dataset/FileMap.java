package test.dataset;

public class FileMap {

	private String fileName;
	private String sentence;
	
	public FileMap()
	{
		
	}
	public FileMap(String fileName, String sentence)
	{
		this.fileName = fileName;
		this.sentence = sentence;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
}
