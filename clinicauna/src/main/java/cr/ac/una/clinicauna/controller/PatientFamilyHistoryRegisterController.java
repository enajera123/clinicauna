package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.PatientFamilyHistoryDto;
import cr.ac.una.clinicauna.services.PatientFamilyHistoryService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientFamilyHistoryRegisterController implements Initializable {

    @FXML
    private HBox parent;
    @FXML
    private HBox familyHistoryView;
    @FXML
    private JFXTextField txfDisease;
    @FXML
    private JFXTextField txfRelationship;
    @FXML
    private TableView<PatientFamilyHistoryDto> tblFamilyHistory;
    @FXML
    private TableColumn<PatientFamilyHistoryDto, String> tcDisease;
    @FXML
    private TableColumn<PatientFamilyHistoryDto, String> tcRelationship;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAdd;
    private PatientDto patientBuffer;
    private List<PatientFamilyHistoryDto> patientFamilyHistoryDtos = new ArrayList<>();
    private PatientFamilyHistoryDto patientFamilyHistoryBuffer;
    private PatientFamilyHistoryService patientFamilyHistoryService = new PatientFamilyHistoryService();
    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        patientBuffer = (PatientDto) data.getData("patientBuffer");
        if (patientBuffer == null) {
            patientBuffer = new PatientDto();
        }
        patientFamilyHistoryDtos = FXCollections.observableArrayList(patientBuffer.getPatientFamilyHistories());
        initializeList();
        loadList();
        btnEdit.setDisable(true);
    }

    @FXML
    private void btnBackAction(MouseEvent event) {
        try {
            Animate.MakeDefaultFadeTransition(familyHistoryView, App.getFXMLLoader("PatientHistory").load());
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnDeleteHistory(ActionEvent event) {
        if (patientFamilyHistoryBuffer != null) {
            ResponseWrapper response = patientFamilyHistoryService
                    .deletePatientFamilyHistory(patientFamilyHistoryBuffer);
            if (response.getCode() != ResponseCode.OK) {
                Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                return;
            }
            patientFamilyHistoryDtos.remove(patientFamilyHistoryBuffer);
            tblFamilyHistory.getItems().remove(patientFamilyHistoryBuffer);
        }
    }

    @FXML
    private void btnSaveHistories(ActionEvent event) {
        for (PatientFamilyHistoryDto patientFamilyHistoryDto : patientFamilyHistoryDtos) {
            patientFamilyHistoryDto.setPatient(new PatientDto(patientBuffer));
            ResponseWrapper response = patientFamilyHistoryDto.getId() == null
                    ? patientFamilyHistoryService.createPatientFamilyHistory(patientFamilyHistoryDto)
                    : patientFamilyHistoryService.updatePatientFamilyHistory(patientFamilyHistoryDto);
            if (response.getCode() != ResponseCode.OK) {
                Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                return;
            }
        }
        btnBackAction(null);
    }

    @FXML
    private void btnAddHistory(ActionEvent event) {
        if (!verifyFields()) {
            return;
        }
        String disease = txfDisease.getText(), relationship = txfRelationship.getText();
        PatientFamilyHistoryDto patientFamilyHistoryDto = new PatientFamilyHistoryDto();
        patientFamilyHistoryDto.setDisease(disease);
        patientFamilyHistoryDto.setRelationship(relationship);
        patientFamilyHistoryDtos.add(patientFamilyHistoryDto);
        cleanFields();
    }

    @FXML
    private void btnEditHistory(ActionEvent event) {
        if (!verifyFields()) {
            return;
        }
        String disease = txfDisease.getText(), relationship = txfRelationship.getText();
        patientFamilyHistoryBuffer.setDisease(disease);
        patientFamilyHistoryBuffer.setRelationship(relationship);
        cleanFields();

    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfDisease, txfRelationship);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private void initializeList() {
        tcDisease.setCellValueFactory(new PropertyValueFactory<>("disease"));
        tcRelationship.setCellValueFactory(new PropertyValueFactory<>("relationship"));
        tblFamilyHistory.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    patientFamilyHistoryBuffer = newValue;
                    if (patientFamilyHistoryBuffer != null) {
                        txfDisease.setText(patientFamilyHistoryBuffer.getDisease());
                        txfRelationship.setText(patientFamilyHistoryBuffer.getRelationship());
                        btnAdd.setDisable(true);
                        btnEdit.setDisable(false);
                    }
                });
    }

    private void loadList() {
        tblFamilyHistory.setItems(FXCollections.observableArrayList(patientFamilyHistoryDtos));
    }

    private void cleanFields() {
        txfDisease.setText("");
        txfRelationship.setText("");
        tblFamilyHistory.getItems().clear();
        loadList();
        btnAdd.setDisable(false);
        btnEdit.setDisable(true);
    }

}
