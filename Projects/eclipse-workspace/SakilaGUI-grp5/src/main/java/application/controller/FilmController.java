package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.core.DaoFactory;
import application.entities.Film;
import application.entities.Language;
import application.extra.DialogBox;
import application.extra.Rating;
import application.extra.RatingAttributeConverter;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FilmController implements Init {

    @FXML
    private TextField title, description, rentalDuration, rentalRate, length, replaceCost, releaseYear;

    @FXML
    private ComboBox<Language> langBox, ogLangBox;

    @FXML
    private ComboBox<String> ratingBox;

    @FXML
    private CheckBox commentaryCheck, deletedSceneCheck, behindTheSceneCheck, trailersCheck;

    @FXML
    private TableView<Film> tableView;

    private List<TextField> listOfTextFields;
    private DaoFactory daoFactory;

    private Film selectedFilm;

    private StringBuilder stringBuilder;

    private RatingAttributeConverter rac;

    @Override
    public void init() {

        // Set formatter for textfields for validation
        rentalDuration.setTextFormatter(FormatterHandler.getIntegerFormatter(0, 2));
        rentalRate.setTextFormatter(FormatterHandler.getDecimalFormatter(4, 2));
        length.setTextFormatter(FormatterHandler.getIntegerFormatter(0, 4));
        replaceCost.setTextFormatter(FormatterHandler.getDecimalFormatter(5, 2));
        releaseYear.setTextFormatter(FormatterHandler.getIntegerFormatter(0, 4));

        rac = new RatingAttributeConverter();
        listOfTextFields = new ArrayList<>();
        daoFactory = DaoFactory.getInstance();
        populateGUI();
        tableView.getSelectionModel().selectedItemProperty().addListener((observeble, oldvalue, newvalue) -> {
            if (newvalue != null) {
                selectedFilm = newvalue;
                populateFields();
            } else {
                clearFields();
            }
        });
    }

    @FXML
    void add() {
        if (title.getText().isBlank() || langBox.getValue() == null || rentalDuration.getText().isBlank()
                || rentalRate.getText().isBlank() || replaceCost.getText().isBlank() || releaseYear.getText().isBlank()
                || length.getText().isBlank()) {
        	DialogBox.information("Error", "You must fill in following information to create a film:"
                    + " Title, Language, Rental Duration, Rental Rate," + " Replacement Cost, Release year, length");
            return;
        }
        Film film = null;
        film = new Film();
        film.setTitle(title.getText());
        film.setDescription(description.getText());
        film.setRentalDuration(Integer.parseInt(rentalDuration.getText()));
        film.setRentalRate(Double.parseDouble(rentalRate.getText()));
        film.setLength(Integer.parseInt(length.getText()));
        film.setReplacementCost(Double.parseDouble(replaceCost.getText()));
        film.setReleaseYear(Integer.parseInt(releaseYear.getText()));
        film.setLanguage(langBox.getValue());
        film.setOriginalLanguage(ogLangBox.getValue());
        film.setRating(rac.convertToEntityAttribute(ratingBox.getValue()));
        film.setSpecialFeatures(checkSpecialFeatures());

        daoFactory.getFilmDao().create(film);
        tableView.getItems().setAll(daoFactory.getFilmDao().getAll());
    }

    @FXML
    void update() {
        if (selectedFilm == null) {
        	DialogBox.information("Information","You need to choose a object in the table to update it" );
            return;
        }
        if (title.getText().isBlank() || langBox.getValue() == null || rentalDuration.getText().isBlank()
                || rentalRate.getText().isBlank() || replaceCost.getText().isBlank() || releaseYear.getText().isBlank()
                || length.getText().isBlank()) {
        	DialogBox.information("Error", "You must fill in following information to update a film:"
                    + " Title, Language, Rental Duration, Rental Rate," + " Replacement Cost, Release year, length");
            return;
        }
        selectedFilm.setTitle(title.getText());
        selectedFilm.setDescription(description.getText());
        selectedFilm.setRentalDuration(Integer.parseInt(rentalDuration.getText()));
        selectedFilm.setRentalRate(Double.parseDouble(rentalRate.getText()));
        selectedFilm.setLength(Integer.parseInt(length.getText()));
        selectedFilm.setReplacementCost(Double.parseDouble(replaceCost.getText()));
        selectedFilm.setReleaseYear(Integer.parseInt(releaseYear.getText()));
        selectedFilm.setLanguage(langBox.getValue());
        selectedFilm.setOriginalLanguage(ogLangBox.getValue());
        selectedFilm.setRating(rac.convertToEntityAttribute(ratingBox.getValue()));
        selectedFilm.setSpecialFeatures(checkSpecialFeatures());

        daoFactory.getFilmDao().update(selectedFilm);
        tableView.getItems().setAll(daoFactory.getFilmDao().getAll());
    }

    @FXML
    void delete() {
        if (selectedFilm == null) {
        	DialogBox.information("Information","You need to choose a object in the table to remove it" );
            return;
        }
        try {
            daoFactory.getFilmDao().delete(selectedFilm);
            selectedFilm = null;
            tableView.getItems().setAll(daoFactory.getFilmDao().getAll());
        } catch (Exception e) {
        	DialogBox.error("PersistenceException", "You cannot remove a film that is referenced by other objects");
        }
    }

    public String checkSpecialFeatures() {
        stringBuilder = new StringBuilder();
        if (commentaryCheck.isSelected()) {
            stringBuilder.append("," + commentaryCheck.getText());
        }
        if (deletedSceneCheck.isSelected()) {
            stringBuilder.append("," + deletedSceneCheck.getText());
        }
        if (behindTheSceneCheck.isSelected()) {
            stringBuilder.append("," + behindTheSceneCheck.getText());
        }
        if (trailersCheck.isSelected()) {
            stringBuilder.append("," + trailersCheck.getText());
        }
        stringBuilder.delete(0, 1);
        return stringBuilder.toString();
    }

    private void populateGUI() {
        tableView.getItems().setAll(daoFactory.getFilmDao().getAll());
        List<Language> languages = daoFactory.getLanguageDao().getAll();
        langBox.getItems().setAll(languages);
        ogLangBox.getItems().setAll(languages);

        ratingBox.getItems().add(rac.convertToDatabaseColumn(Rating.G));
        ratingBox.getItems().add(rac.convertToDatabaseColumn(Rating.PG));
        ratingBox.getItems().add(rac.convertToDatabaseColumn(Rating.PG_13));
        ratingBox.getItems().add(rac.convertToDatabaseColumn(Rating.R));
        ratingBox.getItems().add(rac.convertToDatabaseColumn(Rating.NC_17));

        listOfTextFields.add(title);
        listOfTextFields.add(description);
        listOfTextFields.add(rentalDuration);
        listOfTextFields.add(rentalRate);
        listOfTextFields.add(length);
        listOfTextFields.add(replaceCost);
        listOfTextFields.add(releaseYear);
    }

    private void clearFields() {
        for (TextField textField : listOfTextFields) {
            textField.clear();
        }
        langBox.valueProperty().set(null);
        ogLangBox.valueProperty().set(null);
        ratingBox.valueProperty().set(null);
        trailersCheck.setSelected(false);
        commentaryCheck.setSelected(false);
        deletedSceneCheck.setSelected(false);
        behindTheSceneCheck.setSelected(false);
    }

    private void populateFields() {
        title.setText(selectedFilm.getTitle());
        description.setText(selectedFilm.getDescription());
        rentalDuration.setText(Integer.toString(selectedFilm.getRentalDuration()));
        rentalRate.setText(Double.toString(selectedFilm.getRentalRate()));
        length.setText(Integer.toString(selectedFilm.getLength()));
        replaceCost.setText(Double.toString(selectedFilm.getReplacementCost()));
        releaseYear.setText(Integer.toString(selectedFilm.getReleaseYear()));
        langBox.setValue(selectedFilm.getLanguage());
        ogLangBox.setValue(selectedFilm.getOriginalLanguage());
        ratingBox.setValue(rac.convertToDatabaseColumn(selectedFilm.getRating()));

        trailersCheck.setSelected(selectedFilm.getSpecialFeatures().contains("Trailers"));
        commentaryCheck.setSelected(selectedFilm.getSpecialFeatures().contains("Commentaries"));
        deletedSceneCheck.setSelected(selectedFilm.getSpecialFeatures().contains("Deleted"));
        behindTheSceneCheck.setSelected(selectedFilm.getSpecialFeatures().contains("Behind"));
    }
}
