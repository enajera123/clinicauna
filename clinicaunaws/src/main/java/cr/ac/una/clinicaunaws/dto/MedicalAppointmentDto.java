package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Agenda;
import cr.ac.una.clinicaunaws.entities.MedicalAppointment;
import cr.ac.una.clinicaunaws.entities.Patient;
import cr.ac.una.clinicaunaws.entities.PatientCare;
import cr.ac.una.clinicaunaws.entities.User;
import cr.ac.una.clinicaunaws.util.DtoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
@Schema(name = "MedicalAppointmentDto", description = "DTO for MedicalAppointment entity", requiredProperties = { "id",
        "agenda", "patient", "patientCare", "scheduledBy", "slotsNumber", "scheduledDate", "scheduledStartTime",
        "scheduledEndTime", "state", "reason", "patientPhoneNumber", "patientEmail", "version" })
public class MedicalAppointmentDto implements DtoMapper<MedicalAppointment, MedicalAppointmentDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "AgendaDto", implementation = AgendaDto.class)
    private AgendaDto agenda;

    @Schema(name = "PatientDto", implementation = PatientDto.class)
    private PatientDto patient;

    @Schema(name = "PatientCareDto", implementation = PatientCareDto.class)
    private PatientCareDto patientCare;

    @Schema(name = "UserDto", implementation = UserDto.class)
    private UserDto scheduledBy;

    @Schema(name = "slotsNumber", example = "1", required = true)
    private Long slotsNumber;

    @Schema(name = "scheduledDate", example = "2021-10-10", required = true)
    private String scheduledDate;

    @Size(min = 1, max = 5, message = "The scheduledStartTime must be between 1 and 5 characters")
    @Schema(name = "scheduledStartTime", example = "08:00", required = true)
    private String scheduledStartTime;

    @Size(min = 1, max = 5, message = "The scheduledEndTime must be between 1 and 5 characters")
    @Schema(name = "scheduledEndTime", example = "08:30", required = true)
    private String scheduledEndTime;

    @Size(min = 1, max = 9, message = "The state must be between 1 and 9 characters")
    @Schema(name = "state", example = "SCHEDULED", required = true, allowableValues = {
            "SCHEDULED",
            "CANCELLED",
            "ATTENDED",
            "ABSENT" })
    private String state;

    @Size(min = 1, max = 256, message = "The reason must be between 1 and 256 characters")
    @Schema(name = "reason", example = "reason", required = true)
    private String reason;

    @Size(min = 1, max = 16, message = "The patientPhoneNumber must be between 1 and 16 characters")
    @Schema(name = "patientPhoneNumber", example = "12345678", required = true)
    private String patientPhoneNumber;

    @Size(min = 1, max = 128, message = "The patientEmail must be between 1 and 128 characters")
    @Schema(name = "patientEmail", example = "najera@gmail.com", required = true)
    private String patientEmail;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public MedicalAppointmentDto convertFromEntityToDTO(MedicalAppointment entity, MedicalAppointmentDto dto) {

        PatientCare patientCareEntity = entity.getPatientCare();
        User scheduledByEntity = entity.getScheduledBy();
        Patient patientEntity = entity.getPatient();
        Agenda agendaEntity = entity.getAgenda();
        if (agendaEntity != null) {
            dto.setAgenda(new AgendaDto(agendaEntity));
        }
        if (patientEntity != null) {
            dto.setPatient(new PatientDto(patientEntity));
        }
        if (scheduledByEntity != null) {
            dto.setScheduledBy(new UserDto(scheduledByEntity));
        }
        if (patientCareEntity != null) {
            dto.setPatientCare(new PatientCareDto(patientCareEntity));
        }

        return dto;
    }

    @Override
    public MedicalAppointment convertFromDTOToEntity(MedicalAppointmentDto dto, MedicalAppointment entity) {
        if (dto.getAgenda() != null) {
            entity.setAgenda(new Agenda(dto.getAgenda()));
        }
        if (dto.getPatient() != null) {
            entity.setPatient(new Patient(dto.getPatient()));
        }
        if (dto.getScheduledBy() != null) {
            entity.setScheduledBy(new User(dto.getScheduledBy()));
        }
        if (dto.getPatientCare() != null) {
            entity.setPatientCare(new PatientCare(dto.getPatientCare()));
        }
        return entity;
    }

    /**
     * @param entity constructor to dto
     */
    public MedicalAppointmentDto(MedicalAppointment entity) {
        this.id = entity.getId();
        this.slotsNumber = entity.getSlotsNumber();
        this.scheduledDate = entity.getScheduledDate().toString();
        this.scheduledStartTime = entity.getScheduledStartTime();
        this.scheduledEndTime = entity.getScheduledEndTime();
        this.state = entity.getState();
        this.reason = entity.getReason();
        this.patientPhoneNumber = entity.getPatientPhoneNumber();
        this.patientEmail = entity.getPatientEmail();
        this.version = entity.getVersion();
    }

}
