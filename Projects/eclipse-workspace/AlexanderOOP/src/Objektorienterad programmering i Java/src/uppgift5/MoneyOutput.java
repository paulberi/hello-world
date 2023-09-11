package uppgift5;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class MoneyOutput extends StackPane
{
	IntegerProperty currentAmount = new SimpleIntegerProperty(0);
	
	public MoneyOutput()
	{
		setAlignment(Pos.TOP_CENTER);
		
		InputSocket input = new InputSocket();
		input.setWidth(200);
		input.setHeight(15);
		getChildren().add(input);
		
		ImageView money = new ImageView(new Image(getClass().getResourceAsStream("money.png")));
		money.setPreserveRatio(true);
		money.setTranslateY(-32);
		money.prefWidth(200);
		
		Rectangle moneyClip = new Rectangle(input.getWidth(), 50);
		moneyClip.setTranslateY(input.getTranslateY() +37);
		moneyClip.setTranslateX(5);
		money.setClip(moneyClip);
		
		setMinHeight(input.getHeight() +moneyClip.getHeight());
		setPrefHeight(input.getHeight() +moneyClip.getHeight());
		setMaxHeight(input.getHeight() +moneyClip.getHeight());
		
		currentAmount.addListener((observable, oldAmount, newAmount) ->
		{
			if (newAmount.intValue() > 0)
			{
				money.setOnMouseClicked(e ->
				{
					currentAmount.setValue(0);
				});
				getChildren().add(money);
			}
			else
			{
				money.setOnMouseClicked(null);
				getChildren().remove(money);
			}
		});
	}
}
