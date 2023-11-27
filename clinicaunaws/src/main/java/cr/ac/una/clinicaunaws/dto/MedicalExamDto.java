package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.MedicalExam;
import cr.ac.una.clinicaunaws.entities.Patient;
import cr.ac.una.clinicaunaws.entities.PatientPersonalHistory;
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
@Schema(name = "MedicalExamDto", description = "DTO for MedicalExam entity", requiredProperties = { "id",
        "patientHistory", "name",
        "medicalExamDate", "notes", "version" })
public class MedicalExamDto implements DtoMapper<MedicalExam, MedicalExamDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "PatientPersonalHistoryDto", implementation = PatientPersonalHistoryDto.class)
    private PatientPersonalHistoryDto patientHistory;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "Heart Exam", required = true)
    private String name;

    @Schema(name = "medicalExamDate", example = "2021-10-10", required = true)
    private String medicalExamDate;

    @Size(min = 1, max = 256, message = "The notes must be between 1 and 256 characters")
    @Schema(name = "notes", example = "notes", required = true)
    private String notes;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public MedicalExamDto convertFromEntityToDTO(MedicalExam entity, MedicalExamDto dto) {
        dto.setPatientHistory(new PatientPersonalHistoryDto(entity.getPatientHistory()));
        dto.getPatientHistory().setPatient(new PatientDto(entity.getPatientHistory().getPatient()));
        return dto;
    }

    @Override
    public MedicalExam convertFromDTOToEntity(MedicalExamDto dto, MedicalExam entity) {
        if (dto.getPatientHistory() != null) {
            entity.setPatientHistory(new PatientPersonalHistory(dto.getPatientHistory()));
            if (dto.getPatientHistory().getPatient() != null) {
                entity.getPatientHistory().setPatient(new Patient(dto.getPatientHistory().getPatient()));
            }
        }

        return entity;
    }

    /**
     * @param entity constructor to dto
     */
    public MedicalExamDto(MedicalExam entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.medicalExamDate = entity.getMedicalExamDate().toString();
        this.notes = entity.getNotes();
        this.version = entity.getVersion();
    }

}
