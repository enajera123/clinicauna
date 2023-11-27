package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.model.PatientCareDto;
import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.services.PatientCareService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientCareTitledPaneController implements Initializable {

    @FXML
    private VBox mainView;
    @FXML
    private TextField txfBloodPressure;
    @FXML
    private TextField txfHeartRate;
    @FXML
    private TextField txfTemperature;
    @FXML
    private TextField txfHeight;
    @FXML
    private TextField txfWeight;
    @FXML
    private JFXTextArea txfCarePlan;
    @FXML
    private JFXTextArea txfObservations;
    @FXML
    private JFXTextArea txfPhysicalExam;
    @FXML
    private JFXTextArea txfTreatment;
    @FXML
    private Label lblIMC;
    private PatientHistoryController patientHistoryController;
    private PatientCareService patientCareService = new PatientCareService();
    private PatientCareDto patientCareBuffer;
    private PatientPersonalHistoryDto patientPersonalHistoryBuffer;
    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        patientHistoryController = (PatientHistoryController) data.getData("patientHistoryController");
    }

    @FXML
    private void btnEditPatientCareAction(ActionEvent event) throws IOException {
        data.setData("patientPersonalHistoryBuffer", patientPersonalHistoryBuffer);
        data.setData("patientCareBuffer", patientCareBuffer);
        App.setRoot("PatientCareRegister");
    }

    public void setData(PatientCareDto patientCareDto, PatientPersonalHistoryDto personalHistoryDto) {
        patientCareBuffer = patientCareDto;
        patientPersonalHistoryBuffer = personalHistoryDto;
        bindPatientCare();
    }

    public void bindPatientCare() {
        if (patientCareBuffer != null) {
            txfBloodPressure.textProperty().bindBidirectional(patientCareBuffer.bloodPressure);
            txfHeartRate.textProperty().bindBidirectional(patientCareBuffer.heartRate);
            txfHeight.textProperty().bindBidirectional(patientCareBuffer.height);
            txfTemperature.textProperty().bindBidirectional(patientCareBuffer.temperature);
            txfWeight.textProperty().bindBidirectional(patientCareBuffer.weight);
            txfObservations.textProperty().bindBidirectional(patientCareBuffer.observations);
            txfPhysicalExam.textProperty().bindBidirectional(patientCareBuffer.physicalExam);
            txfCarePlan.textProperty().bindBidirectional(patientCareBuffer.carePlan);
            txfTreatment.textProperty().bindBidirectional(patientCareBuffer.treatment);
            lblIMC.setText(patientCareBuffer.getBodyMassIndex());
        }
    }

    @FXML
    private void btnDeleteMedicalAppointmentAction(ActionEvent event) {
        if (patientCareBuffer.getId() != null) {
            ResponseWrapper response = patientCareService.deletePatientCare(patientCareBuffer.getId());
            if (response.getCode() != ResponseCode.OK) {
                Message.showNotification("ERROR", MessageType.ERROR, response.getMessage());
                return;
            }
            patientHistoryController.deletePatientCare(patientCareBuffer.getId());
        }
    }

}
