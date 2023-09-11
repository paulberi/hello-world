package uppgift5;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class WalletPopup extends Stage
{
	ObjectProperty<Card> pickedCard = new SimpleObjectProperty<Card>();
	BooleanProperty      isOpen     = new SimpleBooleanProperty(true);

	public WalletPopup(Object[] objects)
	{
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("Plånbok");
		
		VBox mainColumn = new VBox(16);
		mainColumn.setSpacing(16);
		mainColumn.setAlignment(Pos.CENTER);
		mainColumn.setPadding(new Insets(8));

		GridPane gridPane = new GridPane();
		gridPane.setHgap(8);
		gridPane.setVgap(gridPane.getHgap());
		int i = 0;
		int y = 0;
		while (i < objects.length)
		{
			for (int x = 0; x < 3; x++)
			{
				if (i >= objects.length)
					break;

				Card card = (Card) objects[i];
				card.setOnMouseClicked(e ->
				{
					pickedCard.setValue(card);
				});

				gridPane.add(card, x, y);
				i++;
			}

			y++;
		}

		mainColumn.getChildren().add(gridPane);

		Button pickButton = new Button("Bekräfta");
		pickButton.disableProperty().bind(Bindings.when(pickedCard.isNotNull()).then(false).otherwise(true));
		pickButton.setOnMouseClicked(e ->
		{
			pickedCard.getValue().setSurfaceStrokeColor("transparent");
			isOpen.setValue(false);
			close();
		});
		mainColumn.getChildren().add(pickButton);

		pickedCard.addListener((observable, oldCard, newCard) ->
		{
			if (oldCard != null)
				oldCard.setSurfaceStrokeColor("transparent");
			if (newCard != null)
				newCard.setSurfaceStrokeColor("red");
		});

		Scene scene = new Scene(mainColumn);
		setScene(scene);
		show();
	}
}
