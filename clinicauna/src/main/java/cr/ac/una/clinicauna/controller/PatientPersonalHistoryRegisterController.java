package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.services.PatientPersonalHistoryService;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientPersonalHistoryRegisterController implements Initializable {

    @FXML
    private VBox mainView;
    @FXML
    private ImageView imgPhotoProfile;
    @FXML
    private JFXTextArea txfPathological;
    @FXML
    private JFXTextArea txfHospitalizations;
    @FXML
    private JFXTextArea txfSurgical;
    @FXML
    private JFXTextArea txfAlergies;
    @FXML
    private JFXTextArea txfTreatments;
    private PatientPersonalHistoryDto patientPersonalHistoryDto;
    private PatientPersonalHistoryService PatientPersonalHistoryService = new PatientPersonalHistoryService();
    private PatientDto patientBuffer;
    private boolean isEditing = true;
    private Data data = Data.getInstance();
    @FXML
    private HBox parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        patientBuffer = (PatientDto) data.getData("patientBuffer");
        if (patientBuffer != null) {
            patientPersonalHistoryDto = (PatientPersonalHistoryDto) PatientPersonalHistoryService
                    .getPatientPersonalHistoryById(patientBuffer.getId()).getData();
        }
        if (patientPersonalHistoryDto == null) {
            patientPersonalHistoryDto = new PatientPersonalHistoryDto();

        }
        isEditing = patientPersonalHistoryDto.getId() != null;
        bindPersonalHistory();
    }

    @FXML
    private void backAction(MouseEvent event) {
        try {
            Animate.MakeDefaultFadeTransition(mainView, App.getFXMLLoader("PatientHistory").load());
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnRegisterPatientPersonalHistoryAction(ActionEvent event) {
        if (!verifyFields()) {
            Message.showNotification("Ups", MessageType.INFO, "fieldsEmpty");
            return;
        }
        patientPersonalHistoryDto.setPatient(new PatientDto(patientBuffer));
        ResponseWrapper response = isEditing
                ? PatientPersonalHistoryService.updatePatientPersonalHistory(patientPersonalHistoryDto)
                : PatientPersonalHistoryService.createPatientPersonalHistory(patientPersonalHistoryDto);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification(response.getCode().name(), MessageType.INFO, response.getMessage());
            backAction(null);
            return;
        }
        Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());

    }

    private void bindPersonalHistory() {
        txfAlergies.textProperty().bindBidirectional(patientPersonalHistoryDto.allergies);
        txfHospitalizations.textProperty().bindBidirectional(patientPersonalHistoryDto.hospitalizations);
        txfPathological.textProperty().bindBidirectional(patientPersonalHistoryDto.pathological);
        txfSurgical.textProperty().bindBidirectional(patientPersonalHistoryDto.surgical);
        txfTreatments.textProperty().bindBidirectional(patientPersonalHistoryDto.treatments);

    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfAlergies, txfHospitalizations, txfPathological, txfSurgical,
                txfTreatments);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            }
        }
        return true;
    }

}
