package representment.imagegenerator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import representment.tiffgenerator.Config;

public class PdfFileImageGenerator implements ImageGenerator {

	private InputStream inputDocument;
	
	private final float ROTATION;
	private final float SCALE;
	
	public PdfFileImageGenerator(InputStream file) {
		this.inputDocument = file;
		this.ROTATION = Config.INPUT_DEFAULT_IMAGE_ROTATION_ANGLE;
		this.SCALE = Config.INPUT_DEFAULT_IMAGE_SCALE_RATIO;
	}
	
	public List<BufferedImage> generate() {
		
		final List<BufferedImage> imageFromPdf;
		
		try {
			byte[] inputDocBytes = IOUtils.toByteArray(inputDocument);
			
			String filename = "SamplePdf.pdf";
			
			Document pdfDocument = new Document();
			pdfDocument.setByteArray(inputDocBytes, 0, inputDocBytes.length, filename);
			
			imageFromPdf = new ArrayList<BufferedImage>();
			
			
			for(int pageNo=0; pageNo<pdfDocument.getNumberOfPages(); pageNo++) {
				
				PDimension pageDimension = pdfDocument.getPageDimension(pageNo, ROTATION);
				
				BufferedImage imagePage = new BufferedImage((int)pageDimension.getWidth(), (int)pageDimension.getHeight(), getBufferedImageType());
						
				Graphics2D imageGraphics = imagePage.createGraphics();
				
				pdfDocument.paintPage(pageNo, imageGraphics, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, ROTATION, SCALE);
				
				imageGraphics.dispose();
				
				imageFromPdf.add(imagePage);
			}
			
			pdfDocument.dispose();
			
			return imageFromPdf;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public int getBufferedImageType() {
		int bitDepth = Config.OUTPUT_BIT_DEPTH;
		switch(bitDepth) {
			case 1:
				return BufferedImage.TYPE_BYTE_BINARY;
			case 8:
				return BufferedImage.TYPE_BYTE_GRAY;
			case 24:
				return BufferedImage.TYPE_INT_RGB;
			default:
				return BufferedImage.TYPE_INT_RGB;
		}
	}
	
}
