package uppgift5;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class ReceiptOutput extends StackPane
{
	BooleanProperty showReceiptProperty = new SimpleBooleanProperty(false);
	
	public ReceiptOutput()
	{
		setAlignment(Pos.TOP_CENTER);
		
		InputSocket input = new InputSocket();
		input.setWidth(120);
		input.setHeight(15);
		getChildren().add(input);
		
		Rectangle receipt = new Rectangle(110, 40);
		receipt.setStyle("-fx-stroke-width: 1; -fx-stroke: black; -fx-fill: white");
		receipt.setX((input.getWidth() -receipt.getWidth()) /2);
		receipt.setTranslateY(5);
		
		setMinHeight(input.getHeight() +receipt.getHeight());
		setMaxHeight(getMinHeight());
		setPrefHeight(getMinHeight());
		
		showReceiptProperty.addListener((observable, oldValue, newValue) ->
		{
			if (newValue.booleanValue())
			{
				receipt.setOnMouseClicked(e ->
				{
					showReceiptProperty.setValue(false);
				});
				getChildren().add(receipt);
			}
			else
			{
				receipt.setOnMouseClicked(null);
				getChildren().remove(receipt);
			}
		});
	}
	
	public void showReceipt()
	{
		showReceiptProperty.setValue(true);
	}
}
