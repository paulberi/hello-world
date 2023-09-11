package se.metria.skogsmaskindata.service.imports.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "object-result-info")
public class Info {

	@XmlElement(name = "ObjectNumber")
	public String objectNumber;

	@XmlElement(name = "Requestor")
	public String executor;

	@XmlElement(name = "EndDate")
	public String enddate;

	@XmlElement(name = "Purpose")
	public String Purpose;

	public String getObjectNumber() {
		return objectNumber;
	}

	public String getExecutor() {
		return executor;
	}

	public String getEnddate() {
		return enddate;
	}

	public String getPurpose() {
		return Purpose;
	}
}
