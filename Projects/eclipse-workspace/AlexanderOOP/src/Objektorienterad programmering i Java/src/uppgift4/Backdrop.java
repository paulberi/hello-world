package uppgift4;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Backdrop extends ImageView
{

	public Backdrop()
	{
		super();
		
		setImage(new Image(getClass().getResourceAsStream("backdrop.png")));
		
		setFitWidth(300);
		setPreserveRatio(true);
		setStyle("-fx-opacity: .9;");
	}
}
