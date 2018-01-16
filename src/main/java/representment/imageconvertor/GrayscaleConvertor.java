package representment.imageconvertor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GrayscaleConvertor implements Convertor {

	
	BufferedImage inputImage;
	BufferedImage outputImage;
	
	public GrayscaleConvertor(BufferedImage inputImage) {
		super();
		this.inputImage = inputImage;
		this.outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	}

	@Override
	public BufferedImage convert() {
		Graphics g = this.outputImage.getGraphics();
		g.drawImage(inputImage, 0, 0, null);
		g.dispose();
		return this.outputImage;
	}

}
