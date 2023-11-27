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
@Schema(name = "PatientPersonalHistoryDto", description = "DTO for PatientPersonalHistory entity", requiredProperties = {
        "id", "patient", "pathological", "hospitalizations", "surgical", "allergies", "treatments", "medicalExams",
        "patientCares", "version" })
public class PatientPersonalHistoryDto implements DtoMapper<PatientPersonalHistory, PatientPersonalHistoryDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "PatientDto", implementation = PatientDto.class, required = true)
    private PatientDto patient;

    @Size(min = 1, max = 256, message = "The pathological must be between 1 and 256 characters")
    @Schema(name = "pathological", example = "Diabetes", required = true)
    private String pathological;

    @Size(min = 1, max = 256, message = "The hospitalizations must be between 1 and 256 characters")
    @Schema(name = "hospitalizations", example = "Diabetes", required = true)
    private String hospitalizations;

    @Size(min = 1, max = 256, message = "The surgical must be between 1 and 256 characters")
    @Schema(name = "surgical", example = "Diabetes", required = true)
    private String surgical;

    @Size(min = 1, max = 256, message = "The allergies must be between 1 and 256 characters")
    @Schema(name = "allergies", example = "Diabetes", required = true)
    private String allergies;

    @Size(min = 1, max = 256, message = "The treatments must be between 1 and 256 characters")
    @Schema(name = "treatments", example = "Diabetes", required = true)
    private String treatments;

    @Schema(name = "MedicalExamDto", implementation = MedicalExamDto.class, required = true)
    private List<MedicalExamDto> medicalExams;

    @Schema(name = "PatientCareDto", implementation = PatientCareDto.class, required = true)
    private List<PatientCareDto> patientCares;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public PatientPersonalHistoryDto convertFromEntityToDTO(PatientPersonalHistory entity,
            PatientPersonalHistoryDto dto) {
        PatientDto patientDto = new PatientDto(entity.getPatient());
        if (patientDto != null) {
            dto.setPatient(patientDto);
        }
        dto.setMedicalExams(DtoMapper.fromEntityList(entity.getMedicalExams(), MedicalExamDto.class));
        dto.setPatientCares(DtoMapper.fromEntityList(entity.getPatientCares(), PatientCareDto.class));
        return dto;
    }

    @Override
    public PatientPersonalHistory convertFromDTOToEntity(PatientPersonalHistoryDto dto, PatientPersonalHistory entity) {
        if (dto.getPatient() != null) {
            entity.setPatient(new Patient(dto.getPatient()));
        }
        return entity;
    }

    public PatientPersonalHistoryDto(PatientPersonalHistory entity) {
        this.id = entity.getId();
        this.pathological = entity.getPathological();
        this.hospitalizations = entity.getHospitalizations();
        this.surgical = entity.getSurgical();
        this.allergies = entity.getAllergies();
        this.treatments = entity.getTreatments();
        this.version = entity.getVersion();
        this.medicalExams = new ArrayList<>();
        this.patientCares = new ArrayList<>();
    }

}
