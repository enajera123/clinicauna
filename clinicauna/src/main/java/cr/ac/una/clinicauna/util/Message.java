package cr.ac.una.clinicauna.util;

import java.util.MissingResourceException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author estebannajera
 */
public class Message {

    public static void showNotification(String header, MessageType type, String property) {
        String content = "";
        try {
            content = Data.languageOption.equals("en") ? Data.englishBundle.getString(property) : Data.spanishBundle.getString(property);
        } catch (MissingResourceException e) {
            content = property;
        }
        Notifications notification = Notifications.create()
                .title(header)
                .text(content)
                .graphic(null)
                .hideAfter(Duration.seconds(7))
                .darkStyle()
                .position(Pos.TOP_RIGHT);
        switch (type) {
            case ERROR:
                notification.showError();
                break;
            case INFO:
                notification.show();
                break;
            case CONFIRMATION:
                notification.showConfirm();
                break;
            default:
                throw new AssertionError(type.name());
        }
    }

    public static ButtonType showConfirmationAlert(String title, AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result;
    }
}
