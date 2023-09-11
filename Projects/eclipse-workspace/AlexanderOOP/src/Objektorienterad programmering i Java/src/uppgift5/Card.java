package uppgift5;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Card extends StackPane
{
	public final transient static double RATIO  = 0.64;
	public final transient static double WIDTH  = 120;
	public final transient static double HEIGHT = WIDTH * RATIO;

	private transient StringProperty surfaceStrokeColor = new SimpleStringProperty();

	private LongProperty    numberProperty  = new SimpleLongProperty();
	private IntegerProperty pinCodeProperty = new SimpleIntegerProperty();

	public Card()
	{
		this.setMaxSize(WIDTH, HEIGHT);
		this.setPrefSize(WIDTH, HEIGHT);

		Rectangle surface = new Rectangle();
		surface.setWidth(WIDTH);
		surface.setHeight(HEIGHT);
		surface.setArcWidth(20);
		surface.setArcHeight(20);
		surface.setStroke(Color.TRANSPARENT);
		surfaceStrokeColor.addListener((observable, oldColor, newColor) ->
		{
			surface.setStroke(Color.valueOf(newColor));
		});
		surface.setFill(Color.CADETBLUE);

		setAlignment(surface, Pos.CENTER);
		getChildren().add(surface);

		HBox headers = getHeaders();
		setAlignment(headers, Pos.TOP_LEFT);
		getChildren().add(headers);

		VBox chipAndNumber = new VBox(2);
		chipAndNumber.setPadding(new Insets(0, 0, 0, 8));
		chipAndNumber.setAlignment(Pos.CENTER_LEFT);
		Rectangle chip = new Rectangle();
		chip.setWidth(16);
		chip.setHeight(chip.getWidth() * RATIO);
		chip.setArcWidth(5);
		chip.setArcHeight(chip.getArcWidth());
		chip.setFill(Color.valueOf("#c1a45b"));
		chipAndNumber.getChildren().add(chip);
		Label numberLabel = new Label();
		numberProperty.addListener((observable, oldValue, newValue) ->
		{
			String str = String.valueOf(newValue).replaceAll("(.{4})", "$1 ").trim();
			numberLabel.setText(str);
		});
		numberLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");
		chipAndNumber.getChildren().add(numberLabel);
		getChildren().add(chipAndNumber);
	}
	
	public Card(CardData card)
	{
		this();
		numberProperty.setValue(card.getNumber());
		pinCodeProperty.setValue(card.getPinCode());
	}

	private HBox getHeaders()
	{
		HBox headers = new HBox();
		headers.setPadding(new Insets(4, 4, 0, 4));

		Label bankLabel = new Label("..EA Finans");
		bankLabel.setStyle("-fx-font-style: italic; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px");
		headers.getChildren().add(bankLabel);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(10, 1);
		headers.getChildren().add(spacer);

		Label visaLabel = new Label("VISA");
		visaLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px");
		headers.getChildren().add(visaLabel);

		return headers;
	}

	public long getNumber()
	{
		return numberProperty.getValue();
	}

	public void setNumber(long number)
	{
		this.numberProperty.setValue(number);
	}

	public long getPinCode()
	{
		return pinCodeProperty.getValue();
	}

	public void setPinCode(int value)
	{
		if (String.valueOf(value).length() == 4)
			this.pinCodeProperty.setValue(value);
		else
			System.out.println("Error: The pin provided has to be 4 digits long.");
	}
	
	public void setSurfaceStrokeColor(String hex)
	{
		surfaceStrokeColor.setValue(hex);
	}
}
