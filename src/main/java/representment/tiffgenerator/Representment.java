package representment.tiffgenerator;

import java.util.ArrayList;
import java.util.List;

public class Representment {
	List<Attachment> attachments;

    private Long thID;
    private String additionalInfo;
    private String bankRefID;
    private String amount;
    private String currency;

	public Representment(List<Attachment> attachments, Long thID, String additionalInfo, String bankRefID,
			String amount, String currency) {
		super();
		this.attachments = attachments;
		this.thID = thID;
		this.additionalInfo = additionalInfo;
		this.bankRefID = bankRefID;
		this.amount = amount;
		this.currency = currency;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public Long getThID() {
		return thID;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public String getBankRefID() {
		return bankRefID;
	}

	public String getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public static class RepresentmentBuilder {
		
		List<Attachment> attachments;
		private Long thID;
	    private String additionalInfo;
	    private String bankRefID;
	    private String amount;
	    private String currency;
	    
		public Representment build() {
			return new Representment(attachments, thID, additionalInfo, bankRefID,	amount, currency);
		}

		public RepresentmentBuilder addAttachment(Attachment attachment) {
			if(this.attachments == null) {
				attachments = new ArrayList<Attachment>();
			}
			this.attachments.add(attachment);
			return this;
		}
		
		public RepresentmentBuilder setThID(Long thID) {
			this.thID = thID;
			return this;
		}

		public RepresentmentBuilder setAdditionalInfo(String additionalInfo) {
			this.additionalInfo = additionalInfo;
			return this;
		}

		public RepresentmentBuilder setBankRefID(String bankRefID) {
			this.bankRefID = bankRefID;
			return this;
		}

		public RepresentmentBuilder setAmount(String amount) {
			this.amount = amount;
			return this;
		}

		public RepresentmentBuilder setCurrency(String currency) {
			this.currency = currency;
			return this;
		}
	}
}
