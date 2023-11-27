package cr.ac.una.clinicauna.model;

import cr.ac.una.clinicauna.util.DtoMapper;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author enajera
 */
public class DoctorDto implements DtoMapper<DoctorDto, DoctorDto> {

    public Long id;
    public UserDto user;
    public SimpleStringProperty code;
    public SimpleStringProperty idCard;
    public SimpleStringProperty shiftStartTime;
    public SimpleStringProperty shiftEndTime;
    public SimpleStringProperty hourlySlots;
    private List<AgendaDto> agendas;
    public Long version;

    public DoctorDto() {
        agendas = new ArrayList<>();
        code = new SimpleStringProperty();
        idCard = new SimpleStringProperty();
        shiftStartTime = new SimpleStringProperty();
        shiftEndTime = new SimpleStringProperty();
        hourlySlots = new SimpleStringProperty();

    }

    public UserDto getUser() {
        return user;
    }

    public void setAgendas(List<AgendaDto> agendas) {
        this.agendas = agendas;
    }

    public List<AgendaDto> getAgendas() {
        return agendas;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code.get();
    }

    public Long getIdCard() {
        return Long.valueOf(idCard.get());
    }

    public String getShiftStartTime() {
        return shiftStartTime.get();
    }

    public String getShiftEndTime() {
        return shiftEndTime.get();
    }

    public Long getHourlySlots() {
        return Long.valueOf(hourlySlots.get());
    }

    public Long getVersion() {
        return version;
    }

    // All setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setIdCard(Long idCard) {
        if (idCard == null) {
            idCard = Long.valueOf(0);
        }
        this.idCard.set(idCard.toString());
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime.set(shiftStartTime);
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime.set(shiftEndTime);
    }

    public void setHourlySlots(Long hourlySlots) {
        if (hourlySlots == null) {
            hourlySlots = Long.valueOf(0);
        }
        this.hourlySlots.set(hourlySlots.toString());
    }

    public void setVersion(Long version) {

        this.version = version;
    }

    @Override
    public DoctorDto convertFromGeneratedToDTO(DoctorDto generated, DoctorDto dto) {

        return dto;
    }

    @Override
    public DoctorDto convertFromDTOToGenerated(DoctorDto dto, DoctorDto generated) {

        return generated;
    }

}
