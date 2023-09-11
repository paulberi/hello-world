package uppgift5;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Display extends StackPane
{
	private StringProperty left1TextProperty  = new SimpleStringProperty();
	private StringProperty left2TextProperty  = new SimpleStringProperty();
	private StringProperty left3TextProperty  = new SimpleStringProperty();
	private StringProperty left4TextProperty  = new SimpleStringProperty();
	private StringProperty right1TextProperty = new SimpleStringProperty();
	private StringProperty right2TextProperty = new SimpleStringProperty();
	private StringProperty right3TextProperty = new SimpleStringProperty();
	private StringProperty right4TextProperty = new SimpleStringProperty();

	StringProperty labelTextProperty     = new SimpleStringProperty();
	StringProperty subTextProperty       = new SimpleStringProperty();
	StringProperty inputTextProperty     = new SimpleStringProperty();
	StringProperty inputFeedbackProperty = new SimpleStringProperty();

	private FadeTransition fadePinCodeTransition;
	private FadeTransition fadeBalanceTransition;
	private FadeTransition fadeTextTransition;
	private Label          showTextLabel;

	public Display()
	{
		super();
		setPrefSize(400, 400);

		Rectangle displayBackground = new Rectangle();
		displayBackground.setWidth(400);
		displayBackground.setHeight(400);
		displayBackground.setArcWidth(20);
		displayBackground.setArcHeight(20);
		displayBackground.setFill(Color.DARKGRAY);
		displayBackground.setStroke(Color.BLACK);
		displayBackground.setStrokeWidth(3);
		getChildren().add(displayBackground);

		VBox labelColumn = new VBox();
		labelColumn.setAlignment(Pos.TOP_CENTER);
		Label label = new Label();
		label.textProperty().bind(labelTextProperty);
		label.setStyle("-fx-font-size: 26");
		label.textProperty().bind(labelTextProperty);
		Label sub = new Label();
		sub.textProperty().bind(labelTextProperty);
		sub.setStyle("-fx-font-size: 16");
		sub.textProperty().bind(subTextProperty);
		labelColumn.getChildren().addAll(label, sub);
		getChildren().add(labelColumn);

		VBox inputColumn = new VBox(2);
		inputColumn.setAlignment(Pos.CENTER);
		Label inputLabel = new Label();
		inputLabel.textProperty().bind(inputTextProperty);
		inputLabel.setStyle("-fx-font-size: 36; -fx-font-weight: bold");
		Label inputFeedbackLabel = new Label();
		inputFeedbackLabel.setStyle("-fx-text-fill: red");
		inputFeedbackLabel.textProperty().bind(inputFeedbackProperty);
		inputColumn.getChildren().addAll(inputLabel, inputFeedbackLabel);
		getChildren().add(inputColumn);

		Label wrongPinCodeLabel = new Label("Fel PIN-kod");
		wrongPinCodeLabel.setStyle("-fx-text-fill: red; -fx-font-size: 42");
		wrongPinCodeLabel.setOpacity(0);
		getChildren().add(wrongPinCodeLabel);
		fadePinCodeTransition = new FadeTransition();
		fadePinCodeTransition.setFromValue(1);
		fadePinCodeTransition.setToValue(0);
		fadePinCodeTransition.setDuration(Duration.millis(1500));
		fadePinCodeTransition.setNode(wrongPinCodeLabel);

		Label balanceTooLowLabel = new Label("För lite pengar på kontot");
		balanceTooLowLabel.setStyle("-fx-text-fill: red; -fx-font-size: 32");
		balanceTooLowLabel.setOpacity(0);
		getChildren().add(balanceTooLowLabel);
		fadeBalanceTransition = new FadeTransition();
		fadeBalanceTransition.setFromValue(1);
		fadeBalanceTransition.setToValue(0);
		fadeBalanceTransition.setDuration(Duration.millis(1500));
		fadeBalanceTransition.setNode(balanceTooLowLabel);
		
		showTextLabel = new Label();
		showTextLabel.setStyle("-fx-text-fill: green; -fx-font-size: 32");
		showTextLabel.setOpacity(0);
		getChildren().add(showTextLabel);
		fadeTextTransition = new FadeTransition();
		fadeTextTransition.setFromValue(1);
		fadeTextTransition.setToValue(0);
		fadeTextTransition.setDuration(Duration.millis(1500));
		fadeTextTransition.setNode(showTextLabel);

		VBox leftButtonLabels = new VBox(DisplayArea.BUTTONS_SPACING);
		leftButtonLabels.setAlignment(Pos.CENTER_LEFT);
		leftButtonLabels.setPadding(new Insets(0, 0, 0, 8));
		for (int i = 0; i < 4; i++)
		{
			Label l = new Label();
			l.setPrefSize(getPrefWidth() / 2, NumpadButton.SIZE);
			switch (i)
			{
				case 0 :
					l.textProperty().bind(left1TextProperty);
					break;
				case 1 :
					l.textProperty().bind(left2TextProperty);
					break;
				case 2 :
					l.textProperty().bind(left3TextProperty);
					break;
				case 3 :
					l.textProperty().bind(left4TextProperty);
					break;
			}

			leftButtonLabels.getChildren().add(l);
		}

		getChildren().add(leftButtonLabels);

		VBox rightButtonLabels = new VBox(DisplayArea.BUTTONS_SPACING);
		rightButtonLabels.setAlignment(Pos.CENTER_RIGHT);
		rightButtonLabels.setPadding(new Insets(0, 8, 0, 0));
		for (int i = 0; i < 4; i++)
		{
			Label l = new Label();
			l.setPrefSize(getPrefWidth() / 2, NumpadButton.SIZE);
			l.setAlignment(Pos.CENTER_RIGHT);
			switch (i)
			{
				case 0 :
					l.textProperty().bind(right1TextProperty);
					break;
				case 1 :
					l.textProperty().bind(right2TextProperty);
					break;
				case 2 :
					l.textProperty().bind(right3TextProperty);
					break;
				case 3 :
					l.textProperty().bind(right4TextProperty);
					break;
			}

			rightButtonLabels.getChildren().add(l);
		}

		getChildren().add(rightButtonLabels);
		setAlignment(rightButtonLabels, Pos.CENTER_RIGHT);
	}

	public void setLeft1TextProperty(String str)
	{
		left1TextProperty.setValue(str);
	}

	public void setLeft2TextProperty(String str)
	{
		left2TextProperty.setValue(str);
	}

	public void setLeft3TextProperty(String str)
	{
		left3TextProperty.setValue(str);
	}

	public void setLeft4TextProperty(String str)
	{
		left4TextProperty.setValue(str);
	}

	public void setRight1TextProperty(String str)
	{
		right1TextProperty.setValue(str);
	}

	public void setRight2TextProperty(String str)
	{
		right2TextProperty.setValue(str);
	}

	public void setRight3TextProperty(String str)
	{
		right3TextProperty.setValue(str);
	}

	public void setRight4TextProperty(String str)
	{
		right4TextProperty.setValue(str);
	}

	public void showWrongPinCode()
	{
		fadePinCodeTransition.playFrom(new Duration(0));
	}

	public void showBalanceTooLow()
	{
		fadeBalanceTransition.playFrom(new Duration(0));
	}

	public void showText(String text)
	{
		showTextLabel.setText(text);
		fadeTextTransition.playFrom(new Duration(0));
	}
}
