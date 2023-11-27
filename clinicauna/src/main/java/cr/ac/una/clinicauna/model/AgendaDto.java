package cr.ac.una.clinicauna.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class AgendaDto {

    private Long id;
    private DoctorDto doctor;
    private SimpleStringProperty agendaDate;
    private SimpleStringProperty shiftStartTime;
    private SimpleStringProperty shiftEndTime;
    private Long hourlySlots;
    private List<MedicalAppointmentDto> medicalAppointments;
    private Long version;

    public AgendaDto() {
        agendaDate = new SimpleStringProperty();
        shiftStartTime = new SimpleStringProperty();
        shiftEndTime = new SimpleStringProperty();
        medicalAppointments = new ArrayList<>();
    }

    public AgendaDto(AgendaDto agendaDto) {
        this();
        setId(agendaDto.getId());
        setAgendaDate(agendaDto.getAgendaDate());
        setShiftStartTime(agendaDto.getShiftStartTime());
        setShiftEndTime(agendaDto.getShiftEndTime());
        setHourlySlots(agendaDto.getHourlySlots());
        setVersion(agendaDto.getVersion());
    }

    public Long getId() {
        return id;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public String getAgendaDate() {
        return agendaDate.get();
    }

    public String getShiftStartTime() {
        return shiftStartTime.get();
    }

    public String getShiftEndTime() {
        return shiftEndTime.get();
    }

    public Long getHourlySlots() {
        return hourlySlots;
    }

    public Long getVersion() {
        return version;
    }

    public List<MedicalAppointmentDto> getMedicalAppointments() {
        return medicalAppointments;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate.set(agendaDate);
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime.set(shiftStartTime);
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime.set(shiftEndTime);
    }

    public void setHourlySlots(Long hourlySlots) {
        this.hourlySlots = hourlySlots;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setMedicalAppointments(List<MedicalAppointmentDto> medicalAppointments) {
        this.medicalAppointments = medicalAppointments;
    }

}
