package representment.imagegenerator;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import representment.main.SimpleDummyLogger;
import representment.tiffgenerator.Config;

public class ByteStreamImageGenerator implements ImageGenerator {

	private static SimpleDummyLogger LOG = new SimpleDummyLogger();
	
	private InputStream inputDocument;
	
	private float ROTATION = 0.0f;
	private float SCALE = 0.0F;
	private int pageHeight;
	private int pageWidth;
	private int imageType;
	
	public ByteStreamImageGenerator(InputStream file) {
		this.inputDocument = file;
		this.pageHeight = Config.OUTPUT_DEFAULT_HEIGHT;
		this.pageWidth = Config.OUTPUT_DEFAULT_WIDTH;
		this.imageType = getBufferedImageType();
	}
	
	public List<BufferedImage> generate() {
		
		final List<BufferedImage> imageFromJByteStream = new ArrayList<BufferedImage>();
		
		try {
			final byte[] imageInBytes = IOUtils.toByteArray(inputDocument);
			if(imageInBytes != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageInBytes));
				BufferedImage outputImage = new BufferedImage(pageWidth, pageHeight, this.imageType);
				
				if(this.imageType == BufferedImage.TYPE_BYTE_BINARY) {
					ImageGenerationUtilities.createDitheredImage(image, outputImage, true);
				}

				imageFromJByteStream.add(outputImage);
				return imageFromJByteStream;
			}
			return null;
			
		} catch (IOException e) {
			LOG.info("Error in reading input document "+e.getMessage());
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
