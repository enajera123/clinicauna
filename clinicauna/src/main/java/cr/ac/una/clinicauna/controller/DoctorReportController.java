package cr.ac.una.clinicauna.controller;

import cr.ac.una.clinicauna.model.AgendaDto;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.MedicalAppointmentDto;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.AgendaService;
import cr.ac.una.clinicauna.services.DoctorService;
import cr.ac.una.clinicauna.services.ReportService;
import cr.ac.una.clinicauna.services.UserService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class DoctorReportController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private ComboBox<UserDto> cbSearchParameter;
    @FXML
    private DatePicker dpStartingDate;
    @FXML
    private DatePicker dpEndingDate;
    @FXML
    private TableView<MedicalAppointmentDto> tblMedicalAppointmentsView;
    @FXML
    private TableColumn<MedicalAppointmentDto, String> tcPatient;
    @FXML
    private TableColumn<MedicalAppointmentDto, String> tcStartingTime;
    @FXML
    private TableColumn<MedicalAppointmentDto, String> tcDate;
    @FXML
    private TableColumn<MedicalAppointmentDto, String> tcPhone;
    @FXML
    private TableColumn<MedicalAppointmentDto, String> tcState;

    private List<UserDto> userDtos = new ArrayList<>();
    private UserService userService = new UserService();
    private Map<String, AgendaDto> agendaDtos = new HashMap<>();
    private AgendaService agendaService = new AgendaService();
    private DoctorService doctorService = new DoctorService();
    private DoctorDto doctorBuffer;
    private ReportService reportService = new ReportService();
    private UserDto u = new UserDto();
    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        u = (UserDto) data.getData("userLoggued");
        initializeComboBox();
        initializeList();
        loadDoctors();
        cbSearchParameter.setOnKeyReleased(event -> searchDoctorEventKey(event));
    }

    @FXML
    private void btnGenerateMedicalAppointmentReport(ActionEvent event) {
        LocalDate startingDate = dpStartingDate.getValue();
        LocalDate endingDate = dpEndingDate.getValue();
        if (startingDate != null && endingDate != null && doctorBuffer != null) {
            if (tblMedicalAppointmentsView.getItems().isEmpty()) {
                Message.showNotification("Ups", MessageType.INFO, "emptyList");
                return;
            }
            if (u.getLanguage().equals("ENGLISH")) {
                ResponseWrapper response = reportService.createAgendaReport(doctorBuffer.getId(), startingDate.toString(), endingDate.toString(), "en");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification("ERROR", MessageType.ERROR, response.getMessage());
                }
            } else {
                ResponseWrapper response = reportService.createAgendaReport(doctorBuffer.getId(), startingDate.toString(), endingDate.toString(), "es");
                if (response.getCode() != ResponseCode.OK) {
                    Message.showNotification("ERROR", MessageType.ERROR, response.getMessage());
                }
            }
        }
    }

    @FXML
    private void dpSelectStartingDate(ActionEvent event) {
        loadAgendas();
    }

    @FXML
    private void dpSelectEndingDate(ActionEvent event) {
        loadAgendas();
    }

    @FXML
    private void selectDoctorAction(ActionEvent event) {
        UserDto user = cbSearchParameter.getValue();
        if (user != null) {
            doctorBuffer = (DoctorDto) doctorService.getDoctorById(user.getId()).getData();
            loadAgendas();
        }
    }

    private void searchDoctorEventKey(KeyEvent event) {
        String idToSearch = cbSearchParameter.getEditor().getText();
        if (idToSearch != null) {
            cbSearchParameter.getItems().clear();
            cbSearchParameter.show();
            if (!idToSearch.isEmpty()) {
                cbSearchParameter.getItems().addAll(userDtos.stream().filter(t -> t.getIdentification().contains(idToSearch)).collect(Collectors.toList()));
                return;
            }
            cbSearchParameter.getItems().addAll(userDtos);
        }
    }

    private void loadAgendas() {
        LocalDate startingDate = dpStartingDate.getValue();
        LocalDate endingDate = dpEndingDate.getValue();
        if (startingDate != null && endingDate != null && doctorBuffer != null) {
            tblMedicalAppointmentsView.getItems().clear();
            for (AgendaDto i : doctorBuffer.getAgendas()) {
                LocalDate agendaDate = LocalDate.parse(i.getAgendaDate());
                if ((startingDate.isBefore(agendaDate) || startingDate.isEqual(agendaDate)) && endingDate.isAfter(agendaDate) || endingDate.isEqual(agendaDate)) {
                    AgendaDto agenda = (AgendaDto) agendaService.getAgendaById(i.getId()).getData();
                    if (agenda != null) {
                        agendaDtos.put(agenda.getAgendaDate(), agenda);
                        loadMedicalAppointments(agenda.getMedicalAppointments());
                    }
                }
            }
        }
    }

    private void loadMedicalAppointments(List<MedicalAppointmentDto> medicalAppointments) {
        tblMedicalAppointmentsView.getItems().addAll(medicalAppointments);
    }

    private void initializeComboBox() {
        cbSearchParameter.setCellFactory(param -> new ListCell<UserDto>() {
            @Override
            protected void updateItem(UserDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdentification());
                }
            }
        });

        cbSearchParameter.setConverter(new StringConverter<UserDto>() {
            @Override
            public String toString(UserDto user) {
                return user == null ? null : user.getIdentification();
            }

            @Override
            public UserDto fromString(String string) {
                return null;
            }
        });
    }

    private void initializeList() {
        tcDate.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        tcStartingTime.setCellValueFactory(new PropertyValueFactory<>("scheduledStartTime"));
        tcState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tcPatient.setCellValueFactory(cellData -> {
            MedicalAppointmentDto medicalAppointmentDto = cellData.getValue();
            if (medicalAppointmentDto != null && medicalAppointmentDto.getPatient() != null) {
                PatientDto patientDto = medicalAppointmentDto.getPatient();
                String patientName = patientDto.getName() + " " + patientDto.getFirstLastname() + " " + patientDto.getSecondLastname();
                return new SimpleStringProperty(patientName);
            }
            return new SimpleStringProperty("-");
        });
    }

    private void loadDoctors() {
        userDtos = (List<UserDto>) userService.getUsers().getData();
        if (userDtos != null) {
            userDtos = userDtos.stream().filter(user -> user.getDoctor() != null).collect(Collectors.toList());
            userDtos.stream().forEach(user -> cbSearchParameter.getItems().add(user));

        }
    }

}
