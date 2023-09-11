package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Actor;
import application.entities.Film;
import application.entities.FilmActor;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class FilmActorController implements Init {

    @FXML
    private ComboBox<Film> film;

    @FXML
    private ComboBox<Actor> actor;

    @FXML
    private TableView<FilmActor> tableView;

    private DaoFactory daoFactory;
    private FilmActor selectedFilmActor;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getFilmActorDao().getAll());
        film.getItems().setAll(daoFactory.getFilmDao().getAll());
        actor.getItems().setAll(daoFactory.getActorDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedFilmActor = newvalue;
                actor.setValue(selectedFilmActor.getActor());
                film.setValue(selectedFilmActor.getFilm());
            } else {
                actor.valueProperty().set(null);
                film.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add() {
        if (film.getValue() == null || actor.getValue() == null) {
            // TODO: varna användaren
        	DialogBox.information("Error", "You need to choose a film and a actor");
        } else {
            try {
                FilmActor filmActor = new FilmActor();
                filmActor.setFilm(film.getValue());
                filmActor.setActor(actor.getValue());
                daoFactory.getFilmActorDao().create(filmActor);
                tableView.getItems().setAll(daoFactory.getFilmActorDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.information("Information","Identical film-actor already exists" );
            }
        }
    }

    @FXML
    void delete() {
        if (selectedFilmActor != null) {
            daoFactory.getFilmActorDao().delete(selectedFilmActor);
            selectedFilmActor = null;
            tableView.getItems().setAll(daoFactory.getFilmActorDao().getAll());
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to delete it" );

        }
    }
}