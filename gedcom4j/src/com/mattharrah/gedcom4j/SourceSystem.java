package com.mattharrah.gedcom4j;

public class SourceSystem {
	public String systemId;
	public String versionNum;
	public String productName;
	public Corporation corporation;
	public SourceDate sourceDate;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Corporation getCorporation() {
		return corporation;
	}

	public void setCorporation(Corporation corporation) {
		this.corporation = corporation;
	}

	public SourceDate getSourceDate() {
		return sourceDate;
	}

	public void setSourceDate(SourceDate sourceDate) {
		this.sourceDate = sourceDate;
	}
}
