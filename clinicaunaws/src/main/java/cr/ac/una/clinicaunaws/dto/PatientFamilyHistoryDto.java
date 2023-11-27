package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Patient;
import cr.ac.una.clinicaunaws.entities.PatientFamilyHistory;
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
@Schema(name = "PatientFamilyHistoryDto", description = "DTO for PatientFamilyHistory entity", requiredProperties = {
        "id", "patient", "disease", "relationship", "version" })
public class PatientFamilyHistoryDto implements DtoMapper<PatientFamilyHistory, PatientFamilyHistoryDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "PatientDto", implementation = PatientDto.class, required = true)
    private PatientDto patient;

    @Size(min = 1, max = 64, message = "The disease must be between 1 and 64 characters")
    @Schema(name = "disease", example = "Diabetes", required = true)
    private String disease;

    @Size(min = 1, max = 64, message = "The relationship must be between 1 and 64 characters")
    @Schema(name = "relationship", example = "Grandmother", required = true)
    private String relationship;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public PatientFamilyHistoryDto convertFromEntityToDTO(PatientFamilyHistory entity, PatientFamilyHistoryDto dto) {
        Patient patientEntity = entity.getPatient();
        if (patientEntity != null) {
            dto.setPatient(new PatientDto(patientEntity));
        }
        return dto;
    }

    @Override
    public PatientFamilyHistory convertFromDTOToEntity(PatientFamilyHistoryDto dto, PatientFamilyHistory entity) {
        entity.setPatient(new Patient(dto.getPatient()));
        return entity;
    }

    public PatientFamilyHistoryDto(PatientFamilyHistory entity) {
        this.id = entity.getId();
        this.disease = entity.getDisease();
        this.relationship = entity.getRelationship();
        this.version = entity.getVersion();
    }

}
