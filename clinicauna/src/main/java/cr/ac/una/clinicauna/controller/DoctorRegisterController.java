package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.DoctorService;
import cr.ac.una.clinicauna.services.UserService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class DoctorRegisterController implements Initializable {

    @FXML
    private HBox parent;
    @FXML
    private VBox mainView;
    @FXML
    private ImageView imgPhotoProfile;
    @FXML
    private JFXTextField txfCode;
    @FXML
    private JFXTextField txfCarne;
    @FXML
    private ComboBox<String> cbState;
    @FXML
    private JFXTextField txfHourlySlots;
    @FXML
    private Spinner<Integer> spStartingHours;
    @FXML
    private Spinner<Integer> spStartingMinutes;
    @FXML
    private Spinner<Integer> spEndingHours;
    @FXML
    private Spinner<Integer> spEndingMinutes;

    private UserDto userBuffer;

    private boolean isEditing = false;
    private DoctorDto doctorBuffer = new DoctorDto();
    private DoctorService doctorService = new DoctorService();
    private UserService userService = new UserService();

    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            userBuffer = (UserDto) data.getData("userBuffer");
            if (userBuffer != null) {
                DoctorDto doctorEncountered = (DoctorDto) doctorService.getDoctorById(userBuffer.getId()).getData();
                if (doctorEncountered == null) {
                    doctorBuffer = new DoctorDto();
                    isEditing = false;
                } else {
                    doctorBuffer = doctorEncountered;
                    isEditing = true;
                }
            }
            initializeSpinners();
            validNumbersInTextField(txfHourlySlots);
            validNumbersInTextField(txfCarne);
            validNumbersInTextField(txfCode);
            bindDoctor();

        } catch (Exception e) {
            System.out.println(e.toString());
            backFromRegister(null);
        }

    }

    @FXML
    private void backFromRegister(MouseEvent event) {
        try {
            Animate.MakeDefaultFadeTransition(mainView, App.getFXMLLoader("UserRegister").load());
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnRegisterDoctorAction(ActionEvent event) throws IOException {
        String startingTime = parseTimeToString(spStartingHours, spStartingMinutes);
        String endingTime = parseTimeToString(spEndingHours, spEndingMinutes);
        if (startingTime.isBlank() || endingTime.isBlank() || !verifyFields()) {
            Message.showNotification("Ups", MessageType.ERROR, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    doctorBuffer.setShiftStartTime(startingTime);
                    doctorBuffer.setShiftEndTime(endingTime);
                    if (saveUser(userBuffer)) {
                        doctorBuffer.setUser(new UserDto(userBuffer));
                        ResponseWrapper response = !isEditing ? doctorService.createDoctor(doctorBuffer)
                                : doctorService.updateDoctor(doctorBuffer);
                        if (response.getCode() != ResponseCode.OK) {
                            Message.showNotification("Error", MessageType.ERROR, response.getMessage());
                            return;
                        }
                        Message.showNotification("Success", MessageType.INFO, "doctorRegisteredSuccess");
                        updateUserLoggued();
                        Animate.MakeDefaultFadeTransition(mainView, App.getFXMLLoader("Main").load());
                        data.removeData("userBuffer");
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });
        }).start();
    }

    private void updateUserLoggued() {
        if (Objects.equals(userBuffer.getId(), ((UserDto) data.getData("userLoggued")).getId())) {
            data.removeData("userLoggued");
            userBuffer = (UserDto) userService.findUserById(userBuffer.getId()).getData();
            data.setData("userLoggued", userBuffer);
        }
    }

    private String parseTimeToString(Spinner spHour, Spinner spMinutes) {
        String time = spHour.getEditor().getText() + ":" + spMinutes.getEditor().getText();
        if (verifyTime(time)) {
            return time;
        }
        Message.showNotification("Invalid format", MessageType.ERROR, "timeInvalid");
        return "";
    }

    private boolean verifyTime(String time) {
        String timeRegex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        return Pattern.matches(timeRegex, time);
    }

    private void bindDoctor() {
        txfCode.textProperty().bindBidirectional(doctorBuffer.code);
        txfCarne.textProperty().bindBidirectional(doctorBuffer.idCard);
        txfHourlySlots.textProperty().bindBidirectional(doctorBuffer.hourlySlots);
        if (doctorBuffer.getShiftStartTime() != null) {
            String[] startingHour = doctorBuffer.getShiftStartTime().split(":");
            if (startingHour.length == 2) {
                spStartingHours.getEditor().setText(startingHour[0]);
                spStartingMinutes.getEditor().setText(startingHour[1]);
            }
        }
        if (doctorBuffer.getShiftEndTime() != null) {
            String[] endingHour = doctorBuffer.getShiftEndTime().split(":");
            if (endingHour.length == 2) {
                spEndingHours.getEditor().setText(endingHour[0]);
                spEndingMinutes.getEditor().setText(endingHour[1]);
            }
        }
    }

//    private void unbindDoctor() {
//        txfCode.textProperty().unbindBidirectional(doctorBuffer.code);
//        txfCarne.textProperty().unbindBidirectional(doctorBuffer.idCard);
//        txfHourlySlots.textProperty().unbindBidirectional(doctorBuffer.hourlySlots);
//        spStartingHours.getEditor().setText("");
//        spStartingMinutes.getEditor().setText("");
//        spEndingMinutes.getEditor().setText("");
//        spEndingHours.getEditor().setText("");
//    }
    private void initializeSpinners() {
        spStartingHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 00));
        spEndingHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 00));
        spStartingMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 00));
        spEndingMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 00));
        StringConverter<Integer> formatter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String text) {
                try {
                    return Integer.valueOf(text);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        };
        spStartingHours.getValueFactory().setConverter(formatter);
        spStartingMinutes.getValueFactory().setConverter(formatter);
        spEndingHours.getValueFactory().setConverter(formatter);
        spEndingMinutes.getValueFactory().setConverter(formatter);
    }

    private boolean verifyFields() {

        List<Node> fields = Arrays.asList(txfCarne, txfCode, txfHourlySlots);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private void validNumbersInTextField(JFXTextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> textFormatter = new TextFormatter<>(
                new IntegerStringConverter(),
                null,
                filter);
        StringConverter<Integer> converter = new IntegerStringConverter() {
            @Override
            public String toString(Integer value) {
                return value != null ? super.toString(value) : "0";
            }
        };

        textField.setTextFormatter(textFormatter);
    }

    public boolean saveUser(UserDto user) throws IOException {
        ResponseWrapper response = user.getId() == null ? userService.createUser(user)
                : userService.updateUser(user);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification("Success", MessageType.CONFIRMATION, response.getMessage());
            user = (UserDto) response.getData();
            if (user != null) {
                userBuffer = user;
            }
            return true;
        }
        Message.showNotification("Ups", MessageType.ERROR, response.getMessage());
        System.out.println(response.getMessage());
        return false;
    }
}
