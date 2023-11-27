package cr.ac.una.clinicauna;

import cr.ac.una.clinicauna.util.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.image.Image;

/**
 * JavaFX App
 *
 * @author arayaroma
 */
public class App extends Application {

    private static Scene scene;
    public static final String DOMAIN_PATH = "/cr/ac/una/clinicauna/";

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = getFXMLLoader("Login").load();
        scene = new Scene(root);
        stage.setTitle("ClinicaUNA");
        stage.getIcons().add(new Image(App.class.getResource("/cr/ac/una/clinicauna/img/estetoscopio.png").toString()));
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(getFXMLLoader(fxml).load());
    }

    public static void setRoot(Parent parent) throws IOException {
        scene.setRoot(parent);
    }

    public static FXMLLoader getFXMLLoader(String fxml) {
        if (Data.languageOption.equals("en")) {
            return new FXMLLoader(App.class.getResource(DOMAIN_PATH + "view/" + fxml + ".fxml"),
                    Data.getInstance().getEnglishBundle());
        }
        return new FXMLLoader(App.class.getResource(DOMAIN_PATH + "view/" + fxml + ".fxml"),
                Data.getInstance().getSpanishBundle());
    }

    public static void main(String[] args) {
        launch();
    }

}
