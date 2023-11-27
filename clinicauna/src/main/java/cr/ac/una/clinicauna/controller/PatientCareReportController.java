package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.PatientService;
import cr.ac.una.clinicauna.services.ReportService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientCareReportController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private JFXTextField txfSearchPatient;
    @FXML
    private ComboBox<String> cbSearchParameter;
    @FXML
    private TableView<PatientDto> tblPatientsView;
    @FXML
    private TableColumn<PatientDto, String> tcIdentification;
    @FXML
    private TableColumn<PatientDto, String> tcName;
    @FXML
    private TableColumn<PatientDto, String> tcLastName;
    @FXML
    private TableColumn<PatientDto, String> tcPhone;
    @FXML
    private TableColumn<PatientDto, String> tcRole;
    private PatientService patientService = new PatientService();
    private ReportService reportService = new ReportService();
    private PatientDto patientBuffer;
    private List<PatientDto> patientDtos = new ArrayList<>();
    private Data data = Data.getInstance();
    private UserDto u = new UserDto();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            u = (UserDto) data.getData("userLoggued");
            if (data.getLanguageOption().equals("en")) {
                cbSearchParameter.getItems().addAll("Name", "Last Name", "Phone", "Identification", "Birth Date");
            } else {
                cbSearchParameter.getItems().addAll("Nombre", "Apellido", "Telefono", "Cédula", "Fecha de Nacimiento");
            }
            initializeList();
            patientDtos = (List<PatientDto>) patientService.getPatients().getData();
            loadPatients(patientDtos);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void searchPatientKeyEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchPatient.getText(), parameterKey = cbSearchParameter.getValue();
            if (key.isBlank() || parameterKey == null) {
                loadPatients(patientDtos);
                return;
            }
            loadPatients(filterPatients(patientDtos, parameterKey, key));
        }
    }

    private void btnViewPatientCare(ActionEvent event) throws IOException {
        if (patientBuffer != null) {
            if (patientBuffer.getPatientPersonalHistory() == null) {
                Message.showNotification("Ups", MessageType.INFO, "patientHistoryEmpty");
                return;
            }
            data.setData("patientBuffer", patientBuffer);
            FXMLLoader loader = App.getFXMLLoader("PatientHistory");
            Animate.MakeDefaultFadeTransition(parent, loader.load());
            PatientHistoryController controller = loader.getController();
            if (controller != null) {
                controller.loadView("patientCareView", true);
            }
        }

    }

    @FXML
    private void btnGeneratePatientCareReport(ActionEvent event) {
        if (patientBuffer != null) {
            if (patientBuffer.getPatientPersonalHistory() == null) {
                Message.showNotification("Ups", MessageType.INFO, "patientHistoryEmpty");
                return;
            }
            if (u.getLanguage().equals("ENGLISH")) {
                ResponseWrapper response = reportService.createPatientReport(patientBuffer.getId(), "en");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                }
            } else {
                ResponseWrapper response = reportService.createPatientReport(patientBuffer.getId(), "es");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                }
            }
        }
    }

    private List<PatientDto> filterPatients(List<PatientDto> patients, String parameter, String key) {
        List<PatientDto> patientsFiltered = new ArrayList<>();
        if (patients != null) {
            parameter = parameter.toLowerCase();
            if (parameter.equals("name") || parameter.equals("nombre")) {
                patientsFiltered = patients
                        .stream()
                        .filter(user -> user.getName().toLowerCase().contains(key.toLowerCase()))
                        .collect(Collectors.toList());
            } else if (parameter.equals("last name") || parameter.equals("apellido")) {
                patientsFiltered = patients
                        .stream()
                        .filter(user -> user.getFirstLastname().toLowerCase().contains(key.toLowerCase()))
                        .collect(Collectors.toList());
            } else if (parameter.equals("phone") || parameter.equals("telefono")) {
                patientsFiltered = patients
                        .stream()
                        .filter(user -> user.getPhoneNumber().toLowerCase().contains(key.toLowerCase()))
                        .collect(Collectors.toList());
            } else if (parameter.equals("identification") || parameter.equals("cédula")) {
                patientsFiltered = patients
                        .stream()
                        .filter(user -> user.getIdentification().contains(key))
                        .collect(Collectors.toList());
            } else if (parameter.equals("birth date") || parameter.equals("fecha de nacimiento")) {
                patientsFiltered = patients
                        .stream()
                        .filter(user -> user.getBirthDate().toLowerCase().contains(key.toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return patientsFiltered;
    }

    private void initializeList() {
        tcIdentification.setCellValueFactory(new PropertyValueFactory<>("identification"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("firstLastname"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tblPatientsView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    patientBuffer = newValue;
                });
    }

    private void loadPatients(List<PatientDto> patients) {
        tblPatientsView.setItems(FXCollections.observableArrayList(patients));
    }

}
