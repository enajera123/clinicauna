package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Report;
import cr.ac.una.clinicaunaws.entities.ReportParameters;
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
@Schema(name = "ReportParametersDto", description = "DTO for ReportParameters entity", requiredProperties = {
        "id", "report", "name", "value", "version" })
public class ReportParametersDto implements DtoMapper<ReportParameters, ReportParametersDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "ReportDto", implementation = ReportDto.class, required = true)
    private ReportDto report;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "patientId", required = true)
    private String name;

    @Size(min = 1, max = 256, message = "The value must be between 1 and 256 characters")
    @Schema(name = "value", example = "1", required = true)
    private String value;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public ReportParametersDto convertFromEntityToDTO(ReportParameters entity, ReportParametersDto dto) {
        dto.setReport(new ReportDto(entity.getReport()));
        return dto;
    }

    @Override
    public ReportParameters convertFromDTOToEntity(ReportParametersDto dto, ReportParameters entity) {
        entity.setReport(new Report(dto.getReport()));
        return entity;
    }

    /**
     * Constructor from entity
     * 
     * @param entity the entity to convert to DTO
     */
    public ReportParametersDto(ReportParameters entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.value = entity.getValue();
        this.version = entity.getVersion();
    }

}
