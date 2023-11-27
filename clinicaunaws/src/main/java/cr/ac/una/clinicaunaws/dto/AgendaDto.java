package cr.ac.una.clinicaunaws.dto;

import java.util.List;
import cr.ac.una.clinicaunaws.entities.Agenda;
import cr.ac.una.clinicaunaws.entities.Doctor;
import cr.ac.una.clinicaunaws.entities.MedicalAppointment;
import cr.ac.una.clinicaunaws.entities.User;
import cr.ac.una.clinicaunaws.util.DtoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author arayaroma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AgendaDto", description = "DTO for Agenda entity", requiredProperties = { "id", "doctor",
        "agendaDate", "shiftStartTime", "shiftEndTime", "hourlySlots", "medicalAppointments", "version" })
public class AgendaDto implements DtoMapper<Agenda, AgendaDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "DoctorDto", implementation = DoctorDto.class, required = true)
    private DoctorDto doctor;

    @Schema(name = "agendaDate", example = "2021-10-10", required = true)
    private String agendaDate;

    @Size(min = 1, max = 5, message = "The shiftStartTime must be between 1 and 5 characters")
    @Schema(name = "shiftStartTime", example = "08:00", required = true)
    private String shiftStartTime;

    @Size(min = 1, max = 5, message = "The shiftEndTime must be between 1 and 5 characters")
    @Schema(name = "shiftEndTime", example = "17:00", required = true)
    private String shiftEndTime;

    @Schema(name = "hourlySlots", example = "3", required = true)
    private Long hourlySlots;

    @Schema(name = "MedicalAppointmentDto", implementation = MedicalAppointmentDto.class, required = true)
    private List<MedicalAppointmentDto> medicalAppointments;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public AgendaDto convertFromEntityToDTO(Agenda entity, AgendaDto dto) {
        Doctor doctorEntity = entity.getDoctor();
        if (doctorEntity != null) {
            dto.setDoctor(new DoctorDto(doctorEntity));
            User userEntity = doctorEntity.getUser();
            if (userEntity != null) {
                dto.getDoctor().setUser(new UserDto(userEntity));
            }
        }
        List<MedicalAppointment> appointments = entity.getMedicalAppointments();
        dto.setMedicalAppointments(DtoMapper.fromEntityList(appointments, MedicalAppointmentDto.class));
        if (dto.getMedicalAppointments() != null) {
            for (int i = 0; i < appointments.size(); i++) {
                dto.getMedicalAppointments().get(i).setScheduledBy(new UserDto(appointments.get(i).getScheduledBy()));
                dto.getMedicalAppointments().get(i).setAgenda(new AgendaDto(appointments.get(i).getAgenda()));
                dto.getMedicalAppointments().get(i).setPatient(new PatientDto(appointments.get(i).getPatient()));
            }
        }
        return dto;
    }

    @Override
    public Agenda convertFromDTOToEntity(AgendaDto dto, Agenda entity) {
        if (dto.getDoctor() != null) {
            entity.setDoctor(new Doctor(dto.getDoctor()));
            if (dto.getDoctor().getUser() != null) {
                entity.getDoctor().setUser(new User(dto.getDoctor().getUser()));
            }
        }
        return entity;
    }

    /**
     * @param agenda constructor from entity to dto
     */
    public AgendaDto(Agenda entity) {
        this.id = entity.getId();
        this.agendaDate = entity.getAgendaDate().toString();
        this.shiftStartTime = entity.getShiftStartTime();
        this.shiftEndTime = entity.getShiftEndTime();
        this.hourlySlots = entity.getHourlySlots();
        this.version = entity.getVersion();
        this.medicalAppointments = new ArrayList<>();
    }

}
