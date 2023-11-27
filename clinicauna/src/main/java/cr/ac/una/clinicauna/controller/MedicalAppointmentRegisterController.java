package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.AgendaDto;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.MedicalAppointmentDto;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.AgendaService;
import cr.ac.una.clinicauna.services.MedicalAppointmentService;
import cr.ac.una.clinicauna.services.PatientService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author vargas
 */
public class MedicalAppointmentRegisterController implements Initializable {

    @FXML
    private VBox mainView;
    @FXML
    private ImageView imgPhotoProfile;
    @FXML
    private Button createPatient;
    @FXML
    private JFXTextField txfEmail;
    @FXML
    private JFXTextField txfPhoneNumber;
    @FXML
    private JFXTextArea txfReason;
    @FXML
    private ComboBox<PatientDto> cbIdentification;
    @FXML
    private ComboBox<String> cbHoursAvailable;
    @FXML
    private StackPane parent;
    @FXML
    private ToggleGroup rbGroup;
    @FXML
    private DatePicker dpAppoinmentDate;
    @FXML
    private Spinner<Integer> spSlots;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblAgendedBy;
    private Data data = Data.getInstance();

    private PatientService patientService = new PatientService();
    private AgendaService agendaService = new AgendaService();
    private MedicalAppointmentService medicalAppointmentService = new MedicalAppointmentService();
    private List<PatientDto> patients = new ArrayList();
    private Map<String, AgendaDto> agendaDtos = new HashMap<>();
    private List<String> allHours = new ArrayList<>();
    private MedicalAppointmentDto medicalAppointmentBuffer;
    private PatientDto patientBuffer;
    private AgendaDto agendaBuffer = new AgendaDto();
    private DoctorDto doctorBuffer;
    private String fechaAppointment;
    private UserDto scheduledBy;

    private boolean isEditing;
    private String option = "";
    private String startShiftTime = "";
    private String endShiftTime = "";
    private Long hourlySlots = 0L;
    private List<LocalDate> localDays = new ArrayList<>();
    private List<AgendaDto> weekendAgendas = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initializeSpinners();
            initializeComboBox();
            agendaBuffer = (AgendaDto) data.getData("agendaBuffer");

            fechaAppointment = (String) data.getData("fechaAppointment");
            doctorBuffer = (DoctorDto) data.getData("doctorBuffer");
            String hourAppointment = (String) data.getData("hourAppointment");
            patientBuffer = (PatientDto) data.getData("patientBuffer");
            medicalAppointmentBuffer = (MedicalAppointmentDto) data.getData("medicalAppointmentBuffer");
            agendaBuffer = agendaBuffer == null ? new AgendaDto() : agendaBuffer;
            medicalAppointmentBuffer = medicalAppointmentBuffer == null ? new MedicalAppointmentDto() : medicalAppointmentBuffer;
            if (medicalAppointmentBuffer.getId() == null) {
                medicalAppointmentBuffer.setScheduledDate(fechaAppointment);
                medicalAppointmentBuffer.setScheduledStartTime(hourAppointment);
            } else {
                patientBuffer = medicalAppointmentBuffer.getPatient();
            }
            isEditing = medicalAppointmentBuffer.getId() != null;
            scheduledBy = (UserDto) data.getData("scheduledBy");
            scheduledBy = isEditing ? medicalAppointmentBuffer.getScheduledBy() : scheduledBy;
            if (!isEditing) {
                btnDelete.setVisible(false);
            }
            loadAgendas(doctorBuffer);
            addPatientsInCb();
            bindMedicalAppointment();
            loadHoursInComboBox();
        } catch (Exception e) {
            System.out.println(e.toString());
            backAction(null);
        }
    }

    @FXML
    private void backAction(MouseEvent event) {
        try {
            option = option.toLowerCase();
            FXMLLoader mainLoader = App.getFXMLLoader("Main");
            Animate.MakeDefaultFadeTransition(mainView, mainLoader.load());
            MainController controller = mainLoader.getController();
            if (controller != null) {
                controller.loadView("agendaModule");
            }

        } catch (IOException e) {
        }

    }

    @FXML
    private void btnCreateMedicalAppointment(ActionEvent event) {
        if (!verifyFields()) {
            Message.showNotification("Ups", MessageType.INFO, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                boolean isAgendaCreated = agendaBuffer == null ? createAgenda(dpAppoinmentDate.getValue().toString()) : true;
                if (isAgendaCreated) {
                    medicalAppointmentBuffer.setAgenda(agendaBuffer);
                    medicalAppointmentBuffer.setPatient(patientBuffer);
                    medicalAppointmentBuffer.setScheduledBy(scheduledBy);
                    medicalAppointmentBuffer.setSlotsNumber((long) spSlots.getValue());
                    medicalAppointmentBuffer.setScheduledDate(agendaBuffer.getAgendaDate());
                    String hour = cbHoursAvailable.getValue();
                    medicalAppointmentBuffer.setScheduledStartTime(hour);
                    medicalAppointmentBuffer.setScheduledEndTime(getEndTime(hour, agendaBuffer.getHourlySlots(), spSlots.getValue()));
                    if (!medicalAppointmentBuffer.getPatientEmail().equals(patientBuffer.getEmail()) || !medicalAppointmentBuffer.getPatientPhoneNumber().equals(patientBuffer.getPhoneNumber())) {
                        patientBuffer.setEmail(medicalAppointmentBuffer.getPatientEmail());
                        patientBuffer.setPhoneNumber(medicalAppointmentBuffer.getPatientPhoneNumber());
                        patientService.updatePatient(patientBuffer);
                    }
                    saveMedicalAppointment(medicalAppointmentBuffer);

                }
            });
        }).start();
    }

    private void addPatientsInCb() {
        patients = (List<PatientDto>) patientService.getPatients().getData();
        if (patients != null) {
            cbIdentification.getItems().addAll(patients);
        }
    }

    @FXML
    private void createPatient(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = App.getFXMLLoader("PatientRegister");
        Animate.MakeDefaultFadeTransition(parent, patientLoader.load());
        PatientRegisterController controller = patientLoader.getController();
        if (controller != null) {
            controller.loadView("medicalAppointmentRegister");
        }

    }

    @FXML
    private void searchById(KeyEvent event) {
        String idToSearch = cbIdentification.getEditor().getText();
        if (idToSearch != null) {
            cbIdentification.show();
            cbIdentification.getItems().clear();
            if (!idToSearch.isEmpty()) {
                cbIdentification.getItems().addAll(patients.stream().filter(t -> t.getIdentification().contains(idToSearch)).collect(Collectors.toList()));
                return;
            }
            cbIdentification.getItems().addAll(patients);
            cbIdentification.show();
        }

    }

    @FXML
    private void btnDeleteMedicalAppointment(ActionEvent event) {
        if (medicalAppointmentBuffer.getId() != null) {
            ResponseWrapper response = medicalAppointmentService.deleteMedicalAppointments(medicalAppointmentBuffer);
            if (response.getCode() == ResponseCode.OK) {
                backAction(null);
                return;
            }
            Message.showNotification("Success", MessageType.INFO, response.getMessage());
        }
    }

    @FXML
    private void cbSelectPatient(ActionEvent event) {
        PatientDto patient = cbIdentification.getValue();
        if (patient != null) {
            patientBuffer = patient;
            medicalAppointmentBuffer.setPatientEmail(patientBuffer.getEmail());
            medicalAppointmentBuffer.setPatientPhoneNumber(patientBuffer.getPhoneNumber());
        }
    }

    @FXML
    private void setSlotsAvailable(InputMethodEvent event) {
        loadHoursInComboBox();
    }

    @FXML
    private void dpAppoinmentChange(ActionEvent event) {
        loadHoursInComboBox();
    }

    private void loadHoursInComboBox() {
        if (dpAppoinmentDate.getValue() != null) {
            agendaBuffer = agendaDtos.get(dpAppoinmentDate.getValue().toString());
            getStartEndTime();
            allHours = calculateHours(startShiftTime, endShiftTime, hourlySlots);
            if (agendaBuffer == null) {
                addAllHoursInCb(getAvailableHoursForAppointment(allHours,
                        doctorBuffer.getHourlySlots(), spSlots.getValue(), new ArrayList()));
            } else {
                addAllHoursInCb(getAvailableHoursForAppointment(allHours,
                        agendaBuffer.getHourlySlots(), spSlots.getValue(), agendaBuffer.getMedicalAppointments()));
            }
        }
    }

    private void initializeComboBox() {
        cbIdentification.setCellFactory(param -> new ListCell<PatientDto>() {
            @Override
            protected void updateItem(PatientDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdentification());
                }
            }
        });

        cbIdentification.setConverter(new StringConverter<PatientDto>() {
            @Override
            public String toString(PatientDto user) {
                return user == null ? null : user.getIdentification();
            }

            @Override
            public PatientDto fromString(String string) {
                return null;
            }
        });
        cbIdentification.valueProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    public void bindMedicalAppointment() {

        txfReason.textProperty().bindBidirectional(medicalAppointmentBuffer.reason);
        txfEmail.textProperty().bindBidirectional(medicalAppointmentBuffer.patientEmail);
        txfPhoneNumber.textProperty().bindBidirectional(medicalAppointmentBuffer.patientPhoneNumber);
        dpAppoinmentDate.valueProperty().bindBidirectional(medicalAppointmentBuffer.scheduledDate);
        cbHoursAvailable.valueProperty().bindBidirectional(medicalAppointmentBuffer.scheduledStartTime);
        if (scheduledBy != null) {
            lblAgendedBy.setText(scheduledBy.getName() + " " + scheduledBy.getFirstLastname() + " " + scheduledBy.getSecondLastname());
        }
        if (patientBuffer != null) {
            txfEmail.textProperty().set(patientBuffer.getEmail());
            txfPhoneNumber.textProperty().set(patientBuffer.getPhoneNumber());
        }
        if (medicalAppointmentBuffer.getSlotsNumber() != null) {
            Long slotsNumber = medicalAppointmentBuffer.getSlotsNumber();
            spSlots.getEditor().setText(slotsNumber.toString());
            spSlots.setEditable(true);
            spSlots.commitValue();
            spSlots.setEditable(false);
        }
        if (medicalAppointmentBuffer.getPatient() != null) {
            cbIdentification.setValue(medicalAppointmentBuffer.getPatient());
        }
        rbGroup.selectedToggleProperty()
                .addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                    if (newValue != null && medicalAppointmentBuffer != null) {
                        medicalAppointmentBuffer.setState(medicalAppointmentBuffer.parseState(((RadioButton) newValue).getText().toLowerCase()));//no sirve
                    }
                });
        if (isEditing) {
            rbGroup.getToggles().forEach(t -> {
                if (t instanceof RadioButton) {
                    if (medicalAppointmentBuffer.getState().toLowerCase().equals("attended")) {
                        if (((RadioButton) t).getText().toLowerCase().equals("attended")
                                || ((RadioButton) t).getText().toLowerCase().equals("atendida")) {
                            t.setSelected(true);
                        }
                    } else if (medicalAppointmentBuffer.getState().toLowerCase().equals("cancelled")) {
                        if (((RadioButton) t).getText().toLowerCase().equals("cancelled")
                                || ((RadioButton) t).getText().toLowerCase().equals("cancelada")) {
                            t.setSelected(true);
                        }
                    } else if (medicalAppointmentBuffer.getState().toLowerCase().equals("scheduled")) {
                        if (((RadioButton) t).getText().toLowerCase().equals("scheduled")
                                || ((RadioButton) t).getText().toLowerCase().equals("agendada")) {
                            t.setSelected(true);
                        }
                    } else if (medicalAppointmentBuffer.getState().toLowerCase().equals("absent")) {
                        if (((RadioButton) t).getText().toLowerCase().equals("absent")
                                || ((RadioButton) t).getText().toLowerCase().equals("ausente")) {
                            t.setSelected(true);
                        }
                    }
                }
            });
        }
    }

    public void initializeSpinners() {
        spSlots.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1));
        spSlots.valueProperty().addListener((observable, oldValue, newValue) -> {
            setSlotsAvailable(null);
        });
        StringConverter<Integer> formatter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%1d", value);
            }

            @Override
            public Integer fromString(String text) {
                try {
                    return Integer.valueOf(text);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        };
        spSlots.getValueFactory().setConverter(formatter);
    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfEmail, txfPhoneNumber, spSlots, cbHoursAvailable);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null && ((JFXTextField) i).getText().isBlank()) {
                return false;
            } else if (i instanceof JFXTextArea && ((JFXTextArea) i).getText() != null && ((JFXTextArea) i).getText().isBlank()) {
                return false;
            } else if (i instanceof Spinner && ((Spinner) i).getValue() == null) {
                return false;
            } else if (i instanceof ComboBox && ((ComboBox) i).getValue() == null) {
                return false;
            }
        }
        return patientBuffer != null && rbGroup.getSelectedToggle() != null;
    }

    public boolean createAgenda(String fechaAppointment) {
        if (doctorBuffer != null) {
            agendaBuffer = new AgendaDto();
            agendaBuffer.setDoctor(doctorBuffer);
            agendaBuffer.setAgendaDate(fechaAppointment);
            agendaBuffer.setHourlySlots(doctorBuffer.getHourlySlots());
            agendaBuffer.setShiftStartTime(doctorBuffer.getShiftStartTime());
            agendaBuffer.setShiftEndTime(doctorBuffer.getShiftEndTime());
            return saveAgenda(agendaBuffer);
        }
        Message.showNotification("ERROR", MessageType.ERROR, "doctorBufferNull");
        return false;
    }

    public boolean saveAgenda(AgendaDto agendaDto) {
        ResponseWrapper response = agendaService.createAgenda(agendaDto);
        if (response.getCode() == ResponseCode.OK) {
            agendaBuffer = (AgendaDto) response.getData();
            return true;
        }
        Message.showNotification("Ups", MessageType.ERROR, response.getMessage());
        return false;
    }

    public boolean saveMedicalAppointment(MedicalAppointmentDto medicalAppointment) {
        ResponseWrapper response = isEditing ? medicalAppointmentService.updateMedicalAppointments(medicalAppointment)
                : medicalAppointmentService.createMedicalAppointments(medicalAppointment);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification("Success", MessageType.CONFIRMATION, response.getMessage());
            medicalAppointmentBuffer = (MedicalAppointmentDto) response.getData();
            backAction(null);
            return true;
        }
        Message.showNotification("Ups", MessageType.ERROR, response.getMessage());
        System.out.println(response.getMessage());
        return false;
    }

    private List<String> calculateHours(String startTime, String endTime, Long fieldsPerHour) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long intervalMillis = TimeUnit.HOURS.toMillis(1) / fieldsPerHour;
            for (long time = start.getTime(); time < end.getTime(); time += intervalMillis) {
                Date newTime = new Date(time);
                result.add(sdf.format(newTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void loadView(PatientDto patientDto, String option) {
        if (patientDto != null && patientDto.getId() != null) {
            patientBuffer = patientDto;
        }
        this.option = option;
    }

    private boolean checkOverlap(String startTime, String endTime, MedicalAppointmentDto mA) {

        LocalTime newStartTimeAppointment = LocalTime.parse(startTime);
        LocalTime newEndTimeAppointment = LocalTime.parse(endTime);
        String end = agendaBuffer == null ? doctorBuffer.getShiftEndTime() : agendaBuffer.getShiftEndTime();
        if (mA != null) {
            if (Objects.equals(mA.getId(), medicalAppointmentBuffer.getId()) || mA.getState().equals("CANCELLED")) {
                return false;
            }
            LocalTime medicalAppointmentstartExist = LocalTime.parse(mA.getScheduledStartTime());
            LocalTime medicalAppointmentendExist = LocalTime.parse(mA.getScheduledEndTime());
            if ((newStartTimeAppointment.isBefore(medicalAppointmentendExist) || newStartTimeAppointment.equals(medicalAppointmentendExist)) && (newEndTimeAppointment.isAfter(medicalAppointmentstartExist) || newEndTimeAppointment.equals(medicalAppointmentstartExist))) {
                return true;
            }
        }
        return newEndTimeAppointment.isAfter(LocalTime.parse(end)) || newEndTimeAppointment.equals(LocalTime.parse(end));
    }

    private String getEndTime(String startTime, Long slots, int medicalAppointmentSlots) {
        long intervalMillis = TimeUnit.HOURS.toMillis(1) / slots;
        long intervalMinutes = TimeUnit.MILLISECONDS.toMinutes(intervalMillis);
        LocalTime horaInicioLocal = LocalTime.parse(startTime);
        LocalTime horaFin = horaInicioLocal.plusMinutes(intervalMinutes * (medicalAppointmentSlots - 1));
        return horaFin.toString();
    }

    private List<String> getAvailableHoursForAppointment(List<String> horasDisponibles, Long slots, int nAppSlots, List<MedicalAppointmentDto> appointments) {
        List<String> hoursAvailable = new ArrayList<>();
        if (!appointments.isEmpty()) {
            for (String hour : horasDisponibles) {
                boolean disponible = true;

                for (MedicalAppointmentDto mA : appointments) {
                    if (checkOverlap(hour, getEndTime(hour, slots, nAppSlots), mA)) {
                        disponible = false;
                    }
                }

                if (disponible) {
                    hoursAvailable.add(hour);
                }
            }
        } else {
            for (String hour : horasDisponibles) {
                boolean disponible = true;

                if (checkOverlap(hour, getEndTime(hour, slots, nAppSlots), null)) {
                    disponible = false;
                }

                if (disponible) {
                    hoursAvailable.add(hour);
                }
            }
        }
        return hoursAvailable;
    }

    private void loadAgendas(DoctorDto doctorDto) {
        if (doctorDto != null) {
            for (AgendaDto i : doctorDto.getAgendas()) {
                AgendaDto agenda = (AgendaDto) agendaService.getAgendaById(i.getId()).getData();
                if (agenda != null) {
                    agendaDtos.put(agenda.getAgendaDate(), agenda);
                }
            }
        }

    }

    private void getStartEndTime() {
        if (agendaBuffer == null && doctorBuffer != null) {
            startShiftTime = doctorBuffer.getShiftStartTime();
            endShiftTime = doctorBuffer.getShiftEndTime();
            hourlySlots = doctorBuffer.getHourlySlots();
        } else {
            startShiftTime = agendaBuffer.getShiftStartTime();
            endShiftTime = agendaBuffer.getShiftEndTime();
            hourlySlots = agendaBuffer.getHourlySlots();
        }
    }

    private void addAllHoursInCb(List<String> horasDisp) {//restringir hora fin
        cbHoursAvailable.setItems(FXCollections.observableArrayList(horasDisp));
    }

}
