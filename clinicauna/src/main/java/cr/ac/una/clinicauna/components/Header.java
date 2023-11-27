package cr.ac.una.clinicauna.components;

import java.time.LocalDate;
import javafx.scene.layout.HBox;

/**
 *
 * @author estebannajera
 */
public class Header extends HBox {

    private String message;
    private LocalDate day;

    public Header(String message, LocalDate day) {
        this.message = message;
        this.day = day;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
