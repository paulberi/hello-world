package uppgift5;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CardInput extends StackPane
{
	ObjectProperty<Card> currentCardProperty = new SimpleObjectProperty<Card>();
	BooleanProperty      cardLockedProperty  = new SimpleBooleanProperty(false);
	
	public CardInput()
	{
		setAlignment(Pos.TOP_CENTER);
		
		InputSocket input = new InputSocket();
		input.setWidth(Card.HEIGHT * 1.2);
		input.setHeight(15);
		getChildren().add(input);
		
		Rectangle cardClip = new Rectangle(Card.HEIGHT, Card.WIDTH);
		cardClip.setOpacity(1);
		cardClip.setTranslateY(input.getTranslateY());
		cardClip.setTranslateX(input.getTranslateX() +((Card.WIDTH /1.25) -6));
		
		setMinWidth(input.getWidth());
		setPrefWidth(input.getWidth());
		setMaxWidth(input.getWidth());
		setMinHeight(input.getHeight()+Card.WIDTH *.25);
		setMaxHeight(getMinHeight());
		setPrefHeight(getMinHeight());
		
		currentCardProperty.addListener((observable, oldCard, newCard) ->
		{
			if (oldCard != null)
			{
				getChildren().remove(oldCard);
				oldCard.setTranslateY(0);
				oldCard.setRotate(0);
				oldCard.setClip(null);
			}
			
			if (newCard != null)
			{
				newCard.setTranslateY((Card.WIDTH /-1.75) +6);
				//newCard.setPadding(new Insets(0, (Card.WIDTH /-1.75) +6, 0, 0));
				newCard.setRotate(90);
				newCard.setAlignment(Pos.TOP_CENTER);
				newCard.setClip(cardClip);
				
				getChildren().add(newCard);
			}
		});
		
		cardLockedProperty.addListener((observable, oldValue, newValue) ->
		{
			if (!newValue)
			{
				currentCardProperty.getValue().setOnMouseClicked(e ->
				{
					CardArea.currentCardProperty.setValue(null);
				});
			}
			else
			{
				currentCardProperty.getValue().setOnMouseClicked(null);
			}
		});
	}
}
