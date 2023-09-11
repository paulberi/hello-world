package uppgift5;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;


public class CardArea extends VBox
{
	static ObjectProperty<Card> currentCardProperty = new SimpleObjectProperty<Card>();
	ListProperty<Card>          cardsProperty       = new SimpleListProperty<Card>(FXCollections.observableArrayList(new ArrayList<Card>()));
	BooleanProperty             cardLockedProperty  = new SimpleBooleanProperty(false);

	public CardArea()
	{
		setSpacing(4);
		setAlignment(Pos.TOP_CENTER);

		Header header = new Header("Kort");
		getChildren().add(header);
		
		CardInput cardInput = new CardInput();
		getChildren().add(cardInput);

		cardInput.cardLockedProperty.bind(cardLockedProperty);
		cardInput.currentCardProperty.bind(currentCardProperty);
	}

	public void addCard(Card card)
	{
		cardsProperty.add(card);
	}
}
