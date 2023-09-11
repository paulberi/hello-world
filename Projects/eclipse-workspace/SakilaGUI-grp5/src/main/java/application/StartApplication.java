package application;

import application.core.DaoFactory;
import application.core.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) {
        ViewHandler vh = new ViewHandler(stage);
        vh.openDashboard();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        // Close the session at program end
        DaoFactory.getInstance().endSession();
        System.out.println("Session closed");
    }
}