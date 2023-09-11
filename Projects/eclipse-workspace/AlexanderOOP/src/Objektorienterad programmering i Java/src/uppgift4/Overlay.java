package uppgift4;

import javafx.animation.FadeTransition;
import javafx.event.EventType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Overlay extends Rectangle
{
	private FadeTransition fadeTransition;

	EventType<SignalEvent> closePopup = new EventType<SignalEvent>("CLOSE_POPUP");

	public Overlay()
	{
		setOpacity(0);
		setFill(Color.BLACK);
		setMouseTransparent(true);

		fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(200));
		fadeTransition.setNode(this);

		setOnMousePressed(event ->
		{
			fireEvent(new SignalEvent(closePopup));
		});
	}

	public void fadeOut()
	{
		fadeTransition.setToValue(.75);
		fadeTransition.play();
		setMouseTransparent(false);
	}

	public void fadeIn()
	{
		fadeTransition.setToValue(0);
		fadeTransition.play();
		setMouseTransparent(true);
	}
}
