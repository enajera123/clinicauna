package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Patient;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.entities.PatientCare;
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
@Schema(name = "PatientCareDto", description = "DTO for PatientCare entity", requiredProperties = { "id",
        "patientCareDate", "patientHistory",
        "bloodPressure", "heartRate", "weight", "height", "temperature", "bodyMassIndex", "bodyMassIndexIdeal",
        "nursingNotes", "reason", "carePlan", "observations", "physicalExam", "treatment", "medicalAppointments",
        "version" })
public class PatientCareDto implements DtoMapper<PatientCare, PatientCareDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "patientCareDate", example = "2021-10-10", required = true)
    private String patientCareDate;

    @Schema(name = "PatientPersonalHistoryDto", implementation = PatientPersonalHistoryDto.class)
    private PatientPersonalHistoryDto patientHistory;

    @Size(min = 1, max = 8, message = "The bloodPressure must be between 1 and 8 characters")
    @Schema(name = "bloodPressure", example = "120/80", required = true)
    private String bloodPressure;

    @Size(min = 1, max = 8, message = "The heartRate must be between 1 and 8 characters")
    @Schema(name = "heartRate", example = "80", required = true)
    private String heartRate;

    @Size(min = 1, max = 8, message = "The weight must be between 1 and 8 characters")
    @Schema(name = "weight", example = "80", required = true)
    private String weight;

    @Size(min = 1, max = 8, message = "The height must be between 1 and 8 characters")
    @Schema(name = "height", example = "1.80", required = true)
    private String height;

    @Size(min = 1, max = 8, message = "The temperature must be between 1 and 8 characters")
    @Schema(name = "temperature", example = "36.5", required = true)
    private String temperature;

    @Size(min = 1, max = 8, message = "The bodyMassIndex must be between 1 and 8 characters")
    @Schema(name = "bodyMassIndex", example = "24.69", required = true)
    private String bodyMassIndex;

    @Size(min = 1, max = 8, message = "The bodyMassIndexIdeal must be between 1 and 8 characters")
    @Schema(name = "bodyMassIndexIdeal", example = "22.5", required = true)
    private String bodyMassIndexIdeal;

    @Size(min = 1, max = 256, message = "The nursingNotes must be between 1 and 256 characters")
    @Schema(name = "nursingNotes", example = "nursingNotes", required = true)
    private String nursingNotes;

    @Size(min = 1, max = 256, message = "The reason must be between 1 and 256 characters")
    @Schema(name = "reason", example = "reason", required = true)
    private String reason;

    @Size(min = 1, max = 256, message = "The carePlan must be between 1 and 256 characters")
    @Schema(name = "carePlan", example = "carePlan", required = true)
    private String carePlan;

    @Size(min = 1, max = 256, message = "The observations must be between 1 and 256 characters")
    @Schema(name = "observations", example = "observations", required = true)
    private String observations;

    @Size(min = 1, max = 256, message = "The physicalExam must be between 1 and 256 characters")
    @Schema(name = "physicalExam", example = "physicalExam", required = true)
    private String physicalExam;

    @Size(min = 1, max = 256, message = "The treatment must be between 1 and 256 characters")
    @Schema(name = "treatment", example = "treatment", required = true)
    private String treatment;

    @Schema(name = "MedicalAppointmentDto", implementation = MedicalAppointmentDto.class)
    private List<MedicalAppointmentDto> medicalAppointments;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public PatientCareDto convertFromEntityToDTO(PatientCare entity, PatientCareDto dto) {
        PatientPersonalHistory personalHistory = entity.getPatientHistory();
        if (personalHistory != null) {
            dto.setPatientHistory(new PatientPersonalHistoryDto(personalHistory));
        }
        dto.setMedicalAppointments(
                DtoMapper.fromEntityList(entity.getMedicalAppointments(), MedicalAppointmentDto.class));
        return dto;
    }

    @Override
    public PatientCare convertFromDTOToEntity(PatientCareDto dto, PatientCare entity) {
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
    public PatientCareDto(PatientCare entity) {
        this.id = entity.getId();
        this.patientCareDate = entity.getPatientCareDate().toString();
        this.bloodPressure = entity.getBloodPressure();
        this.heartRate = entity.getHeartRate();
        this.weight = entity.getWeight();
        this.height = entity.getHeight();
        this.temperature = entity.getTemperature();
        this.bodyMassIndex = entity.getBodyMassIndex();
        this.bodyMassIndexIdeal = entity.getBodyMassIndexIdeal();
        this.nursingNotes = entity.getNursingNotes();
        this.reason = entity.getReason();
        this.carePlan = entity.getCarePlan();
        this.observations = entity.getObservations();
        this.physicalExam = entity.getPhysicalExam();
        this.treatment = entity.getTreatment();
        this.medicalAppointments = new ArrayList<>();
        this.version = entity.getVersion();
    }

}
