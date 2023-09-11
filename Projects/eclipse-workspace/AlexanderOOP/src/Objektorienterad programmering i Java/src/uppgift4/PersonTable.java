package uppgift4;

import java.util.Arrays;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class PersonTable extends TableView<Person>
{
	ContextMenu     contextMenu;
	EditPopup       editPopup;
	BooleanProperty popupOpen = new SimpleBooleanProperty();

	public PersonTable()
	{
		popupOpen.setValue(false);

		Label placeholderLabel = new Label("Table is empty");
		placeholderLabel.setStyle("-fx-opacity: .5");
		setPlaceholder(placeholderLabel);
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Person, String> firstNameColumn = new TableColumn<>("First name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		TableColumn<Person, String> lastNameColumn = new TableColumn<>("Last name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

		getColumns().addAll(Arrays.asList(firstNameColumn, lastNameColumn, ageColumn));

		setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
			@Override
			public TableRow<Person> call(TableView<Person> tableView)
			{
				final TableRow<Person> row         = new TableRow<>();
				final ContextMenu      contextMenu = new ContextMenu();
				MenuItem               editItem    = new MenuItem("Edit");
				editItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event)
					{
						openEditDialog();
					}
				});
				MenuItem duplicateItem = new MenuItem("Duplicate");
				duplicateItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event)
					{
						getItems().add(getSelectionModel().getSelectedItem());
					}
				});
				MenuItem removeItem = new MenuItem("Delete");
				removeItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event)
					{
						getItems().remove(row.getItem());
						getSelectionModel().clearSelection();
					}
				});
				contextMenu.getItems().addAll(editItem, duplicateItem, removeItem);

				row.contextMenuProperty().bind(Bindings.when(row.emptyProperty().not()).then(contextMenu).otherwise((ContextMenu) null));

				row.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
				{
					if (row.isEmpty())
						getSelectionModel().clearSelection();
				});

				return row;
			}
		});
	}

	private void openEditDialog()
	{
		editPopup = new EditPopup(getSelectionModel().getSelectedItem());
		popupOpen.setValue(true);
		editPopup.person.addListener((observer, oldValue, newValue) ->
		{
			getItems().set(getSelectionModel().getSelectedIndex(), newValue);
			editPopup = null;
			popupOpen.setValue(false);
		});
		editPopup.isOpen.addListener((observer, oldValue, newValue) ->
		{
			editPopup = null;
			popupOpen.setValue(false);
		});
	}

	public void closePopup()
	{
		if (popupOpen.getValue())
		{
			editPopup.close();
			editPopup = null;
			popupOpen.setValue(false);
		}

	}
}
