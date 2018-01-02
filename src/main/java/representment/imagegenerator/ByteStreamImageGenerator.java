package representment.imagegenerator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import representment.tiffgenerator.Config;

public class ByteStreamImageGenerator implements ImageGenerator {

	private InputStream inputDocument;
	
	private float ROTATION = 0.0f;
	private float SCALE = 0.0F;
	
	public ByteStreamImageGenerator(InputStream file) {
		this.inputDocument = file;
	}
	
	public List<BufferedImage> generate() {
		
		final List<BufferedImage> imageFromJByteStream = new ArrayList<BufferedImage>();
		
		try {
			final byte[] imageInBytes = IOUtils.toByteArray(inputDocument);
			if(imageInBytes != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageInBytes));
				imageFromJByteStream.add(image);
				return imageFromJByteStream;
			}
			return null;
			
		} catch (IOException e) {
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
