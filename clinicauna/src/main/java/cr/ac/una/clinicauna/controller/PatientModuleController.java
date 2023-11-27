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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class PatientModuleController implements Initializable {

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
    @FXML
    private Button btnEdit;
    @FXML
    private HBox containerButtons;
    @FXML
    private HBox hboxCreatePatient;
    @FXML
    private Button btnGenerateReport;
    private PatientService patientService = new PatientService();
    private PatientDto patientBuffer;
    private List<PatientDto> patientDtos = new ArrayList<>();
    private Data data = Data.getInstance();
    private ReportService reportService = new ReportService();
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
            btnEdit.setDisable(true);
            initializeList();
            patientDtos = (List<PatientDto>) patientService.getPatients().getData();
            loadPatients(patientDtos);
            txfSearchPatient.setOnKeyPressed(e -> searchPatientAction(e));
            loadPrivileges();
            containerButtons.getChildren().remove(btnGenerateReport);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void btnNewPatientAction(ActionEvent event) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("PatientRegister");
        Animate.MakeDefaultFadeTransition(parent, loader.load());
        PatientRegisterController controller = loader.getController();
        if (controller != null) {
            controller.loadView("patientModule");
        }
    }

    @FXML
    private void btnViewPatientAction(ActionEvent event) throws IOException {
        data.setData("patientBuffer", patientBuffer);
        UserDto userLoggued = (UserDto) data.getData("userLoggued");
        if (userLoggued != null && userLoggued.getRole().equals("RECEPCIONIST")) {
            FXMLLoader loader = App.getFXMLLoader("PatientRegister");
            Animate.MakeDefaultFadeTransition(parent, loader.load());
            PatientRegisterController controller = loader.getController();
            if (controller != null) {
                controller.loadView("patientModule");
            }
            return;
        }
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("PatientHistory").load());
    }

    @FXML
    private void btnDeletePatientAction(ActionEvent event) {
        if (patientBuffer != null) {
            ResponseWrapper response = patientService.deletePatient(patientBuffer);
            if (response.getCode() == ResponseCode.OK) {
                tblPatientsView.getItems().remove(patientBuffer);
            } else {
                Message.showNotification("Error", MessageType.ERROR, response.getMessage());
            }
        }
    }

    @FXML
    private void btnGenerateReportAction(ActionEvent event) {
        if (patientBuffer != null) {
            if (patientBuffer.getPatientPersonalHistory() == null) {
                Message.showNotification("Ups", MessageType.INFO, "patientHistoryEmpty");
                return;
            }
            if (u.getLanguage().equals("ENGLISH")) {
                ResponseWrapper response = reportService.createMedicalExamReport(patientBuffer.getId(), "en");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                }
            } else {
                ResponseWrapper response = reportService.createMedicalExamReport(patientBuffer.getId(), "es");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                }
            }

        }
    }

    private void searchPatientAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchPatient.getText(), parameterKey = cbSearchParameter.getValue();
            if (key.isBlank() || parameterKey == null) {
                loadPatients(patientDtos);
                return;
            }
            loadPatients(filterPatients(patientDtos, parameterKey, key));
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
                    if (patientBuffer != null) {
                        btnEdit.setDisable(false);
                        return;
                    }
                    btnEdit.setDisable(true);

                });
    }

    private void loadPatients(List<PatientDto> patients) {
        tblPatientsView.setItems(FXCollections.observableArrayList(patients));
    }

    public void loadView(String option) {
        option = option.toLowerCase();
        if (option.equals("medicalexamreport")) {
            hboxCreatePatient.getChildren().clear();
            containerButtons.getChildren().clear();
            containerButtons.getChildren().add(btnGenerateReport);
        }
    }

    private void loadPrivileges() {
        UserDto userLogged = (UserDto) Data.getInstance().getData("userLoggued");
        if (userLogged != null) {
            if (userLogged.getRole().toLowerCase().equals("recepcionist")) {
                if (userLogged.getLanguage().equals("ENGLISH")) {
                    btnEdit.setText(data.getEnglishBundle().getString("edit"));
                } else {
                    btnEdit.setText(data.getSpanishBundle().getString("edit"));
                }
            }
        }
    }

}
