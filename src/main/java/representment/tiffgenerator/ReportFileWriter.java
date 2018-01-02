package representment.tiffgenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ReportFileWriter {
	
	private String filename;
	
	private String filepath;
	
	private int totalPages;
	
	private ImageOutputStream outputStream;
	
	private ImageWriter writer;
	
	
	public static ReportFileWriter createMultipageTiff(String filename, String filepath, String reportFormatName) {
		ReportFileWriter reportWriter = new ReportFileWriter(filename, filepath);
		reportWriter.createOutputStream();
		reportWriter.createImageWriter(reportFormatName);
		return reportWriter;
	}

	private ReportFileWriter(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
	}

	private void createImageWriter(String formatName) {
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
		if(writers.hasNext()) {
			this.writer = writers.next();
			if(this.outputStream == null) {
				this.createOutputStream();
			}
			this.writer.setOutput(this.outputStream);
		}
		else {
			throw new RuntimeException("No writers available");
		}
	}
	
	private void createOutputStream() {
		try {
			File file = new File(this.filename);
			this.outputStream = ImageIO.createImageOutputStream(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeImageToNewPage(BufferedImage image) {
		try {
			writer.writeToSequence(new IIOImage(image, null, null), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize() throws IOException {
		writer.prepareWriteSequence(null);
	}
	
	public void close() throws IOException {
		writer.endWriteSequence();
	}
}
