package application.controller;

import application.core.DaoFactory;
import application.entities.Address;
import application.entities.City;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AddressController implements Init {

	@FXML private TextField address1, address2, district, phone,
			postalCode, xCoord, yCoord;

	@FXML private ComboBox<City> city;

	@FXML private TableView<Address> tableView;

	private DaoFactory daoFactory;
	private Address selectedAddress;

	@Override
	public void init() {

		// Set formatter for textfields for validation
		xCoord.setTextFormatter(FormatterHandler.getCoordinateFormatter(180));
		yCoord.setTextFormatter(FormatterHandler.getCoordinateFormatter(90));
		postalCode.setTextFormatter(FormatterHandler.getIntegerFormatter(0, 10));
		phone.setTextFormatter(FormatterHandler.getIntegerFormatter(0,20));

		daoFactory = DaoFactory.getInstance();
		tableView.getItems().setAll(daoFactory.getAddressDao().getAll());
		city.getItems().addAll(daoFactory.getCityDao().getAll());
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((observeble, oldvalue, newvalue) -> {
					if (newvalue != null) {
						selectedAddress = newvalue;
						address1.setText(selectedAddress.getAddress());
						address2.setText(selectedAddress.getAddress2());
						district.setText(selectedAddress.getDistrict());
						phone.setText(selectedAddress.getPhone());
						postalCode
								.setText(selectedAddress.getPostalCode());
						city.setValue(selectedAddress.getCity());
						xCoord.setText(selectedAddress.getXCoordinate());
						yCoord.setText(selectedAddress.getYCoordinate());

					} else {
						address1.clear();
						address2.clear();
						district.clear();
						phone.clear();
						postalCode.clear();
						city.valueProperty().set(null);
						xCoord.clear();
						yCoord.clear();
					}
				});

	}

	@FXML
	void add() {
		Address address = new Address();
		address.setAddress(address1.getText());
		address.setAddress2(address2.getText());
		address.setCity(city.getValue());
		address.setDistrict(district.getText());
		address.setPhone(phone.getText());
		address.setPostalCode(postalCode.getText());
		address.setLocation("POINT(" + xCoord.getText() + " " + yCoord.getText() + ")");
		daoFactory.getAddressDao().create(address);
		tableView.getItems().setAll(daoFactory.getAddressDao().getAll());

	}

	@FXML
	void delete() {
		daoFactory.getAddressDao().delete(selectedAddress);
		tableView.getItems().setAll(daoFactory.getAddressDao().getAll());
	}

	@FXML
	void update() {
		selectedAddress.setAddress(address1.getText());
		selectedAddress.setAddress2(address2.getText());
		selectedAddress.setCity(city.getValue());
		selectedAddress.setDistrict(district.getText());
		selectedAddress.setPhone(phone.getText());
		selectedAddress.setPostalCode(postalCode.getText());
		selectedAddress.setLocation("POINT(" + xCoord.getText() + " " + yCoord.getText() + ")");
		daoFactory.getAddressDao().update(selectedAddress);
		tableView.getItems().setAll(daoFactory.getAddressDao().getAll());
	}
	void getCoordinates() {

	}
}