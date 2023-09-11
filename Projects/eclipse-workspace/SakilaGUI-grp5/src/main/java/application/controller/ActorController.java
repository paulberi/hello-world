package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Actor;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ActorController implements Init {

    @FXML
    private TextField firstName, lastName;

    @FXML
    private TableView<Actor> tableView;

    private Actor selectedActor;

    private DaoFactory daoFactory;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getActorDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedActor = newvalue;
                firstName.setText(selectedActor.getFirstName());
                lastName.setText(selectedActor.getLastName());
            } else {
                firstName.clear();
                lastName.clear();
            }
        });
    }

    @FXML
    void add() {
        if (firstName.getText().isBlank() || lastName.getText().isBlank()) {
        	DialogBox.information("Error", "You need to choose a first name and a last name");
        } else {
            Actor actor = new Actor();
            actor.setFirstName(firstName.getText());
            actor.setLastName(lastName.getText());
            daoFactory.getActorDao().create(actor);
            tableView.getSelectionModel().selectedItemProperty();
            tableView.getItems().setAll(daoFactory.getActorDao().getAll());
        }
    }

    @FXML
    void update() {
        if (selectedActor != null) {
            if (firstName.getText().isBlank() || lastName.getText().isBlank()) {
            	DialogBox.information("Error", "You need to choose a first name and a last name");
            } else {
                selectedActor.setFirstName(firstName.getText());
                selectedActor.setLastName(lastName.getText());
                daoFactory.getActorDao().update(selectedActor);
                tableView.getSelectionModel().selectedItemProperty();
                tableView.getItems().setAll(daoFactory.getActorDao().getAll());
            }
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
        }
    }

    @FXML
    void delete() {
        if (selectedActor != null) {
            try {
                daoFactory.getActorDao().delete(selectedActor);
                selectedActor = null;
                tableView.getItems().setAll(daoFactory.getActorDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.error("PersistenceException", "You cannot remove a store that is referenced by other objects");
            }
        } else {
            // TODO: varna användaren
        	DialogBox.information("Information","You need to choose a object in the table to r it" );
        }
    }
}
