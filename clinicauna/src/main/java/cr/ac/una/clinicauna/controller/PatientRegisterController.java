package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.services.PatientService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientRegisterController implements Initializable {

    @FXML
    private HBox parent;
    @FXML
    private VBox mainView;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private JFXTextField txfIdentification;
    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfLastName;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private JFXTextField txfSecondLastName;
    @FXML
    private JFXTextField txfEmail;
    @FXML
    private JFXTextField txfPhoneNumber;
    private PatientDto patientBuffer = new PatientDto();
    private PatientService patientService = new PatientService();
    private String option = "";
    boolean isEditing = false;

    private Data data = Data.getInstance();
    @FXML
    private RadioButton rbEnglish;
    @FXML
    private ToggleGroup groupLanguage;
    @FXML
    private RadioButton rbSpanish;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbGender.getItems().addAll("MALE", "FEMALE");
        PatientDto patientDto = (PatientDto) data.getData("patientBuffer");
        if (patientDto != null) {
            patientBuffer = patientDto;
            isEditing = true;
        }
        bindPatient();
    }

    @FXML
    private void backFromRegister(MouseEvent event) {
        try {
            option = option.toLowerCase();

            FXMLLoader loader;
            switch (option) {
                case "medicalappointmentregister":
                    loader = App.getFXMLLoader("MedicalAppointmentRegister");
                    Animate.MakeDefaultFadeTransition(mainView, loader.load());
                    MedicalAppointmentRegisterController medicalAppointmentRegisterController = loader.getController();
                    if (medicalAppointmentRegisterController != null) {
                        medicalAppointmentRegisterController.loadView(patientBuffer, "patientRegister");
                    }
                    data.removeData("patientBuffer");
                    break;
                case "patienthistory":
                    Animate.MakeDefaultFadeTransition(mainView, App.getFXMLLoader("PatientHistory").load());
                    break;
                case "patientmodule":
                    loader = App.getFXMLLoader("Main");
                    Animate.MakeDefaultFadeTransition(mainView, loader.load());
                    MainController mainController = loader.getController();
                    if (mainController != null) {
                        mainController.loadView("PatientModule");
                    }
                    data.removeData("patientBuffer");
                    break;
                default:
                    Animate.MakeDefaultFadeTransition(mainView, App.getFXMLLoader("Main").load());
                    data.removeData("patientBuffer");
                    break;
            }
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnRegisterAction(ActionEvent event) throws IOException {
        if (!verifyFields()) {
            Message.showNotification("Ups", MessageType.INFO, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                ResponseWrapper response = !isEditing ? patientService.createPatient(patientBuffer)
                        : patientService.updatePatient(patientBuffer);
                Message.showNotification(response.getCode().name(), MessageType.INFO, response.getMessage());
                if (response.getCode() == ResponseCode.OK) {
                    patientBuffer = (PatientDto) response.getData();
                    backFromRegister(null);
                }
            });
        }).start();
    }

    private void bindPatient() {
        txfEmail.textProperty().bindBidirectional(patientBuffer.email);
        txfName.textProperty().bindBidirectional(patientBuffer.name);
        txfLastName.textProperty().bindBidirectional(patientBuffer.firstLastname);
        txfIdentification.textProperty().bindBidirectional(patientBuffer.identification);
        txfPhoneNumber.textProperty().bindBidirectional(patientBuffer.phoneNumber);
        txfSecondLastName.textProperty().bindBidirectional(patientBuffer.secondLastname);
        dpBirthDate.valueProperty().bindBidirectional(patientBuffer.birthDate);
        cbGender.valueProperty().bindBidirectional(patientBuffer.gender);
        groupLanguage.selectedToggleProperty()
                .addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                    if (newValue != null && patientBuffer != null) {
                        if (rbEnglish.isSelected()) {
                            patientBuffer.setLanguage("ENGLISH");
                        } else {
                            patientBuffer.setLanguage("SPANISH");
                        }

                    }
                });
        if (isEditing) {
            if (patientBuffer.getLanguage().toLowerCase().equals("english")) {
                rbEnglish.setSelected(true);
            } else if (patientBuffer.getLanguage().toLowerCase().equals("spanish")) {
                rbSpanish.setSelected(true);
            }
        }
    }

    public void loadView(String option) {
        this.option = option;
    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfEmail, txfIdentification, txfLastName, txfName, txfSecondLastName,
                txfPhoneNumber);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            }
        }
        return groupLanguage.getSelectedToggle() != null;
    }

}
