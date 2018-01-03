package representment.imagegenerator;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import representment.tiffgenerator.Config;

public class ImageGenerationUtilities {
	
	public static void scaleImage(BufferedImage inputImage, BufferedImage outputImage, float scaleFactor) {
		
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
		return Math.min(horizontalScaleFactor, verticalScaleFactor);
	}
	
	public static void createDitheredImage(BufferedImage inputImage, BufferedImage outputImage, boolean scale) {
		
		BufferedImage scaledInputImage = new BufferedImage(outputImage.getWidth(), outputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		ImageGenerationUtilities.scaleImage(inputImage, scaledInputImage, getScaleFactor(inputImage, scaledInputImage));
		
		WritableRaster input = scaledInputImage.copyData(null);
		WritableRaster output  = outputImage.getRaster();

		int inputHeight = scaledInputImage.getHeight();
		int inputWidth = scaledInputImage.getWidth();
		
		final int threshold = 128;
		float value, qerror;

		for (int y = 0; y < inputHeight; ++y) {
			for (int x = 0; x < inputWidth; ++x) {
				value = input.getSample(x, y, 0);

				// Threshold value and compute quantization error
				if (value < threshold) {
					output.setSample(x, y, 0, 0);
					qerror = value;
				} else {
					output.setSample(x, y, 0, 1);
					qerror = value - 255;
				}

				// Spread error amongst neighboring pixels
				// Based on Floyd-Steinberg Dithering
				// http://en.wikipedia.org/wiki/Floyd-Steinberg_dithering
				if (true) {
					if((x > 0) && (y > 0) && (x < (inputWidth-1)) && (y < (inputHeight-1))) {
						// 7/16
						value = input.getSample(x+1, y, 0);
						input.setSample(x+1, y, 0, clamp(value + 0.4375f * qerror));
						// 3/16
						value = input.getSample(x-1, y+1, 0);
						input.setSample(x-1, y+1, 0, clamp(value + 0.1875f * qerror));
						// 5/16
						value = input.getSample(x, y+1, 0);
						input.setSample(x, y+1, 0, clamp(value + 0.3125f * qerror));
						// 1/16
						value = input.getSample(x+1, y+1, 0);
						input.setSample(x+1, y+1, 0, clamp(value + 0.0625f * qerror));
					}
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
