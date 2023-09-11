package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Address;
import application.entities.Customer;
import application.entities.Store;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomerController implements Init {

    @FXML
    private TextField firstName, lastName, email;

    @FXML
    private ComboBox<Address> address;

    @FXML
    private ComboBox<Store> store;

    @FXML
    private CheckBox activeCustomer;

    @FXML
    private TableView<Customer> tableView;
    private DaoFactory daoFactory;
    private Customer selectedCustomer;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getCustomerDao().getAll());
        address.getItems().setAll(daoFactory.getAddressDao().getAll());
        store.getItems().setAll(daoFactory.getStoreDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedCustomer = newvalue;
                firstName.setText(selectedCustomer.getFirstName());
                lastName.setText(selectedCustomer.getLastName());
                email.setText(selectedCustomer.getEmail());
                address.setValue(selectedCustomer.getAddress());
                store.setValue(selectedCustomer.getStore());
            } else {
                firstName.clear();
                lastName.clear();
                email.clear();
                address.valueProperty().set(null);
                store.valueProperty().set(null);
            }
        });

    }

    @FXML
    void add() {
        if (firstName.getText().isBlank() || lastName.getText().isBlank() || address.getValue() == null
                || store.getValue() == null) {
        	DialogBox.information("Error", "You must fill in following information to create a Customer:"
                    + "first name, last name, address and store");
            return;
        } else {
            Customer customer = new Customer();
            customer.setFirstName(firstName.getText());
            customer.setLastName(lastName.getText());
            customer.setEmail(email.getText());
            customer.setAddress(address.getValue());
            customer.setStore(store.getValue());
            daoFactory.getCustomerDao().create(customer);
            tableView.getItems().setAll(daoFactory.getCustomerDao().getAll());
        }
    }

    @FXML
    void update() {
        if (selectedCustomer != null) {
            if (firstName.getText().isBlank() || lastName.getText().isBlank() || address.getValue() == null
                    || store.getValue() == null) {
            	DialogBox.information("Error", "You must fill in following information to update a Customer:"
                        + "first name, last name, address and store");
                return;
            } else {
                selectedCustomer.setFirstName(firstName.getText());
                selectedCustomer.setLastName(lastName.getText());
                selectedCustomer.setEmail(email.getText());
                selectedCustomer.setAddress(address.getValue());
                selectedCustomer.setStore(store.getValue());
                daoFactory.getCustomerDao().update(selectedCustomer);
                tableView.getItems().setAll(daoFactory.getCustomerDao().getAll());
            }
        } else {
            // TODO: varna användaren
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }

    }

    @FXML
    void delete() {
        if (selectedCustomer != null) {
            try {
                daoFactory.getCustomerDao().delete(selectedCustomer);
                selectedCustomer = null;
                tableView.getItems().setAll(daoFactory.getCustomerDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.error("PersistenceException", "You cannot remove a customer that is referenced by other objects");
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to remove it" );
        }
    }

}
