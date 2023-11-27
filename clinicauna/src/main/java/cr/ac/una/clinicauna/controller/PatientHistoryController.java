package cr.ac.una.clinicauna.controller;

import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.util.Data;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.animations.FlipInY;
import cr.ac.una.clinicauna.model.MedicalExamDto;
import cr.ac.una.clinicauna.model.PatientCareDto;
import cr.ac.una.clinicauna.model.PatientFamilyHistoryDto;
import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.services.PatientPersonalHistoryService;
import cr.ac.una.clinicauna.services.PatientService;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientHistoryController implements Initializable {

    @FXML
    private Label lblIdentification;
    @FXML
    private Label lblFullName;
    @FXML
    private Label lblBirthDate;
    @FXML
    private Label lblPhoneNumber;
    @FXML
    private Label lblGender;
    @FXML
    private LineChart<String, Number> chartMassIndex;
    @FXML
    private HBox parent;
    @FXML
    private VBox mainView;
    @FXML
    private Label lblAlergies;
    @FXML
    private Label lblTreatments;
    @FXML
    private Label lblPathological;
    @FXML
    private Label lblHospitalizations;
    @FXML
    private Label lblSurgical;
    @FXML
    private Accordion acPatientCares;
    @FXML
    private VBox familyHistoryView;
    @FXML
    private TableView<PatientFamilyHistoryDto> tblFamilyHistory;
    @FXML
    private TableColumn<PatientFamilyHistoryDto, String> tcDisease;
    @FXML
    private TableColumn<PatientFamilyHistoryDto, String> tcRelationship;
    @FXML
    private VBox personalHistoryView;
    @FXML
    private VBox patientCareView;
    @FXML
    private StackPane mainStack;
    @FXML
    private JFXTextField txfSearchByDate;
    @FXML
    private JFXTextField txfSearchMedicalExam;
    @FXML
    private Accordion acMedicalExams;
    private PatientDto patientBuffer;
    private PatientPersonalHistoryDto patientPersonalHistoryBuffer;
    private PatientService patientService = new PatientService();
    private PatientPersonalHistoryService patientPersonalHistoryService = new PatientPersonalHistoryService();
    private List<PatientCareDto> patientCareDtos = new ArrayList<>();
    private List<MedicalExamDto> medicalExamDtos = new ArrayList<>();

    private Data data = Data.getInstance();
    private boolean isFromReportView = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            patientBuffer = (PatientDto) data.getData("patientBuffer");
            patientBuffer = (PatientDto) patientService.getPatientById(patientBuffer.getId()).getData();
            patientPersonalHistoryBuffer = patientBuffer.getPatientPersonalHistory();
            if (patientPersonalHistoryBuffer != null) {
                patientPersonalHistoryBuffer = (PatientPersonalHistoryDto) patientPersonalHistoryService
                        .getPatientPersonalHistoryById(patientBuffer.getPatientPersonalHistory().getId()).getData();
            }
            data.setData("patientBuffer", patientBuffer);
            initializeList();
            initializeAccordion();
            loadChart();
            bindPatient();
            txfSearchByDate.setOnKeyPressed(t -> searchPatientCareAction(t));
            txfSearchMedicalExam.setOnKeyPressed(t -> searchMedicalExamAction(t));
        } catch (Exception e) {
            System.out.println(e.toString());
            backAction(null);
        }
    }

    @FXML
    private void backAction(MouseEvent event) {
        try {
            data.removeData("patientBuffer");
            FXMLLoader loader = App.getFXMLLoader("Main");
            Animate.MakeDefaultFadeTransition(mainStack, loader.load());
            MainController controller = loader.getController();
            if (controller != null) {
                if (isFromReportView) {
                    Data.getInstance().setData("option", "patientReport");
                    controller.loadView("reportModule");
                    return;
                }
                controller.loadView("patientModule");

            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void btnNewHistoryAction(ActionEvent event) throws IOException {
        data.setData("patientPersonalHistoryBuffer", patientBuffer.getPatientPersonalHistory());
        Animate.MakeDefaultFadeTransition(mainStack, App.getFXMLLoader("PatientCareRegister").load());
    }

    @FXML
    private void btnNewMedicalExamAction(ActionEvent event) throws IOException {
        data.setData("patientPersonalHistoryBuffer", patientBuffer.getPatientPersonalHistory());
        Animate.MakeDefaultFadeTransition(mainStack, App.getFXMLLoader("MedicalExamRegister").load());
    }

    @FXML
    private void editPatientAction(MouseEvent event) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("PatientRegister");
        Animate.MakeDefaultFadeTransition(mainStack, loader.load());
        PatientRegisterController controller = loader.getController();
        if (controller != null) {
            controller.loadView("patientHistory");
        }
    }

    @FXML
    private void editPersonalHistoryAction(MouseEvent event) throws IOException {
        Animate.MakeDefaultFadeTransition(mainStack, App.getFXMLLoader("PatientPersonalHistoryRegister").load());
    }

    @FXML
    private void showPersonalHistoryView(MouseEvent event) {
        new FlipInY(personalHistoryView).play();
        personalHistoryView.toFront();
    }

    @FXML
    private void editFamilyHistoryAction(MouseEvent event) throws IOException {
        Animate.MakeDefaultFadeTransition(mainStack, App.getFXMLLoader("PatientFamilyHistoryRegister").load());
    }

    @FXML
    private void showFamilyHistory(MouseEvent event) {
        new FlipInY(familyHistoryView).play();
        familyHistoryView.toFront();
    }

    @FXML
    private void showMainView(MouseEvent event) {
        new FlipInY(mainView).play();
        mainView.toFront();
    }

    @FXML
    private void showPatientCareView(MouseEvent event) {
        if (patientPersonalHistoryBuffer == null) {
            Message.showNotification("Ups", MessageType.INFO, "patientHistoryEmpty");
            return;
        }
        new FlipInY(patientCareView).play();
        patientCareView.toFront();
    }

    private void searchPatientCareAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchByDate.getText();
            if (key.isBlank()) {
                loadAccordionPatientCares(patientCareDtos);
                return;
            }
            loadAccordionPatientCares(patientCareDtos.stream().filter(t -> t.getPatientCareDate().contains(key))
                    .collect(Collectors.toList()));
        }
    }

    private void searchMedicalExamAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchMedicalExam.getText();
            if (key.isBlank()) {
                loadAccordionMedicalExam(medicalExamDtos);
                return;
            }
            loadAccordionMedicalExam(medicalExamDtos.stream().filter(t -> t.getMedicalExamDate().contains(key))
                    .collect(Collectors.toList()));
        }
    }

    private void initializeAccordion() {
        try {
            if (patientPersonalHistoryBuffer != null) {
                patientCareDtos = patientPersonalHistoryBuffer.getPatientCares();
                medicalExamDtos = patientPersonalHistoryBuffer.getMedicalExams();
                loadAccordionPatientCares(patientCareDtos);
                loadAccordionMedicalExam(medicalExamDtos);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void loadAccordionPatientCares(List<PatientCareDto> patientCareDtos) {
        try {
            acPatientCares.getPanes().clear();
            Collections.sort(patientCareDtos, (PatientCareDto o1, PatientCareDto o2) -> o1.getPatientCareDate()
                    .compareTo(o2.getPatientCareDate()));
            data.setData("patientHistoryController", this);
            for (PatientCareDto patientCareDto : patientCareDtos) {
                FXMLLoader loader = App.getFXMLLoader("PatientCareTitledPane");
                acPatientCares.getPanes().add(new TitledPane(patientCareDto.getPatientCareDate(), loader.load()));
                PatientCareTitledPaneController controller = loader.getController();
                controller.setData(patientCareDto, patientPersonalHistoryBuffer);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void loadAccordionMedicalExam(List<MedicalExamDto> medicalExamDtos) {
        try {
            acMedicalExams.getPanes().clear();
            Collections.sort(medicalExamDtos, (MedicalExamDto o1, MedicalExamDto o2) -> o1.getMedicalExamDate()
                    .compareTo(o2.getMedicalExamDate()));
            for (MedicalExamDto medicalExamDto : medicalExamDtos) {
                FXMLLoader loader = App.getFXMLLoader("MedicalExamTitledPane");
                acMedicalExams.getPanes().add(new TitledPane(medicalExamDto.getMedicalExamDate() + ": " + medicalExamDto.getName(), loader.load()));
                MedicalExamTitledPaneController controller = loader.getController();
                controller.setData(medicalExamDto, patientPersonalHistoryBuffer);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void loadChart() {
        if (patientPersonalHistoryBuffer != null) {
            chartMassIndex.getXAxis().setLabel("Dates");
            chartMassIndex.getYAxis().setLabel("IMC");
            XYChart.Series<String, Number> imc = new XYChart.Series<>();
            XYChart.Series<String, Number> imcIdeal = new XYChart.Series<>();
            imc.setName("IMC");
            imcIdeal.setName("IMC Ideal");

            for (PatientCareDto patientCareDto : patientPersonalHistoryBuffer.getPatientCares()) {
                imc.getData().add(new XYChart.Data<>(patientCareDto.getPatientCareDate(),
                        Double.valueOf(patientCareDto.getBodyMassIndex())));
                imcIdeal.getData().add(new XYChart.Data<>(patientCareDto.getPatientCareDate(), Double.valueOf(patientCareDto.getBodyMassIndexIdeal())));
            }

            chartMassIndex.getData().add(imc);
            chartMassIndex.getData().add(imcIdeal);

        }
    }

    private void initializeList() {
        tcDisease.setCellValueFactory(new PropertyValueFactory<>("disease"));
        tcRelationship.setCellValueFactory(new PropertyValueFactory<>("relationship"));
    }

    private void bindPatient() {
        lblFullName.setText(patientBuffer.getName() + " " + patientBuffer.getFirstLastname() + " "
                + patientBuffer.getSecondLastname());
        lblGender.textProperty().bindBidirectional(patientBuffer.gender);
        lblIdentification.textProperty().bindBidirectional(patientBuffer.identification);
        lblPhoneNumber.textProperty().bindBidirectional(patientBuffer.phoneNumber);
        lblBirthDate.setText(patientBuffer.getBirthDate());
        lblGender.textProperty().bindBidirectional(patientBuffer.gender);
        PatientPersonalHistoryDto patientPersonalHistoryDto = patientBuffer.getPatientPersonalHistory();
        if (patientPersonalHistoryDto != null) {
            lblAlergies.textProperty().bindBidirectional(patientPersonalHistoryDto.allergies);
            lblHospitalizations.textProperty().bindBidirectional(patientPersonalHistoryDto.hospitalizations);
            lblPathological.textProperty().bindBidirectional(patientPersonalHistoryDto.pathological);
            lblTreatments.textProperty().bindBidirectional(patientPersonalHistoryDto.treatments);
            lblSurgical.textProperty().bindBidirectional(patientPersonalHistoryDto.surgical);
        }
        List<PatientFamilyHistoryDto> patientFamilyHistoryDtos = patientBuffer.getPatientFamilyHistories();
        tblFamilyHistory.setItems(FXCollections.observableArrayList(patientFamilyHistoryDtos));
    }

    /**
     * *
     *
     * @param option MainView, PatientCareView
     * @param isFromReportView
     */
    public void loadView(String option, boolean isFromReportView) {
        if (option.toLowerCase().equals("mainview")) {
            mainView.toFront();
        }
        if (option.toLowerCase().equals("patientcareview")) {
            patientCareView.toFront();
        }
        this.isFromReportView = isFromReportView;
    }

    public void deletePatientCare(Long id) {
        patientCareDtos.removeIf(t -> Objects.equals(t.getId(), id));
        loadAccordionPatientCares(patientCareDtos);
    }

}
