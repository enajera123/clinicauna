package cr.ac.una.clinicauna.model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author estebannajera
 */
public class MedicalAppointmentDto {

    private Long id;
    private Long slotsNumber;
    private AgendaDto agenda;
    private PatientDto patient;
    private PatientCareDto patientCare;
    private UserDto scheduledBy;
    public ObjectProperty<LocalDate> scheduledDate;
    public SimpleStringProperty scheduledStartTime;
    public SimpleStringProperty scheduledEndTime;
    public SimpleStringProperty state;
    public SimpleStringProperty reason;
    public SimpleStringProperty patientPhoneNumber;
    public SimpleStringProperty patientEmail;
    private Long version;

    public MedicalAppointmentDto() {
        scheduledDate = new SimpleObjectProperty<>();
        scheduledEndTime = new SimpleStringProperty();
        scheduledStartTime = new SimpleStringProperty();
        state = new SimpleStringProperty();
        reason = new SimpleStringProperty();
        patientPhoneNumber = new SimpleStringProperty();
        patientEmail = new SimpleStringProperty();
    }

    public MedicalAppointmentDto(MedicalAppointmentDto medicalAppointmentDto) {
        this();
        setId(medicalAppointmentDto.getId());
        setScheduledDate(medicalAppointmentDto.getScheduledDate());
        setScheduledEndTime(medicalAppointmentDto.getScheduledEndTime());
        setScheduledStartTime(medicalAppointmentDto.getScheduledStartTime());
        setSlotsNumber(medicalAppointmentDto.getSlotsNumber());
        setState(medicalAppointmentDto.getState());
        setReason(medicalAppointmentDto.getReason());
        setPatientPhoneNumber(medicalAppointmentDto.getPatientPhoneNumber());
        setPatientEmail(medicalAppointmentDto.getPatientEmail());
        setVersion(medicalAppointmentDto.getVersion());
    }

    public String parseState(String role) {
        switch (role) {
            case "scheduled":
                return "SCHEDULED";
            case "agendada":
                return "SCHEDULED";
            case "absent":
                return "ABSENT";
            case "ausente":
                return "ABSENT";
            case "attended":
                return "ATTENDED";
            case "atendida":
                return "ATTENDED";
            case "cancelled":
                return "CANCELLED";
            case "cancelada":
                return "CANCELLED";
        }
        return "";
    }

    public Long getId() {
        return this.id;
    }

    public PatientDto getPatient() {
        return this.patient;
    }

    public Long getSlotsNumber() {
        return slotsNumber;
    }

    public String getScheduledDate() {
        return this.scheduledDate.get().toString();
    }

    public String getScheduledStartTime() {
        return scheduledStartTime.get();
    }

    public String getScheduledEndTime() {
        return scheduledEndTime.get();
    }

    public String getState() {
        return this.state.get();
    }

    public String getReason() {
        return this.reason.get();
    }

    public String getPatientPhoneNumber() {
        return this.patientPhoneNumber.get();
    }

    public String getPatientEmail() {
        return this.patientEmail.get();
    }

    public Long getVersion() {
        return this.version;
    }

    public AgendaDto getAgenda() {
        return this.agenda;
    }

    public PatientCareDto getPatientCare() {
        return this.patientCare;
    }

    public void setSlotsNumber(Long slotsNumber) {
        this.slotsNumber = slotsNumber;
    }

    public void setPatientCare(PatientCareDto patientCare) {
        this.patientCare = patientCare;
    }

    public void setAgenda(AgendaDto agenda) {
        this.agenda = agenda;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate.set(LocalDate.parse(scheduledDate));
    }

    public void setScheduledStartTime(String scheduledStartTime) {
        this.scheduledStartTime.set(scheduledStartTime);
    }

    public void setScheduledEndTime(String scheduledEndTime) {
        this.scheduledEndTime.set(scheduledEndTime);
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber.set(patientPhoneNumber);
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail.setValue(patientEmail);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public UserDto getScheduledBy() {
        return scheduledBy;
    }

    public void setScheduledBy(UserDto scheduledBy) {
        this.scheduledBy = scheduledBy;
    }

}
