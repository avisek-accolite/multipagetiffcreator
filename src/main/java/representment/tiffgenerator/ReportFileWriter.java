package representment.tiffgenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import representment.main.SimpleDummyLogger;

public class ReportFileWriter {
	
	private static SimpleDummyLogger LOG = new SimpleDummyLogger();
	
	private String filename;
	private String filepath;
	private int totalPages;
	private ImageOutputStream outputStream;
	private ImageWriter writer;
	
	
	public static ReportFileWriter createMultipageTiff(String filename, String filepath, String reportFormatName) throws IOException {
		ReportFileWriter reportWriter = new ReportFileWriter(filename, filepath);
		reportWriter.createOutputStream();
		reportWriter.createImageWriter(reportFormatName);
		return reportWriter;
	}

	private ReportFileWriter(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
	}

	private void createImageWriter(String formatName) throws IOException {
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
		if(writers.hasNext()) {
			this.writer = writers.next();
			if(this.outputStream == null) {
				this.createOutputStream();
			}
			this.writer.setOutput(this.outputStream);
		}
		else {
			LOG.info("No writers available. Report cannot be created.");
			throw new RuntimeException("No writers available");
		}
	}
	
	private void createOutputStream() throws IOException{
		try {
			File file = new File(this.filename);
			this.outputStream = ImageIO.createImageOutputStream(file);
			LOG.debug("Output file created successfully");
		} catch (IOException e) {
			LOG.info("Output file creation failed with error:\n"+e.getStackTrace());
			throw e;
		}
	}
	
	public void writeImageToNewPage(BufferedImage image) {
		try {
			LOG.debug("Writing image to report");
			writer.writeToSequence(new IIOImage(image, null, null), null);
		} catch (IOException e) {
			LOG.info("Failed to write to report with error: "+e.getStackTrace());
		}
	}
	
	public void initialize() throws IOException {
		writer.prepareWriteSequence(null);
	}
	
	public void close() throws IOException {
		writer.endWriteSequence();
	}
}
