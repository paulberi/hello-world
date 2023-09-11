package uppgift3;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application
{
	public boolean         timerRunning   = false;
	public boolean         requestedSplit = false;
	public ArrayList<Long> splits;
	public long            previousSplit;


	public static void main(String[] args)
	{
		launch();
	}


	@Override
	public void start(Stage stage)
	{
		// Main column
		VBox mainColumn = new VBox();
		mainColumn.setAlignment(Pos.TOP_CENTER);
		mainColumn.setSpacing(16);
		mainColumn.setStyle("-fx-padding: 80 0 0 0;");


		// --> Time pane
		StackPane timePane = new StackPane();

		// Time label
		Label timeLabel = new Label("00:00.000");
		timeLabel.setStyle("-fx-font-size: 42;");

		// Current split tracker
		Arc arc = new Arc();
		arc.setStyle("-fx-opacity: 75%;");
		arc.setManaged(false);
		arc.setStrokeWidth(3);
		arc.setStartAngle(90);
		arc.setFill(Color.TRANSPARENT);
		arc.setStroke(Color.ORANGE);
		mainColumn.getChildren().add(arc);
		timePane.getChildren().addAll(arc, timeLabel);
		timePane.setPadding(new Insets(0, 0, 16, 0));

		mainColumn.getChildren().add(timePane);
		// <!-- Time pane


		// --> Buttons row
		HBox buttonsRow = new HBox();
		buttonsRow.setAlignment(Pos.CENTER);
		buttonsRow.setSpacing(16);
		mainColumn.getChildren().add(buttonsRow);

		// Start/stop button
		Button startStopButton = new Button("Start");
		buttonsRow.getChildren().add(startStopButton);

		// Split/reset button
		Button splitResetButton = new Button("Split");
		splitResetButton.setDisable(true);
		buttonsRow.getChildren().add(splitResetButton);
		// <!-- Buttons row


		// Scrollable split list
		ScrollPane splitsScroll = new ScrollPane();
		splitsScroll.pannableProperty().set(true);
		splitsScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		VBox splitsColumn = new VBox();
		splitsColumn.setAlignment(Pos.CENTER_LEFT);
		splitsColumn.setPadding(new Insets(16, 16, 0, 16));
		splitsScroll.setContent(splitsColumn);
		mainColumn.getChildren().add(splitsScroll);
		splitsColumn.heightProperty().addListener(observable -> splitsScroll.setVvalue(1));


		// Timer
		AnimationTimer timer = new AnimationTimer() {
			long startTime;

			@Override
			public void start()
			{
				previousSplit = 0;
				splits        = new ArrayList<Long>();
				timerRunning  = true;
				startTime     = System.currentTimeMillis();
				startStopButton.setText("Stop");
				splitResetButton.setDisable(false);

				super.start();
			}

			@Override
			public void stop()
			{
				// Last split
				requestedSplit = true;
				handle(0);

				// Reset
				timerRunning = false;
				startStopButton.setText("Start");
				startStopButton.setDisable(true);
				splitResetButton.setText("Reset");
				timeLabel.setStyle("-fx-font-size: 42; -fx-font-size: 42; -fx-effect:	dropshadow(three-pass-box, orange, 5, 0, 0, 0);");

				super.stop();
			}

			@Override
			public void handle(long now)
			{
				long     elapsedTime       = System.currentTimeMillis() - startTime;
				String[] elapsedTimeLabels = getTimes(elapsedTime);

				timeLabel.setText(elapsedTimeLabels[0] + ":" + elapsedTimeLabels[1] + "." + elapsedTimeLabels[2]);

				// Current split
				if (splits.size() > 0)
				{
					long bestSplitTotal = 0;
					long lastSplitTime  = 0;
					for (long s : splits)
					{
						lastSplitTime += s;

						if (bestSplitTotal == 0 || s < bestSplitTotal)
							bestSplitTotal = s;
					}

					arc.setLength(360 * ((double) (elapsedTime - lastSplitTime) / (double) bestSplitTotal));

					if (arc.getLength() < 360)
						arc.setStroke(Color.GREEN);
					else
						arc.setStroke(Color.ORANGE);
				}

				// New split
				if (requestedSplit)
				{
					long     currentSplitTime       = elapsedTime - previousSplit;
					String[] currentSplitTimeLabels = getTimes(currentSplitTime);
					previousSplit  = elapsedTime;
					requestedSplit = false;
					splits.add(currentSplitTime);

					// Add to history
					TextFlow splitFlow = new TextFlow();
					splitFlow.setTranslateX(300);
					Text split1 = new Text("Split " + splits.size() + "\t");
					split1.setStyle("-fx-font-size: 14px; -fx-fill: orange;");
					Text split2 = new Text(currentSplitTimeLabels[0] + ":" + currentSplitTimeLabels[1] + "." + currentSplitTimeLabels[2]);
					split2.setStyle("-fx-font-size: 14px; -fx-fill: white;");
					splitFlow.getChildren().addAll(split1, split2);

					// Enter animation
					TranslateTransition splitsTransition = new TranslateTransition();
					splitsTransition.setFromX(300);
					splitsTransition.setToX(0);
					splitsTransition.setDuration(Duration.millis(250));
					splitsTransition.setNode(splitFlow);
					splitsTransition.play();

					splitsColumn.getChildren().add(splitFlow);

					// Highlight best split
					int  bestIndex = 0;
					long best      = -1;
					for (var i = 0; i < splits.size(); i++)
					{
						if (splits.get(i) <= best || best == -1)
						{
							bestIndex = i;
							best      = splits.get(i);
						}

						// Reset color of all splits
						TextFlow resetFlow = (TextFlow) splitsColumn.getChildren().get(i);
						resetFlow.getChildren().get(1).setStyle("-fx-font-size: 14px; -fx-fill: white;");
					}

					TextFlow bestFlow = (TextFlow) splitsColumn.getChildren().get(bestIndex);
					bestFlow.getChildren().get(1).setStyle("-fx-font-size: 14px; -fx-fill: green;");

					// Scroll to bottom
					splitsScroll.setVvalue(1f);
				}

			}
		};


		// Connect buttons
		startStopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e)
			{
				if (e.getButton().equals(MouseButton.PRIMARY))
				{
					if (timerRunning)
						timer.stop();
					else
						timer.start();
				}

			}
		});
		splitResetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e)
			{
				if (e.getButton().equals(MouseButton.PRIMARY))
				{
					// Split
					if (timerRunning)
						requestedSplit = true;
					// Reset
					else
					{
						timeLabel.setText("00:00.000");
						timeLabel.setStyle("-fx-font-size: 42;");
						splits.clear();
						splitsColumn.getChildren().clear();
						splitResetButton.setText("Split");
						splitResetButton.setDisable(true);
						previousSplit = 0;
						startStopButton.setDisable(false);
						arc.setLength(0);
					}

				}

			}
		});


		// Add a backdrop
		StackPane mainStack    = new StackPane();
		ImageView backdropView = new ImageView(new Image(getClass().getResourceAsStream("mountain-climber.png")));
		backdropView.setFitWidth(300);
		backdropView.setPreserveRatio(true);
		backdropView.setStyle("-fx-opacity: .9;");
		mainStack.getChildren().add(backdropView);
		StackPane.setAlignment(backdropView, Pos.BOTTOM_RIGHT);
		mainStack.getChildren().add(mainColumn);


		// Let's fly
		Scene scene = new Scene(mainStack, 300, 480);
		stage.setTitle("Uppgift 3: Stoppuret");
		stage.setResizable(false);
		stage.setScene(scene);
		scene.getStylesheets().add(this.getClass().getResource("Theme.css").toExternalForm());
		stage.show();


		// Set arc center and radius now that we have drawn the nodes
		arc.setCenterX(timePane.getWidth() / 2);
		arc.setCenterY((timeLabel.getHeight() / 2));
		arc.setRadiusX(((timePane.getWidth() / 2) + 75) / 2);
		arc.setRadiusY(((timeLabel.getHeight() / 2) + 50) / 2);


		// Stop the startStopButton from resizing on text change
		startStopButton.setMinWidth(startStopButton.getWidth());
		startStopButton.setMaxWidth(startStopButton.getWidth());
		startStopButton.setPrefWidth(startStopButton.getWidth());
	}


	private String[] getTimes(long time)
	{
		String[] times = new String[3];

		times[0] = String.valueOf((time / 1000) / 60);
		times[1] = String.valueOf((time / 1000) % 60);
		times[2] = String.valueOf(time - ((((time / 1000) / 60) * 60000) + (((time / 1000) % 60) * 1000)));

		// Make sure we follow the xx:xx.xxx format
		times[0] = times[0].length() == 1 ? "0" + times[0] : times[0];
		times[1] = times[1].length() == 1 ? "0" + times[1] : times[1];
		times[2] = times[2].length() == 1 ? "00" + times[2] : times[2].length() == 2 ? "0" + times[2] : times[2];

		return times;
	}
}