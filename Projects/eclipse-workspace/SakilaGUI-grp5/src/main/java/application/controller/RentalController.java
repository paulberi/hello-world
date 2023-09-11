package application.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Customer;
import application.entities.Inventory;
import application.entities.Rental;
import application.entities.Staff;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

public class RentalController implements Init {

    @FXML
    private DatePicker returnDate;

    @FXML
    private ComboBox<Customer> customer;

    @FXML
    private ComboBox<Staff> staff;

    @FXML
    private ComboBox<Inventory> inventory;

    @FXML
    private TableView<Rental> tableView;

    private DaoFactory daoFactory;
    private Rental selectedRental;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getRentalDao().getAll());
        customer.getItems().setAll(daoFactory.getCustomerDao().getAll());
        staff.getItems().setAll(daoFactory.getStaffDao().getAll());
        inventory.getItems().setAll(daoFactory.getInventoryDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedRental = newvalue;
                customer.setValue(selectedRental.getCustomer());
                staff.setValue(selectedRental.getStaff());
                inventory.setValue(selectedRental.getInventory());
                if (selectedRental.getReturnDate() == null) {
                    returnDate.valueProperty().set(null);
                } else {
                    returnDate.setValue(selectedRental.getReturnDate().toLocalDate());
                }
            } else {
                customer.valueProperty().set(null);
                staff.valueProperty().set(null);
                inventory.valueProperty().set(null);
                returnDate.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add() {
        if (customer.getValue() == null || staff.getValue() == null || inventory.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a customer, staff and a inventory");
        } else {
            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            if (returnDate.getValue() != null) {
                rental.setReturnDate(returnDate.getValue().atTime(LocalTime.now()));
            }
            rental.setCustomer(customer.getValue());
            rental.setStaff(staff.getValue());
            rental.setInventory(inventory.getValue());
            daoFactory.getRentalDao().create(rental);
            tableView.getItems().setAll(daoFactory.getRentalDao().getAll());
        }
    }

    @FXML
    void update() {
        if (selectedRental != null) {
            if (customer.getValue() == null || staff.getValue() == null || inventory.getValue() == null) {
            	DialogBox.information("Error", "You need to choose a customer, staff and a inventory");
            } else {
                if (returnDate.getValue() != null) {
                    selectedRental.setReturnDate(returnDate.getValue().atTime(LocalTime.now()));
                }
                selectedRental.setCustomer(customer.getValue());
                selectedRental.setStaff(staff.getValue());
                selectedRental.setInventory(inventory.getValue());
                daoFactory.getRentalDao().update(selectedRental);
                tableView.getItems().setAll(daoFactory.getRentalDao().getAll());
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }
    }

    @FXML
    void delete() {
        if (selectedRental != null) {
            try {
                daoFactory.getRentalDao().delete(selectedRental);
                selectedRental = null;
                tableView.getItems().setAll(daoFactory.getRentalDao().getAll());
            } catch (PersistenceException e) {
                // TODO: varna användaren
            	DialogBox.error("PersistenceException", "You cannot remove a store that is referenced by other objects");
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to remove it" );
        }
    }
}