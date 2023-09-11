package application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.PersistenceException;
import javax.sql.rowset.serial.SerialBlob;

import application.core.DaoFactory;
import application.entities.Address;
import application.entities.Staff;
import application.entities.Store;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StaffController implements Init {

    @FXML
    private TextField firstName, lastName, email, username, password;

    @FXML
    private ComboBox<Address> address;

    @FXML
    private CheckBox active;

    @FXML
    private ComboBox<Store> store;

    @FXML
    private TableView<Staff> tableView;

    @FXML
    private ImageView imageView;

    private DaoFactory daoFactory;

    private Staff selectedStaff;

    private Blob profilePicture;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getStaffDao().getAll());
        address.getItems().setAll(daoFactory.getAddressDao().getAll());
        store.getItems().setAll(daoFactory.getStoreDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedStaff = newvalue;
                populateFields();
            } else {
                clearFields();
            }
        });

    }

    @FXML
    void add() {
        if (firstName.getText().isBlank() || lastName.getText().isBlank() || username.getText().isBlank()
                || address.getValue() == null || store.getValue() == null) {
            DialogBox.information("Error", "You need to choose a username, address and a store");
        } else {
            Staff staff = new Staff();
            staff.setFirstName(firstName.getText());
            staff.setLastName(lastName.getText());
            staff.setEmail(email.getText());
            staff.setUserName(username.getText());
            staff.setPassword(password.getText());
            staff.setAddress(address.getValue());
            staff.setStore(store.getValue());
            staff.setPicture(profilePicture);
            staff.setActive(active.isSelected());
            daoFactory.getStaffDao().create(staff);
            tableView.getItems().setAll(daoFactory.getStaffDao().getAll());
        }
    }

    @FXML
    void update() {
        if (selectedStaff != null) {
            if (firstName.getText().isBlank() || lastName.getText().isBlank() || username.getText().isBlank()
                    || address.getValue() == null || store.getValue() == null) {
                DialogBox.information("Error", "You need to choose a username, address and a store");
            } else {
                selectedStaff.setFirstName(firstName.getText());
                selectedStaff.setLastName(lastName.getText());
                selectedStaff.setEmail(email.getText());
                selectedStaff.setUserName(username.getText());
                selectedStaff.setPassword(password.getText());
                selectedStaff.setAddress(address.getValue());
                selectedStaff.setStore(store.getValue());
                selectedStaff.setPicture(profilePicture);
                selectedStaff.setActive(active.isSelected());
                daoFactory.getStaffDao().update(selectedStaff);
                tableView.getItems().setAll(daoFactory.getStaffDao().getAll());
            }
        } else {
            DialogBox.information("Information", "You need to choose a object in the table to update it");
        }
    }

    @FXML
    void delete() {
        if (selectedStaff != null) {
            try {
                daoFactory.getStaffDao().delete(selectedStaff);
                selectedStaff = null;
                tableView.getItems().setAll(daoFactory.getStaffDao().getAll());
            } catch (PersistenceException e) {
                DialogBox.error("PersistenceException",
                        "You cannot remove a store that is referenced by other objects");
            }
        } else {
            // TODO: varna användaren
            DialogBox.information("Information", "You need to choose a object in the table to delete it");
        }
    }

    @FXML
    void chooseImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg",
                "*jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] streamBytes = inputStream.readAllBytes();
            if (streamBytes.length > 64000) {
                DialogBox.error("Error", "Your profile picture cannot be larger than 64kb");
                return;
            }
            profilePicture = new SerialBlob(streamBytes);
            imageView.setImage(getImageFromBlob(profilePicture));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image getImageFromBlob(Blob blob) {
        Blob picture = blob;
        Image image = null;
        try {
            if (picture == null) {
                return null;
            }
            InputStream input = picture.getBinaryStream();
            image = new Image(input);
            input.close();
        } catch (SQLException | IOException e1) {
            DialogBox.error("IO/SQL exception", e1.getMessage());
        }

        return image;

    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        username.clear();
        password.clear();
        address.valueProperty().set(null);
        store.valueProperty().set(null);
        active.setSelected(false);
        imageView.setImage(null);
    }

    private void populateFields() {
        firstName.setText(selectedStaff.getFirstName());
        lastName.setText(selectedStaff.getLastName());
        email.setText(selectedStaff.getEmail());
        username.setText(selectedStaff.getUserName());
        password.setText(selectedStaff.getPassword());
        active.setSelected(selectedStaff.isActive());
        address.setValue(selectedStaff.getAddress());
        store.setValue(selectedStaff.getStore());
        imageView.setImage(getImageFromBlob(selectedStaff.getPicture()));
    }
}
