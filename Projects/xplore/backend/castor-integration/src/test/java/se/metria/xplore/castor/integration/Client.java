package se.metria.xplore.castor.integration;

import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ObjectFactory;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ProsonaPartnerSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tempuri.IProsonaRealestateExchange;
import org.tempuri.IProsonaRealestateExchangeTransferSelectionToProsonaProsonaFaultFaultFaultMessage;

@Component
public class Client {
	@Value("apiKey12345")
	private String apiKey;

	@Value("customer1234")
	private String customerID;

	@Autowired
	private IProsonaRealestateExchange serviceProxy;

	public String upload(String name) throws IProsonaRealestateExchangeTransferSelectionToProsonaProsonaFaultFaultFaultMessage {
		ObjectFactory types = new ObjectFactory();


		ProsonaPartnerSelection selection = types.createProsonaPartnerSelection();
		selection.setDescription("Client test description");
		selection.setName(name);
		return serviceProxy.transferSelectionToProsona(selection, customerID, apiKey);
	}
}
