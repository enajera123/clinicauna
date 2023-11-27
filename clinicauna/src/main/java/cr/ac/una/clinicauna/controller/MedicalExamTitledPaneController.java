package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.model.MedicalExamDto;
import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.util.Data;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class MedicalExamTitledPaneController implements Initializable {

    @FXML
    private VBox mainView;
    @FXML
    private TextField txfExamName;
    @FXML
    private JFXTextArea txfNotes;

    private MedicalExamDto medicalExamBuffer;
    private PatientPersonalHistoryDto patientPersonalHistoryDto;

    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setData(MedicalExamDto medicalExamDto, PatientPersonalHistoryDto personalHistoryDto) {
        medicalExamBuffer = medicalExamDto;
        patientPersonalHistoryDto = personalHistoryDto;
        bindMedicalExam();
    }

    public void bindMedicalExam() {
        if (medicalExamBuffer != null) {
            txfExamName.textProperty().bindBidirectional(medicalExamBuffer.name);
            txfNotes.textProperty().bindBidirectional(medicalExamBuffer.notes);
        }
    }

    @FXML
    private void btnEditMedicalExamAction(ActionEvent event) throws IOException {
        data.setData("patientPersonalHistoryBuffer", patientPersonalHistoryDto);
        data.setData("medicalExamBuffer", medicalExamBuffer);
        App.setRoot("MedicalExamRegister");
    }

}
