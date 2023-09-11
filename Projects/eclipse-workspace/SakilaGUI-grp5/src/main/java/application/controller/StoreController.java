package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Address;
import application.entities.Staff;
import application.entities.Store;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class StoreController implements Init {

    @FXML
    private ComboBox<Staff> managerStaff;

    @FXML
    private ComboBox<Address> address;

    @FXML
    private TableView<Store> tableView;

    private Store selectedStore;
    private DaoFactory daoFactory;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        managerStaff.getItems().setAll(daoFactory.getStaffDao().getAll());
        address.getItems().setAll(daoFactory.getAddressDao().getAll());
        tableView.getItems().setAll(daoFactory.getStoreDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedStore = newvalue;
                managerStaff.setValue(selectedStore.getManagerStaff());
                address.setValue(selectedStore.getAddress());
            } else {
                address.valueProperty().set(null);
                managerStaff.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add() {
        if (managerStaff.getValue() == null || address.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a manager and a adress");
        } else {
            try {
                Store store = new Store();
                store.setAddress(address.getValue());
                store.setManagerStaff(managerStaff.getValue());
                daoFactory.getStoreDao().create(store);
                tableView.getItems().setAll(daoFactory.getStoreDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.information("Information","You cannot have the same staff and manager in a diffrent store" );
            }
        }
    }

    @FXML
    void update() {
        if (selectedStore != null) {
            if (managerStaff.getValue() == null || address.getValue() == null) {
            	DialogBox.information("Error", "You need to choose a manager and a adress");
            } else {
                try {
                    selectedStore.setAddress(address.getValue());
                    selectedStore.setManagerStaff(managerStaff.getValue());
                    daoFactory.getStoreDao().update(selectedStore);
                    tableView.getItems().setAll(daoFactory.getStoreDao().getAll());
                } catch (PersistenceException e) {
                	DialogBox.information("Information","You cannot have the same staff and manager in a diffrent store" );
                }
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }
    }

    @FXML
    void delete() {
        if (selectedStore != null) {
            try {
                daoFactory.getStoreDao().delete(selectedStore);
                selectedStore = null;
                tableView.getItems().setAll(daoFactory.getStoreDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.error("PersistenceException", "You cannot remove a store that is referenced by other objects");
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to remove it" );
        }
    }
}