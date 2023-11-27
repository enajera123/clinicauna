package cr.ac.una.clinicaunaws.dto;

import java.util.List;
import cr.ac.una.clinicaunaws.entities.Patient;
import cr.ac.una.clinicaunaws.entities.PatientPersonalHistory;
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
@Schema(name = "PatientDto", description = "DTO for Patient entity", requiredProperties = { "id", "name",
        "firstLastname", "secondLastname", "identification", "phoneNumber", "email", "gender", "birthDate",
        "language", "patientPersonalHistory", "patientFamilyHistories", "medicalAppointments", "version" })
public class PatientDto implements DtoMapper<Patient, PatientDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "Sebas", required = true)
    private String name;

    @Size(min = 1, max = 32, message = "The firstLastname must be between 1 and 32 characters")
    @Schema(name = "firstLastname", example = "Vargas", required = true)
    private String firstLastname;

    @Size(min = 1, max = 32, message = "The secondLastname must be between 1 and 32 characters")
    @Schema(name = "secondLastname", example = "Elizondo", required = true)
    private String secondLastname;

    @Size(min = 1, max = 16, message = "The identification must be between 1 and 16 characters")
    @Schema(name = "identification", example = "123456789", required = true)
    private String identification;

    @Size(min = 1, max = 16, message = "The phoneNumber must be between 1 and 16 characters")
    @Schema(name = "phoneNumber", example = "123456789", required = true)
    private String phoneNumber;

    @Size(min = 1, max = 128, message = "The email must be between 1 and 128 characters")
    @Schema(name = "email", example = "sebas@gmail.com", required = true)
    private String email;

    @Size(min = 1, max = 6, message = "The gender must be between 1 and 6 characters")
    @Schema(name = "gender", example = "MALE", required = true, allowableValues = { "MALE", "FEMALE" })
    private String gender;

    @Schema(name = "birthDate", example = "2021-09-01", required = true)
    private String birthDate;

    @Size(min = 1, max = 12, message = "The language must be between 1 and 12 characters")
    @Schema(name = "language", example = "ENGLISH", required = true)
    private String language;

    @Schema(name = "patientPersonalHistoryDto", implementation = PatientPersonalHistoryDto.class, required = true)
    private PatientPersonalHistoryDto patientPersonalHistory;

    @Schema(name = "patientFamilyHistories", implementation = PatientFamilyHistoryDto.class, required = true)
    private List<PatientFamilyHistoryDto> patientFamilyHistories;

    @Schema(name = "medicalAppointments", implementation = MedicalAppointmentDto.class, required = true)
    private List<MedicalAppointmentDto> medicalAppointments;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public PatientDto convertFromEntityToDTO(Patient entity, PatientDto dto) {
        PatientPersonalHistory personalHistory = entity.getPatientPersonalHistory();
        if (personalHistory != null) {
            dto.setPatientPersonalHistory(new PatientPersonalHistoryDto(personalHistory));
        }

        dto.setPatientFamilyHistories(
                DtoMapper.fromEntityList(entity.getPatientFamilyHistories(), PatientFamilyHistoryDto.class));
        dto.setMedicalAppointments(
                DtoMapper.fromEntityList(entity.getMedicalAppointments(), MedicalAppointmentDto.class));
        return dto;
    }

    @Override
    public Patient convertFromDTOToEntity(PatientDto dto, Patient entity) {
        return entity;
    }

    /**
     * @param patient constructor from entity to dto
     */
    public PatientDto(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.firstLastname = patient.getFirstLastname();
        this.secondLastname = patient.getSecondLastname();
        this.identification = patient.getIdentification();
        this.phoneNumber = patient.getPhoneNumber();
        this.language = patient.getLanguage();
        this.email = patient.getEmail();
        this.gender = patient.getGender();
        this.birthDate = patient.getBirthDate().toString();
        this.medicalAppointments = new ArrayList<>();
        this.patientFamilyHistories = new ArrayList<>();
        this.version = patient.getVersion();
    }

}
