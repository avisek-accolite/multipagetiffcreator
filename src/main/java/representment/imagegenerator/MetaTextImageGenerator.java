package representment.imagegenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import representment.tiffgenerator.Config;
import representment.tiffgenerator.Representment;

public class MetaTextImageGenerator implements ImageGenerator{
	
	Representment representment;
	
	Color backgroundColor;
	Color foregroundColor; 
	int pageHeight;
	int pageWidth;
	Font font;
	int lineHeight;
	
	public MetaTextImageGenerator(Representment representment) {
		this.representment = representment;
		
		this.backgroundColor = Color.decode(Config.META_BACKGROUND_COLOR);
		this.foregroundColor = Color.decode(Config.META_FOREGROUND_COLOR);
		this.pageHeight = Config.META_PAGE_HEIGHT;
		this.pageWidth = Config.META_PAGE_WIDTH;
		this.font = new Font(Config.META_FONT, Font.PLAIN, Config.META_FONT_SIZE);
		this.lineHeight = Config.META_FONT_SIZE*2;		
	}
	
	public List<BufferedImage> generate() {
		List<BufferedImage> image = new ArrayList<BufferedImage>();
		image.add(getReportMetaImage());
		return image;
	}
	
	public BufferedImage getReportMetaImage() {
	    String[] reportMetaInformation = getReportMetatext();
	    
	    BufferedImage bufferedImage = new BufferedImage(pageWidth, pageHeight, getBufferedImageType());
	    
	    Graphics graphics = bufferedImage.getGraphics();
	    graphics.setColor(backgroundColor);
	    graphics.fillRect(0, 0, pageWidth, pageHeight);
	    graphics.setColor(foregroundColor);
	    graphics.setFont(font);
	    
	    for(int line = 0; line<reportMetaInformation.length; line++) {
	    	graphics.drawString(reportMetaInformation[line], 10, 25 + (line*lineHeight));	    	
	    }
	    
	    return bufferedImage;
	}
	
	public String[] getReportMetatext() {
		
		String[] reportText = new String[4];
		
		reportText[0] = Config.META_LABEL_BANKREFID + ": " + representment.getBankRefID();
		reportText[1] = Config.META_LABEL_AMOUNT + ": " + representment.getAmount();
		reportText[2] = Config.META_LABEL_CURRENCY + ": " + representment.getCurrency();
		reportText[3] = Config.META_LABEL_ADDITIONALINFO + ": " + representment.getAdditionalInfo();

		return reportText;
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
