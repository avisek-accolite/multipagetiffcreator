package representment.imagegenerator;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import representment.main.SimpleDummyLogger;
import representment.tiffgenerator.Config;

public class ImageGenerationUtilities {
	
	private static SimpleDummyLogger LOG = new SimpleDummyLogger();
	
	public static void grayscaleImage(BufferedImage inputImage, BufferedImage outputImage) {
		
	}
	
	public static void scaleImage(BufferedImage inputImage, BufferedImage outputImage, float scaleFactor) {
		
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
	}
	
	public static float getScaleFactor(BufferedImage inputImage, BufferedImage outputImage) {
		float horizontalScaleFactor = (float)outputImage.getWidth() / (float)inputImage.getWidth();
		float verticalScaleFactor = (float)outputImage.getHeight() / (float)inputImage.getHeight();
		float scaleFactor = Math.min(horizontalScaleFactor, verticalScaleFactor);
		LOG.debug("Image scaling required with scale factor = "+scaleFactor);
		return scaleFactor;
	}
	
	public static void createDitheredImage(BufferedImage inputImage, BufferedImage outputImage, boolean scale) {
		
		BufferedImage scaledInputImage = new BufferedImage(outputImage.getWidth(), outputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		ImageGenerationUtilities.scaleImage(inputImage, scaledInputImage, getScaleFactor(inputImage, scaledInputImage));
		LOG.debug("Image scaled, applying dither to image");
		
		WritableRaster input = scaledInputImage.copyData(null);
		WritableRaster output  = outputImage.getRaster();

		int inputHeight = scaledInputImage.getHeight();
		int inputWidth = scaledInputImage.getWidth();
		
		final int threshold = Config.BINARY_CONVERSION_THRESHOLD;
		float value, qerror;
		LOG.info("Adding dither to image with threshold at "+threshold);

		for (int y = 0; y < inputHeight; ++y) {
			for (int x = 0; x < inputWidth; ++x) {
				value = input.getSample(x, y, 0);
				if (value < threshold) {
					output.setSample(x, y, 0, 0);
					qerror = value;
				} else {
					output.setSample(x, y, 0, 1);
					qerror = value - 255;
				}

				if((x > 0) && (y > 0) && (x < (inputWidth-1)) && (y < (inputHeight-1))) {
					value = input.getSample(x+1, y, 0);
					input.setSample(x+1, y, 0, clamp(value + 0.4375f * qerror));
					value = input.getSample(x-1, y+1, 0);
					input.setSample(x-1, y+1, 0, clamp(value + 0.1875f * qerror));
					value = input.getSample(x, y+1, 0);
					input.setSample(x, y+1, 0, clamp(value + 0.3125f * qerror));
					value = input.getSample(x+1, y+1, 0);
					input.setSample(x+1, y+1, 0, clamp(value + 0.0625f * qerror));
				}
			}
		}
	}
	
	private static int clamp(float value) { 
		return Math.min(Math.max(Math.round(value), 0), 255); 
	}
	
	
	public static int getDefaultBufferedImageType() {
		int bitDepth = Config.OUTPUT_BIT_DEPTH;
		switch(bitDepth) {
			case 1:
				LOG.info("Output image set to black and white");
				return BufferedImage.TYPE_BYTE_BINARY;
			case 8:
				LOG.info("Output image set to grayscale");
				return BufferedImage.TYPE_BYTE_GRAY;
			case 24:
				LOG.info("Output image set to coloured");
				return BufferedImage.TYPE_INT_RGB;
			default:
				LOG.info("Output image set to coloured");
				return BufferedImage.TYPE_INT_RGB;
		}
	}
}
