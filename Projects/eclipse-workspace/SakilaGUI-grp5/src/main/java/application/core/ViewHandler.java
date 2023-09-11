package application.core;

import java.io.IOException;

import application.controller.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewHandler {
	
	private Stage stage;
	
	public ViewHandler(Stage stage) {
		this.stage = stage;
	}
	
	public void openDashboard() {
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
			DashboardController d = loader.getController();
			d.init();
			stage.setScene(scene);
			stage.setTitle("Sakila");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
