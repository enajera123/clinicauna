package cr.ac.una.clinicauna.controller;

import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.components.AppointmentNode;
import cr.ac.una.clinicauna.components.Header;
import cr.ac.una.clinicauna.model.AgendaDto;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.MedicalAppointmentDto;
import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.AgendaService;
import cr.ac.una.clinicauna.services.DoctorService;
import cr.ac.una.clinicauna.services.MedicalAppointmentService;
import cr.ac.una.clinicauna.services.UserService;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import cr.ac.una.clinicauna.util.AgendaBuilder;
import cr.ac.una.clinicauna.util.Data;

import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author arayaroma
 */
public class AgendaModuleController implements Initializable {

    @FXML
    private HBox hbAgendaHeader;
    @FXML
    private GridPane gpAgenda;
    @FXML
    private Label lbDoctorName;
    @FXML
    private Label lbDoctorCode;
    @FXML
    private Label lbDoctorIdCard;
    @FXML
    private ComboBox<UserDto> cbDoctor;
    @FXML
    private VBox parent;
    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private ImageView deleteZone;

    private Integer countWeeks = 0;
    private DoctorService doctorService = new DoctorService();
    private UserService userService = new UserService();
    private DoctorDto doctorBuffer;
    private AgendaService agendaService = new AgendaService();
    private Map<String, AgendaDto> agendaDtos = new HashMap<>();
    private Map<String, Integer> days = new HashMap();
    private Map<String, Integer> medicalAppointmentsHours = new HashMap();
    private List<UserDto> userDtos = new ArrayList<>();
    private List<String> hoursCalculated = new ArrayList<>();
    private Data data = Data.getInstance();
    private MedicalAppointmentDto medicalAppointentBuffer;
    private MedicalAppointmentService medicalAppointmentService = new MedicalAppointmentService();
    private UserDto userLoggued;
    private String startShiftTime = "";
    private String endShiftTime = "";
    private Long hourlySlots = 0L;
    private List<LocalDate> localDays = new ArrayList<>();
    private List<AgendaDto> weekendAgendas = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLoggued = (UserDto) data.getData("userLoggued");
        countWeeks = (Integer) data.getData("countWeeks");
        countWeeks = countWeeks == null ? 0 : countWeeks;//Set the actual week: 0 is actual and then plus and min the weeks
        setDays(countWeeks);
        initializeComboBox();
        loadDoctors();
        cbDoctor.setOnKeyReleased(event -> searchDoctorEventKey(event));

    }

    @FXML
    private void leftArrowAction(MouseEvent event) {
        setDays(countWeeks -= 1);
        if (doctorBuffer != null) {
            loadGrid();
        }

    }

    @FXML
    private void rigthArrowAction(MouseEvent event) {
        setDays(countWeeks += 1);
        if (doctorBuffer != null) {

            loadGrid();
        }
    }

    @FXML
    private void cbSelectDoctor(ActionEvent event) {
        UserDto user = cbDoctor.getValue();
        if (user != null) {
            doctorBuffer = (DoctorDto) doctorService.getDoctorById(user.getId()).getData();
            loadDoctor();
        }
    }

    @FXML
    private void btnSetActualWeekAction(ActionEvent event) {
        setDays(0);
        countWeeks = 0;
        if (doctorBuffer != null) {
            loadGrid();
        }
    }

    private void searchDoctorEventKey(KeyEvent event) {
        String idToSearch = cbDoctor.getEditor().getText();
        if (idToSearch != null) {
            cbDoctor.getItems().clear();
            cbDoctor.show();
            if (!idToSearch.isEmpty()) {
                cbDoctor.getItems().addAll(userDtos.stream().filter(t -> t.getIdentification().contains(idToSearch)).collect(Collectors.toList()));
                return;
            }
            cbDoctor.getItems().addAll(userDtos);
        }
    }

    /**
     * Initialize all the requires in grid
     */
    private void loadGrid() {
        cleanAgenda();
        loadActualWeek();
        getStartEndTime();
        hoursCalculated = calculateHours(startShiftTime, endShiftTime, hourlySlots);
        loadHours(hoursCalculated);
        loadPanes();
        loadAgendas(doctorBuffer);
    }

    private void loadActualWeek() {
        if (doctorBuffer != null) {
            weekendAgendas.clear();
            for (AgendaDto i : doctorBuffer.getAgendas()) {
                if (localDays.contains(LocalDate.parse(i.getAgendaDate()))) {
                    weekendAgendas.add(i);
                }
            }
        }
    }

    private void getStartEndTime() {
        if (doctorBuffer != null && weekendAgendas.isEmpty()) {
            startShiftTime = doctorBuffer.getShiftStartTime();
            endShiftTime = doctorBuffer.getShiftEndTime();
            hourlySlots = doctorBuffer.getHourlySlots();
        } else {
            startShiftTime = weekendAgendas.get(0).getShiftStartTime();
            endShiftTime = weekendAgendas.get(0).getShiftEndTime();
            hourlySlots = weekendAgendas.get(0).getHourlySlots();
        }
    }

    private void loadDoctor() {
        new Thread(() -> {
            Platform.runLater(() -> {
                if (doctorBuffer != null) {
                    lbDoctorCode.setText(doctorBuffer.getCode());
                    lbDoctorIdCard.setText(String.valueOf(doctorBuffer.getIdCard()));
                    loadGrid();
                    if (doctorBuffer.getUser() != null) {
                        lbDoctorName.setText(doctorBuffer.getUser().getName());
                    }
                }
            });
        }).start();

    }

    private boolean hasTimeConflict(String startTime, String endingTime, AgendaDto agendaDto) {
        try {
            if (agendaDto != null) {
                LocalTime medicalStartTime, medicalEndingTime;
                LocalTime newMedicalStartTime = LocalTime.parse(startTime), newMedicalEndingTime = LocalTime.parse(endingTime);
                LocalTime finalHourDoctor = LocalTime.parse(agendaDto.getShiftEndTime());
                for (MedicalAppointmentDto medicalAppointmentDto : agendaDto.getMedicalAppointments()) {
                    if (medicalAppointentBuffer != null && !Objects.equals(medicalAppointentBuffer.getId(), medicalAppointmentDto.getId()) && !medicalAppointmentDto.getState().equals("CANCELLED")) {
                        medicalStartTime = LocalTime.parse(medicalAppointmentDto.getScheduledStartTime());
                        medicalEndingTime = LocalTime.parse(medicalAppointmentDto.getScheduledEndTime());

                        if (newMedicalStartTime.isBefore(medicalEndingTime) && (newMedicalEndingTime.isAfter(medicalStartTime) || newMedicalEndingTime.equals(medicalStartTime))) {
                            return true;
                        }
                    }
                }
                return newMedicalEndingTime.isAfter(finalHourDoctor) || newMedicalEndingTime.equals(finalHourDoctor);
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.toString());
            return true;
        }
    }

    /**
     * Update the medical appointment when is dragged
     *
     * @param medicalAppointmentDto
     * @param newTime
     * @param newDate
     * @return
     */
    private boolean updateMedicalAppointment(MedicalAppointmentDto medicalAppointmentDto, String newTime, LocalDate newDate) {
        try {
            if (medicalAppointmentDto != null) {
                AgendaDto agendaDto = agendaDtos.get(newDate.toString());
                if (agendaDto == null) {
                    agendaDto = new AgendaDto();
                    agendaDto.setAgendaDate(newDate.toString());
                    agendaDto = createAgenda(agendaDto);
                }
                if (agendaDto != null) {

                    String shiftEndingTimeIndex = getEndTime(newTime, hourlySlots, medicalAppointmentDto.getSlotsNumber().intValue());
                    if (!hasTimeConflict(newTime, shiftEndingTimeIndex, agendaDto)) {
                        medicalAppointmentDto.setAgenda(new AgendaDto(agendaDto));
                        medicalAppointmentDto.setScheduledDate(newDate.toString());
                        medicalAppointmentDto.setScheduledStartTime(newTime);
                        medicalAppointmentDto.setScheduledEndTime(shiftEndingTimeIndex);
                        ResponseWrapper response = medicalAppointmentService.updateMedicalAppointments(medicalAppointmentDto);
                        if (response.getCode() != ResponseCode.OK) {
                            Message.showNotification("Error", MessageType.ERROR, response.getMessage());
                            return false;
                        }
                        medicalAppointentBuffer = (MedicalAppointmentDto) response.getData();
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    private String getEndTime(String startTime, Long slots, int medicalAppointmentSlots) {
        long intervalMillis = TimeUnit.HOURS.toMillis(1) / slots;
        long intervalMinutes = TimeUnit.MILLISECONDS.toMinutes(intervalMillis);
        LocalTime horaInicioLocal = LocalTime.parse(startTime);
        LocalTime horaFin = horaInicioLocal.plusMinutes(intervalMinutes * (medicalAppointmentSlots - 1));//Quitar el 1
        return horaFin.toString();
    }

    private AgendaDto createAgenda(AgendaDto agendaDto) {
        agendaDto.setDoctor(doctorBuffer);
        agendaDto.setHourlySlots(hourlySlots);
        agendaDto.setShiftStartTime(startShiftTime);
        agendaDto.setShiftEndTime(endShiftTime);
        ResponseWrapper response = agendaService.createAgenda(agendaDto);
        agendaDto = (AgendaDto) response.getData();
        return agendaDto;
    }

    private void loadAgendas(DoctorDto doctorDto) {
        for (AgendaDto i : doctorDto.getAgendas()) {
            AgendaDto agenda = (AgendaDto) agendaService.getAgendaById(i.getId()).getData();
            if (agenda != null) {
                agendaDtos.put(agenda.getAgendaDate(), agenda);
                loadMedicalAppointments(agenda.getMedicalAppointments());
            }
        }
    }

    private void updateAgendas(DoctorDto doctorDto) {
        for (AgendaDto i : doctorDto.getAgendas()) {
            AgendaDto agenda = (AgendaDto) agendaService.getAgendaById(i.getId()).getData();
            if (agenda != null) {
                agendaDtos.put(agenda.getAgendaDate(), agenda);
            }
        }
    }

    private void loadMedicalAppointments(List<MedicalAppointmentDto> medicalAppointments) {
        for (int i = 0; i < medicalAppointments.size(); i++) {
            MedicalAppointmentDto medicalAppointmentDto = medicalAppointments.get(i);
            String date = medicalAppointmentDto.getScheduledDate();
            String time = medicalAppointmentDto.getScheduledStartTime();
            Integer slotsNumber = medicalAppointmentDto.getSlotsNumber().intValue();
            Integer column = days.get(date);
            Integer row = medicalAppointmentsHours.get(time);
            if (column != null && row != null) {
                AppointmentNode node = createMedicalAppointmentCard(medicalAppointmentDto);
                gpAgenda.add(node, column, row);
                GridPane.setRowSpan(node, slotsNumber);
            }
        }
    }

    private void cleanAgenda() {
        for (int i = 1; i < gpAgenda.getRowCount(); i++) {
            for (int j = 0; j < gpAgenda.getColumnCount(); j++) {
                removeNodeInGrid(i, j);
            }
        }
    }

    public void loadView(DoctorDto doctorDto) {
        if (doctorDto != null) {
            doctorBuffer = (DoctorDto) doctorService.getDoctorById(doctorDto.getId()).getData();
            loadDoctor();
        }
    }

    private void loadDoctors() {
        userDtos = (List<UserDto>) userService.getUsers().getData();
        if (userDtos != null) {
            userDtos = userDtos.stream().filter(user -> user.getDoctor() != null).collect(Collectors.toList());
            userDtos.stream().forEach(user -> cbDoctor.getItems().add(user));

        }
    }

    /**
     * Set just the information required of the object
     */
    private void initializeComboBox() {
        cbDoctor.setCellFactory(param -> new ListCell<UserDto>() {
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

        cbDoctor.setConverter(new StringConverter<UserDto>() {
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

    /**
     * Set the week on the agenda
     *
     * @param weekOffset
     */
    private void setDays(int weekOffset) {
        LocalDate date = (weekOffset == 0) ? LocalDate.now() : (weekOffset > 0)
                ? LocalDate.now().plusWeeks(weekOffset) : LocalDate.now().minusWeeks(-weekOffset);
        AgendaBuilder agenda = AgendaBuilder.builder().withActualDate(date).build();
        localDays.clear();
        localDays = agenda.calculateWeekDays(date);
        days.clear();
        for (int i = 0; i < localDays.size(); i++) {
            removeNodeInGrid(0, i + 1);
            LocalDate actualDay = localDays.get(i);
            String day = actualDay.getDayOfWeek().name().substring(0, 3);
            Integer number = localDays.get(i).getDayOfMonth();
            Header nodeDay = createAgendaHeader(day + " " + number, actualDay);
            if (LocalDate.now().equals(actualDay)) {
                nodeDay.getStyleClass().add("actual-day");
            }
            days.put(localDays.get(i).toString(), i + 1);
            gpAgenda.add(nodeDay, i + 1, 0);
        }
        lblMonth.setText(String.valueOf(date.getMonthValue()));
        lblYear.setText(String.valueOf(date.getYear()));

    }

    private void removeNodeInGrid(Integer row, Integer column) {
        Integer column2, row2;
        List<Node> nodeToRemove = new ArrayList<>();
        for (Node node : gpAgenda.getChildren()) {
            column2 = GridPane.getColumnIndex(node);
            row2 = GridPane.getRowIndex(node);
            if (Objects.equals(column2, column) && Objects.equals(row, row2)) {
                nodeToRemove.add(node);
            }
        }
        gpAgenda.getChildren().removeAll(nodeToRemove);
    }

    private Header createAgendaHeader(String info, LocalDate date) {
        Header header = new Header(info, date);
        header.getStyleClass().add("bg-black");
        header.getChildren().add(new Label(info));
        header.setAlignment(Pos.CENTER);
        return header;
    }

    /**
     * Determinate the intermediate hours into a interval time depending of
     * fields por hour
     *
     * @param startTime
     * @param endTime
     * @param fieldsPerHour
     * @return
     */
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

    /**
     * Load a hours list on the agenda view
     *
     * @param hours
     */
    private void loadHours(List<String> hours) {
        for (int i = 0; i < hours.size(); i++) {
            medicalAppointmentsHours.put(hours.get(i), i + 1);
            removeNodeInGrid(i + 1, 0);
            gpAgenda.add(createAgendaHeader(hours.get(i), null), 0, i + 1);
            RowConstraints row = new RowConstraints(USE_COMPUTED_SIZE);
            gpAgenda.getRowConstraints().add(row);
        }
    }

    /**
     * Visual medical appointment card
     *
     * @param medicalAppointmentDto
     * @return
     */
    private AppointmentNode createMedicalAppointmentCard(MedicalAppointmentDto medicalAppointmentDto) {
        AppointmentNode appointmentNode = new AppointmentNode(medicalAppointmentDto);
        appointmentNode.setAlignment(Pos.CENTER);
        PatientDto patientDto = medicalAppointmentDto.getPatient();
        if (patientDto != null) {
            appointmentNode.getChildren().addAll(new Label(patientDto.getName()), new Label("Tel: " + patientDto.getPhoneNumber()));
            appointmentNode.getStyleClass().add("cardMedicalAppointment");
        }
        appointmentNode.getStyleClass().add(medicalAppointmentDto.getState().toLowerCase());
        appointmentNode.setOnMouseClicked(e -> createMedicalAppointment(e, appointmentNode.getMedicalAppointmentDto()));
        initializeDragAndDrop(appointmentNode);
        return appointmentNode;
    }

    Node nodeBuffer;//Node buffer of the medical appointment

    private void initializeTrash() {
        deleteZone.setOnDragOver(event -> {
            if (event.getGestureSource() != gpAgenda && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                deleteZone.setImage(new Image(App.class.getResource("/cr/ac/una/clinicauna/img/basureroAbierto.png").toString()));
            }
            event.consume();
        });
        deleteZone.setOnDragExited(event -> {
            if (event.getGestureSource() != gpAgenda && event.getDragboard().hasString()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AgendaModuleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    deleteZone.setImage(new Image(App.class.getResource("/cr/ac/una/clinicauna/img/basurero.png").toString()));
                }).start();
            }
            event.consume();
        });
        deleteZone.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()) {
                medicalAppointentBuffer = ((AppointmentNode) nodeBuffer).getMedicalAppointmentDto();
                ResponseWrapper response = medicalAppointmentService.deleteMedicalAppointments(medicalAppointentBuffer);
                if (response.getCode() == ResponseCode.OK) {
                    gpAgenda.getChildren().remove(nodeBuffer);
                    new Thread(() -> updateAgendas(doctorBuffer)).start();
                }
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void initializeDragAndDrop(Node node) {
        initializeTrash();
        node.setOnDragDetected((event) -> {
            nodeBuffer = node;
            Dragboard dragboard = node.startDragAndDrop(TransferMode.MOVE);
            dragboard.setDragView(node.snapshot(null, null));
            ClipboardContent content = new ClipboardContent();
            content.putString("check");
            dragboard.setContent(content);
        });
        gpAgenda.setOnDragOver(event -> {
            if (event.getGestureSource() != gpAgenda && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        gpAgenda.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                Integer row = GridPane.getRowIndex((Node) event.getTarget());
                Integer col = GridPane.getColumnIndex((Node) event.getTarget());
                if (row != null && col != null && row > 0 && col > 0) {
                    if (nodeBuffer instanceof AppointmentNode) {
                        medicalAppointentBuffer = ((AppointmentNode) nodeBuffer).getMedicalAppointmentDto();
                        if (updateMedicalAppointment(medicalAppointentBuffer, getHourInGrid(row), LocalDate.parse(getDayInGrid(col)))) {
                            gpAgenda.getChildren().remove(nodeBuffer);
                            gpAgenda.add(nodeBuffer, col, row);
                            ((AppointmentNode) nodeBuffer).setMedicalAppointmentDto(medicalAppointentBuffer);
                            new Thread(() -> updateAgendas(doctorBuffer)).start();
                        }
                    }

                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private String getHourInGrid(int pos) {
        Integer row, column;
        for (Node i : gpAgenda.getChildren()) {
            row = GridPane.getRowIndex(i);
            column = GridPane.getColumnIndex(i);
            if (row != null && row == pos && column != null && column == 0 && i instanceof Header) {
                return ((Header) i).getMessage();
            }
        }
        return "";
    }

    private String getDayInGrid(int pos) {
        Integer row, column;
        for (Node i : gpAgenda.getChildren()) {
            row = GridPane.getRowIndex(i);
            column = GridPane.getColumnIndex(i);
            if (row != null && row == 0 && column != null && column == pos && i instanceof Header) {
                LocalDate date = ((Header) i).getDay();
                if (date != null) {
                    return date.toString();
                }
                return "";
            }
        }
        return "";
    }

    private void loadPanes() {
        for (int i = 1; i < gpAgenda.getRowCount(); i++) {
            for (int j = 1; j < gpAgenda.getColumnCount(); j++) {
                HBox hBox = new HBox();
                hBox.getStyleClass().add("paneContainer");
                hBox.setOnMouseClicked(event -> createMedicalAppointment(event, null));
                gpAgenda.add(hBox, j, i);
            }
        }
    }

    private void openMedicalAppointmentRegisterView() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("MedicalAppointmentRegister");
        Animate.MakeDefaultFadeTransition(parent, loader.load());
        MedicalAppointmentRegisterController controller = loader.getController();
        if (controller != null) {
            controller.loadView(null, "AgendaModule");
        }
    }

    private void createMedicalAppointment(MouseEvent event, MedicalAppointmentDto medicalAppointmentDto) {
        try {
            Integer column = GridPane.getColumnIndex((Node) event.getSource());
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            String day = column != null ? getDayInGrid(column) : null;
            String hour = row != null ? getHourInGrid(row) : null;
            if (day != null) {
                data.setData("agendaBuffer", agendaDtos.get(day));
            }
            data.setData("fechaAppointment", day);
            data.setData("hourAppointment", hour);
            data.setData("doctorBuffer", doctorBuffer);
            data.setData("scheduledBy", userLoggued);
            data.setData("medicalAppointmentBuffer", medicalAppointmentDto != null && medicalAppointmentDto.getState().equals("CANCELLED") ? null : medicalAppointmentDto);
            data.setData("countWeeks", countWeeks);
            openMedicalAppointmentRegisterView();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

};
