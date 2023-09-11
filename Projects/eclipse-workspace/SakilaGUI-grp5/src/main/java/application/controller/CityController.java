package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.City;
import application.entities.Country;
import application.extra.DialogBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CityController implements Init {

    @FXML
    private TextField cityField;

    @FXML
    private ComboBox<Country> country;

    @FXML
    private TableView<City> tableView;

    private DaoFactory daoFactory;

    private City selectedCity;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getCityDao().getAll());
        country.getItems().setAll(daoFactory.getCountryDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedCity = newvalue;
                cityField.setText(selectedCity.getCityName());
                country.setValue(selectedCity.getCountry());
            } else {
                cityField.clear();
                country.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add(ActionEvent event) {
        if (cityField.getText().isBlank() || country.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a city name and a country");
        	return;
        } else {
            City city = null;
            city = new City();
            city.setCityName(cityField.getText());
            city.setCountry(country.getValue());
            daoFactory.getCityDao().create(city);
            tableView.getItems().setAll(daoFactory.getCityDao().getAll());
        }
    }

    @FXML
    void update(ActionEvent event) {
        if (selectedCity != null) {
            if (cityField.getText().isBlank() || country.getValue() == null) {
            	DialogBox.information("Error", "You need to choose a city name and a country");
            	return;
            } else {
                selectedCity.setCityName(cityField.getText());
                selectedCity.setCountry(country.getValue());
                daoFactory.getCityDao().update(selectedCity);
                tableView.getItems().setAll(daoFactory.getCityDao().getAll());
            }
        } else {
            // TODO: varna användaren
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }

    }

    @FXML
    void delete(ActionEvent event) {
        if (selectedCity != null) {
            try {
                daoFactory.getCityDao().delete(selectedCity);
                selectedCity = null;
                tableView.getItems().setAll(daoFactory.getCityDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.error("PersistenceException", "You cannot remove a city that is referenced by other objects");
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to remove it" );
        }
    }
}
