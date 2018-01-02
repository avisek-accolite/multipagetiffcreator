package representment.tiffgenerator;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.List;

import representment.imagegenerator.ImageGenerator;
import representment.imagegenerator.MetaTextImageGenerator;
import representment.imagegenerator.ByteStreamImageGenerator;
import representment.imagegenerator.PdfFileImageGenerator;

public class TiffGenerator {
	
	public void generateRepresentmentAsTiff(Representment representment) throws Exception {
		List<Attachment> attachments = representment.getAttachments();
		
		ReportFileWriter outputFile= ReportFileWriter.createMultipageTiff("SampleTiff.tiff", "", "tiff");
		outputFile.initialize();

		//Write meta to first page
		ImageGenerator metaTextGenerator = new MetaTextImageGenerator(representment);
		outputFile.writeImageToNewPage(metaTextGenerator.generate().get(0));
		
		//Write images to other pages
		for(Attachment attachment: attachments) {
			ImageGenerator attachmentImageGenerator = this.getImageGenerator(attachment);
			if(attachmentImageGenerator == null) {
				throw new Exception();
			}
			List<BufferedImage> attachmentImages = attachmentImageGenerator.generate();
			
			for(BufferedImage attachmentImage : attachmentImages) {				
				outputFile.writeImageToNewPage(attachmentImage);
			}
		}
		
		outputFile.close();
	}
	
	public ImageGenerator getImageGenerator(Attachment attachment) {
		String extension = attachment.getExtension();
		try {
			if(extension.equalsIgnoreCase("pdf")) {
				return new PdfFileImageGenerator(attachment.getAttachmentStream());
			} 
			else if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
				return new ByteStreamImageGenerator(attachment.getAttachmentStream());
			}
			else if(extension.equalsIgnoreCase("png")) {
				return new ByteStreamImageGenerator(attachment.getAttachmentStream());
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
