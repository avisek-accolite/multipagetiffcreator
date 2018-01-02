package representment.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import representment.tiffgenerator.Attachment;
import representment.tiffgenerator.Representment;
import representment.tiffgenerator.Representment.RepresentmentBuilder;
import representment.tiffgenerator.TiffGenerator;

public class TiffGeneratorMain {
	public static void main(String[] args) throws IOException {
		Attachment attachment1 = new Attachment("src/main/resources/accolite-logo.jpg");
		Attachment attachment2 = new Attachment("src/main/resources/accolite-new-office.jpg");
		Attachment attachment3 = new Attachment("src/main/resources/download.png");
		Attachment attachment4 = new Attachment("src/main/resources/accolite-black-logo.jpg");
		Attachment attachment5 = new Attachment("src/main/resources/samplepdf.pdf");
		Attachment attachment6 = new Attachment("src/main/resources/simple.pdf");
		
		Representment representment = new Representment.RepresentmentBuilder()
				.setAmount("2000")
				.setAdditionalInfo("No information available")
				.setBankRefID("ABC123")
				.setCurrency("SEK")
				.setThID(100287738L)
				.addAttachment(attachment1)
				.addAttachment(attachment2)
				.addAttachment(attachment3)
				.addAttachment(attachment4)
				.addAttachment(attachment5)
				.addAttachment(attachment6)
				.build();
		
		TiffGenerator generator = new TiffGenerator();
		
		try {
			generator.generateRepresentmentAsTiff(representment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
