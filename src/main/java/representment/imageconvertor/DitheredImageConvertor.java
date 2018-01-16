package representment.imageconvertor;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import representment.imagegenerator.OtsuThresholding;
import representment.main.SimpleDummyLogger;
import representment.tiffgenerator.Config;

public class DitheredImageConvertor implements Convertor {
	
	private static SimpleDummyLogger LOG = new SimpleDummyLogger();
	
	BufferedImage inputImage;
	BufferedImage outputImage;
	
	boolean scaleBeforeDither;
	
	public DitheredImageConvertor(BufferedImage inputImage, BufferedImage outputImage, boolean isScaleEnabled) {
		this.inputImage = inputImage;
		this.outputImage = outputImage;
		this.scaleBeforeDither = isScaleEnabled;
		
	}
	
	@Override
	public BufferedImage convert() {
		BufferedImage scaledInputImage = new BufferedImage(outputImage.getWidth(), outputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		//ImageGenerationUtilities.scaleImage(inputImage, scaledInputImage, getScaleFactor(inputImage, scaledInputImage));
		Convertor scaleConvertor = new ImageScaleConvertor(inputImage, scaledInputImage);
		scaleConvertor.convert();
		LOG.debug("Image scaled, applying dither to image");
		
		WritableRaster input = scaledInputImage.copyData(null);
		WritableRaster output  = outputImage.getRaster();

		int inputHeight = scaledInputImage.getHeight();
		int inputWidth = scaledInputImage.getWidth();
		
		OtsuThresholding otsu = new OtsuThresholding();
		final int threshold;
		if(Config.USE_OTSU_THRESHOLDING) {
			threshold =  otsu.getThreshold(scaledInputImage);  
		}
		else {
			threshold = Config.BINARY_CONVERSION_THRESHOLD;
		}
		
		Convertor convertToGray = new GrayscaleConvertor(inputImage);
		convertToGray.convert();
		
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
		
		return this.outputImage;
	}
	
	private static int clamp(float value) { 
		return Math.min(Math.max(Math.round(value), 0), 255); 
	}
}
