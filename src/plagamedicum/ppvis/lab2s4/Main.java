package plagamedicum.ppvis.lab2s4;

import javafx.application.Application;
import javafx.stage.Stage;
import plagamedicum.ppvis.lab2s4.Controller.Controller;
import plagamedicum.ppvis.lab2s4.model.Pet;
import plagamedicum.ppvis.lab2s4.view.View;

public class Main extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage mainStage) {
		Pet model = new Pet();
		Controller controller = new Controller(model);
		View view = new View(controller);
		
		mainStage = view.getStage();
		mainStage.show();
	}
}
