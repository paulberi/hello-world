package se.metria.xplore.castor.integration.service;

import lombok.Builder;
import lombok.Data;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ArrayOfProsonaPartnerSelection;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ObjectFactory;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ProsonaPartnerSelection;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SelectionStorage {
	private final Map<String, Selection> toProsona;
	private final Map<String, Selection> toPartner;


	public SelectionStorage() {
		this.toProsona = new HashMap<>();
		this.toPartner = new HashMap<>();
	}

	public String transferToPartner(String customerID, ProsonaPartnerSelection selection) {
		return transfer(customerID, selection, toPartner);
	}

	public String transferToProsona(String customerID, ProsonaPartnerSelection selection) {
		return transfer(customerID, selection, toProsona);
	}

	private String transfer(String customerID, ProsonaPartnerSelection selection, Map<String, Selection> channel) {
		var data = new Selection.SelectionBuilder()
				.customerID(customerID)
				.selection(selection)
				.id(UUID.randomUUID())
				.build();
		channel.put(customerID, data);
		return data.id.toString();
	}

	public ArrayOfProsonaPartnerSelection getFromPartner(String customerID) {
		var items = this.toProsona.values().stream()
				.filter(sel -> sel.customerID.equals(customerID))
				.map(sel -> sel.selection)
				.collect(Collectors.toList());
		var types = new ObjectFactory();
		var array = types.createArrayOfProsonaPartnerSelection();
		array.getProsonaPartnerSelection().addAll(items);
		return array;
	}

	public ArrayOfProsonaPartnerSelection getFromProsona(String customerID) {
		var items = this.toPartner.values().stream()
				.filter(sel -> sel.customerID.equals(customerID))
				.map(sel -> sel.selection)
				.collect(Collectors.toList());
		var types = new ObjectFactory();
		var array = types.createArrayOfProsonaPartnerSelection();
		array.getProsonaPartnerSelection().addAll(items);
		return array;
	}

	public boolean remove(String id) {
		return (this.toPartner.remove(id) != null) ||
				(this.toProsona.remove(id) != null);
	}

	public ProsonaPartnerSelection get(String selectionID, String customerID) {
		var data = toPartner.get(selectionID);
		if (data != null && data.customerID.equals(customerID)) {
			return data.selection;
		}
		data = toProsona.get(selectionID);
		if (data != null && data.customerID.equals(customerID)) {
			return data.selection;
		}
		return null;
	}

	@Data
	@Builder
	private static class Selection {
		UUID id;
		String customerID;
		ProsonaPartnerSelection selection;
	}
}
