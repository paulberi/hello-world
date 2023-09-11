package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Film;
import application.entities.Inventory;
import application.entities.Store;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class InventoryController implements Init {

    @FXML
    private ComboBox<Film> film;

    @FXML
    private ComboBox<Store> store;

    @FXML
    private TableView<Inventory> tableView;

    private DaoFactory daoFactory;
    private Inventory selectedInventory;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getInventoryDao().getAll());
        store.getItems().setAll(daoFactory.getStoreDao().getAll());
        film.getItems().setAll(daoFactory.getFilmDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedInventory = newvalue;
                store.setValue(selectedInventory.getStore());
                film.setValue(selectedInventory.getFilm());
            } else {
                store.valueProperty().set(null);
                film.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add() {
        if (film.getValue() == null || store.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a film and a store");
        } else {
            Inventory inventory = new Inventory();
            inventory.setFilm(film.getValue());
            inventory.setStore(store.getValue());
            daoFactory.getInventoryDao().create(inventory);
            tableView.getItems().setAll(daoFactory.getInventoryDao().getAll());
        }
    }

    @FXML
    void update() {
        if (selectedInventory != null) {
            if (film.getValue() == null || store.getValue() == null) {
            	DialogBox.information("Error", "You need to choose a film and a store");
            } else {
                selectedInventory.setFilm(film.getValue());
                selectedInventory.setStore(store.getValue());
                daoFactory.getInventoryDao().update(selectedInventory);
                tableView.getItems().setAll(daoFactory.getInventoryDao().getAll());
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }
    }

    @FXML
    void delete() {
        if (selectedInventory != null) {
            try {
                daoFactory.getInventoryDao().delete(selectedInventory);
                selectedInventory = null;
                tableView.getItems().setAll(daoFactory.getInventoryDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.error("PersistenceException", "You cannot remove a inventory that is referenced by other objects");
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to delete it" );
        }
    }
}