package application.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DashboardController{
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab addressTab;
	@FXML
	private Tab actorTab;
	@FXML
	private Tab cityTab;
	@FXML
	private Tab customerTab;
	@FXML
	private Tab filmTab;
	@FXML
	private Tab filmActorTab;
	@FXML
	private Tab filmCatTab;
	@FXML
	private Tab inventoryTab;
	@FXML
	private Tab paymentTab;
	@FXML
	private Tab rentalTab;
	@FXML
	private Tab staffTab;
	@FXML
	private Tab storeTab;
	
	@FXML
	private ActorController actorController;
	
	@FXML
	private AddressController addressController;
	
	@FXML
	private CityController cityController;
	
	@FXML
	private CustomerController customerController;
	
	@FXML
	private FilmActorController filmActorController;
	@FXML
	private FilmCategoryController filmCategoryController;
	@FXML
	private FilmController filmController;
	@FXML
	private InventoryController inventoryController;
	@FXML
	private PaymentController paymentController;
	@FXML
	private RentalController rentalController;
	@FXML
	private StaffController staffController;
	@FXML
	private StoreController storeController;
	
	public void init() {
		Long start = System.nanoTime();
		initAllControllersAtStart();
		reInitSelectedTab();
        System.out.println("Time taken: " + ((System.nanoTime() - start) / Math.pow(10, 9)));
	}
	private void initAllControllersAtStart() {
		addressController.init();
        actorController.init();
        cityController.init();
        customerController.init();
        filmActorController.init();
        filmCategoryController.init();
        filmController.init();
        inventoryController.init();
        paymentController.init();
        rentalController.init();
        staffController.init();
        storeController.init();
	}
	private void reInitSelectedTab() {
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable,
					Tab oldValue, Tab newValue) {
				if (newValue.equals(addressTab)) {
					addressController.init();
				}
				else if (newValue.equals(actorTab)) {
			        actorController.init();
				}
				else if (newValue.equals(cityTab)) {
			        cityController.init();
				}
				else if (newValue.equals(customerTab)) {
			        customerController.init();
				}
				else if (newValue.equals(filmActorTab)) {
			        filmActorController.init();
				}
				else if (newValue.equals(filmCatTab)) {
			        filmCategoryController.init();
				}
				else if (newValue.equals(filmTab)) {
			        filmController.init();
				}
				else if (newValue.equals(inventoryTab)) {
			        inventoryController.init();
				}
				else if (newValue.equals(paymentTab)) {
			        paymentController.init();
				}
				else if (newValue.equals(rentalTab)) {
			        rentalController.init();
				}
				else if (newValue.equals(staffTab)) {
			        staffController.init();
				}
				else if (newValue.equals(storeTab)) {
			        storeController.init();
				}

			}
	    });
	}

}
