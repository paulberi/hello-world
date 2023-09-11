package application.controller;

import javax.persistence.PersistenceException;

import application.core.DaoFactory;
import application.entities.Category;
import application.entities.Film;
import application.entities.FilmCategory;
import application.extra.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class FilmCategoryController implements Init {

    @FXML
    private ComboBox<Film> film;

    @FXML
    private ComboBox<Category> category;

    @FXML
    private TableView<FilmCategory> tableView;

    private DaoFactory daoFactory;
    private FilmCategory selectedFilmCategory;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        tableView.getItems().setAll(daoFactory.getFilmCategoryDao().getAll());
        category.getItems().setAll(daoFactory.getCategoryDao().getAll());
        film.getItems().setAll(daoFactory.getFilmDao().getAll());
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedFilmCategory = newvalue;
                category.setValue(selectedFilmCategory.getCategory());
                film.setValue(selectedFilmCategory.getFilm());
            } else {
                category.valueProperty().set(null);
                film.valueProperty().set(null);
            }
        });
    }

    @FXML
    void add() {
        if (film.getValue() == null || category.getValue() == null) {
        	DialogBox.information("Error", "You need to choose a film and a category");
        } else {
            try {
                FilmCategory filmCategory = new FilmCategory();
                filmCategory.setCategory(category.getValue());
                filmCategory.setFilm(film.getValue());
                daoFactory.getFilmCategoryDao().create(filmCategory);
                tableView.getItems().setAll(daoFactory.getFilmCategoryDao().getAll());
            } catch (PersistenceException e) {
            	DialogBox.information("Information","Identical film-category already exists" );
            }
        }
    }

    @FXML
    void delete() {
        if (selectedFilmCategory != null) {
            daoFactory.getFilmCategoryDao().delete(selectedFilmCategory);
            selectedFilmCategory = null;
            tableView.getItems().setAll(daoFactory.getFilmCategoryDao().getAll());
        } else {
        	DialogBox.information("Information","You need to choose a object in the table to delete it" );
        }

    }
}