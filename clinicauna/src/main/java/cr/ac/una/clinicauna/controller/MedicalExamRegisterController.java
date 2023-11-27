package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.MedicalExamDto;
import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.services.MedicalExamService;
import cr.ac.una.clinicauna.services.PatientPersonalHistoryService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class MedicalExamRegisterController implements Initializable {

    @FXML
    private VBox mainView;
    @FXML
    private JFXTextField txfExamName;
    @FXML
    private JFXTextArea txfNotes;

    private MedicalExamDto medicalExamBuffer;
    private Data data = Data.getInstance();
    private MedicalExamService medicalExamService = new MedicalExamService();
    private PatientPersonalHistoryDto patientPersonalHistoryDto;
    private PatientPersonalHistoryService personalHistoryService = new PatientPersonalHistoryService();
    private boolean isEditing;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        medicalExamBuffer = (MedicalExamDto) data.getData("medicalExamBuffer");
        patientPersonalHistoryDto = (PatientPersonalHistoryDto) data.getData("patientPersonalHistoryBuffer");
        if (patientPersonalHistoryDto != null) {
            patientPersonalHistoryDto = (PatientPersonalHistoryDto) personalHistoryService
                    .getPatientPersonalHistoryById(patientPersonalHistoryDto.getId()).getData();
        }
        medicalExamBuffer = medicalExamBuffer == null ? new MedicalExamDto() : medicalExamBuffer;
        isEditing = medicalExamBuffer.getId() != null;
        bindMedicalExam();

    }

    @FXML
    private void backFromRegister(MouseEvent event) {
        try {
            data.removeData("medicalExamBuffer");
            FXMLLoader loader = App.getFXMLLoader("PatientHistory");
            Animate.MakeDefaultFadeTransition(mainView, loader.load());
            PatientHistoryController controller = loader.getController();
            if (controller != null) {
                controller.loadView("patientCareView", false);
            }
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnSaveMedicalExamAction(ActionEvent event) {
        if (!verifyFields()) {
            Message.showNotification("Ups", MessageType.INFO, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    if (medicalExamBuffer.getMedicalExamDate() == null) {
                        medicalExamBuffer.setMedicalExamDate(LocalDate.now().toString());
                    }
                    medicalExamBuffer.setPatientHistory(patientPersonalHistoryDto);
                    ResponseWrapper response = isEditing ? medicalExamService.updateMedicalExam(medicalExamBuffer) : medicalExamService.createMedicalExam(medicalExamBuffer);
                    if (response.getCode() == ResponseCode.OK) {
                        backFromRegister(null);
                        Message.showNotification(response.getCode().name(), MessageType.INFO, response.getMessage());
                        return;
                    }
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });
        }).start();
    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfExamName, txfNotes);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            } else if (i instanceof JFXTextArea && ((JFXTextArea) i).getText() != null
                    && ((JFXTextArea) i).getText().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private void bindMedicalExam() {
        txfExamName.textProperty().bindBidirectional(medicalExamBuffer.name);
        txfNotes.textProperty().bindBidirectional(medicalExamBuffer.notes);
    }

}
