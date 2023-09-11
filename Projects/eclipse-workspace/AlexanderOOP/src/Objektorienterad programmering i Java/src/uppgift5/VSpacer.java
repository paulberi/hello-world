package uppgift5;

import javafx.scene.layout.Pane;

public class VSpacer extends Pane
{
	public VSpacer(int height)
	{
		setMinSize(1, height);
		setMaxSize(1, height);
		setPrefSize(1, height);
	}
}
