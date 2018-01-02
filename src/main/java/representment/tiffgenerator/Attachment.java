package representment.tiffgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/*
 * This holds all attachments for a particular representment
 * Methods: To download attachment, to get metadata (filename, etc of the attachment)
 */
public class Attachment {
	
	String filename;
	
	public Attachment(String filename) {
		this.filename = filename;
	}
	
	public InputStream getAttachmentStream() throws FileNotFoundException {
		return new FileInputStream(new File(this.filename));
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getExtension() {
		return this.filename.substring(this.filename.indexOf('.')+1);
	}
}
