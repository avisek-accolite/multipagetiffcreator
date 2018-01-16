package representment.imageconvertor;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import representment.main.SimpleDummyLogger;

public class ImageScaleConvertor implements Convertor {

	private static SimpleDummyLogger LOG = new SimpleDummyLogger();
	
	BufferedImage inputImage;
	BufferedImage outputImage;
	
	float scaleFactor;
	
	public ImageScaleConvertor(BufferedImage inputImage, BufferedImage outputImage) {
		this.inputImage = inputImage;
		this.outputImage = outputImage;
		this.scaleFactor = ImageScaleConvertor.getScaleFactor(inputImage, outputImage);
	}
	
	public ImageScaleConvertor(BufferedImage inputImage, float scaleFactor)  {
		this.inputImage = inputImage;
		this.outputImage = new BufferedImage((int)(inputImage.getWidth()*scaleFactor), (int)(inputImage.getHeight()*scaleFactor), inputImage.getType());
		this.scaleFactor = scaleFactor;
	}
	
	@Override
	public BufferedImage convert() {
		LOG.debug("Scaling image by scaling factor: "+scaleFactor);
		
		boolean centerVertical = false;
		boolean centerHorizontal = false;
		if(inputImage.getWidth()*scaleFactor - outputImage.getWidth() < 2 ) {
			centerVertical = true;
		}
		else if(inputImage.getHeight()*scaleFactor - outputImage.getHeight() < 2) {
			centerHorizontal = true;
		}
		
		int outputStartX = 0;
		int outputEndX = outputImage.getWidth();
		int outputStartY = 0;
		int outputEndY = outputImage.getHeight();
		if(centerHorizontal) {
			outputStartX = (int)(outputImage.getWidth()/2 - (inputImage.getWidth()*scaleFactor)/2);
			outputEndX = (int)(inputImage.getWidth()*scaleFactor + outputStartX);
		}
		if(centerVertical) {
			outputStartY = (int)(outputImage.getHeight()/2 - (inputImage.getHeight()*scaleFactor)/2);
			outputEndY = (int)(inputImage.getHeight()*scaleFactor + outputStartY);
		}
		
		Graphics2D outputGraphics = outputImage.createGraphics();
		outputGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		outputGraphics.drawImage(inputImage, outputStartX, outputStartY, outputEndX, outputEndY, 0, 0, inputImage.getWidth(), inputImage.getHeight(), null);
		outputGraphics.dispose();
		
		return this.outputImage;
	}
	
	public static float getScaleFactor(BufferedImage inputImage, BufferedImage outputImage) {
		float horizontalScaleFactor = (float)outputImage.getWidth() / (float)inputImage.getWidth();
		float verticalScaleFactor = (float)outputImage.getHeight() / (float)inputImage.getHeight();
		float scaleFactor = Math.min(horizontalScaleFactor, verticalScaleFactor);
		LOG.debug("Image scaling required with scale factor = "+scaleFactor);
		return scaleFactor;
	}
}
