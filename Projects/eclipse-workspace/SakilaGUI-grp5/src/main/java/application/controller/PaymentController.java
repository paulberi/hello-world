package application.controller;

import application.core.DaoFactory;
import application.entities.Customer;
import application.entities.Payment;
import application.entities.Rental;
import application.entities.Staff;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PaymentController implements Init {

    @FXML
    private TextField amount;

    @FXML
    private DatePicker paymentDate;

    @FXML
    private ComboBox<Customer> customer;

    @FXML
    private ComboBox<Staff> staff;

    @FXML
    private ComboBox<Rental> rental;

    @FXML
    private TableView<Payment> tableView;

    private DaoFactory daoFactory;
    private Payment selectedPayment;

    @Override
    public void init() {
        // Set formatter for textfields for validation
        amount.setTextFormatter(FormatterHandler.getDecimalFormatter(5, 2));

        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getPaymentDao().getAll());
        customer.getItems().setAll(daoFactory.getCustomerDao().getAll());
        staff.getItems().setAll(daoFactory.getStaffDao().getAll());
        rental.getItems().setAll(daoFactory.getRentalDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedPayment = newvalue;
                amount.setText(Double.toString(selectedPayment.getAmount()));
                paymentDate.valueProperty().set(selectedPayment.getPaymentDate());
                customer.setValue(selectedPayment.getCustomer());
                staff.setValue(selectedPayment.getStaff());
                rental.setValue(selectedPayment.getRental());
            } else {
                amount.clear();
                paymentDate.valueProperty().set(null);
                customer.valueProperty().set(null);
                staff.valueProperty().set(null);
                rental.valueProperty().set(null);
            }
        });

    }

    @FXML
    void add() {
        if (amount.getText().isBlank() || paymentDate.getValue() == null || customer.getValue() == null
                || staff.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a customer, staff and a payment date");
        } else {
            Payment payment = new Payment();
            payment.setAmount(Double.parseDouble(amount.getText()));
            payment.setPaymentDate(paymentDate.getValue());
            payment.setCustomer(customer.getValue());
            payment.setStaff(staff.getValue());
            payment.setRental(rental.getValue());
            daoFactory.getPaymentDao().create(payment);
            tableView.getItems().setAll(daoFactory.getPaymentDao().getAll());
        }

    }

    @FXML
    void update() {
        if (selectedPayment != null) {
            if (amount.getText().isBlank() || paymentDate.getValue() == null || customer.getValue() == null
                    || staff.getValue() == null) {
            	DialogBox.information("Error", "You need to choose a customer, staff and a payment date");
            } else {
                selectedPayment.setAmount(Double.parseDouble(amount.getText()));
                selectedPayment.setPaymentDate(paymentDate.getValue());
                selectedPayment.setCustomer(customer.getValue());
                selectedPayment.setStaff(staff.getValue());
                selectedPayment.setRental(rental.getValue());
                daoFactory.getPaymentDao().update(selectedPayment);
                tableView.getItems().setAll(daoFactory.getPaymentDao().getAll());
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }

    }

    @FXML
    void delete() {
        if (selectedPayment != null) {
            daoFactory.getPaymentDao().delete(selectedPayment);
            selectedPayment = null;
            tableView.getItems().setAll(daoFactory.getPaymentDao().getAll());
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to delete it" );
        }
    }
}