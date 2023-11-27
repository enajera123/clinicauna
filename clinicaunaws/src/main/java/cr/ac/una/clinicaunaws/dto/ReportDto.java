package cr.ac.una.clinicaunaws.dto;

import java.util.List;
import cr.ac.una.clinicaunaws.entities.Report;
import cr.ac.una.clinicaunaws.util.DtoMapper;
import cr.ac.una.clinicaunaws.util.QueryManager;
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
@Schema(name = "ReportDto", description = "DTO for Report entity", requiredProperties = {
        "id", "name", "description", "query", "reportDate", "frequency", "reportParameters", "reportRecipients",
        "version" })
public class ReportDto implements DtoMapper<Report, ReportDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "Patient report", required = true)
    private String name;

    @Size(min = 1, max = 256, message = "The description must be between 1 and 256 characters")
    @Schema(name = "description", example = "Detail information of patients", required = true)
    private String description;

    @Schema(name = "query", example = "SELECT * FROM TBL_PATIENT", required = true)
    private String query;

    @Schema(name = "reportDate", example = "2021-10-10", required = true)
    private String reportDate;

    @Size(min = 1, max = 32, message = "The frequency must be between 1 and 32 characters")
    @Schema(name = "frequency", example = "ONCE", required = true, allowableValues = {
            "ONCE",
            "DAILY",
            "WEEKLY",
            "MONTHLY",
            "YEARLY" })
    private String frequency;

    @Schema(name = "ReportParametersDto", implementation = ReportParametersDto.class, required = true)
    private List<ReportParametersDto> reportParameters;

    @Schema(name = "ReportRecipientsDto", implementation = ReportRecipientsDto.class, required = true)
    private List<ReportRecipientsDto> reportRecipients;

    @Schema(name = "QueryManager", implementation = QueryManager.class, required = true)
    private QueryManager<?> queryManager = new QueryManager<>();

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public ReportDto convertFromEntityToDTO(Report entity, ReportDto dto) {
        dto.setReportParameters(DtoMapper.fromEntityList(entity.getReportParameters(), ReportParametersDto.class));
        dto.setReportRecipients(DtoMapper.fromEntityList(entity.getReportRecipients(), ReportRecipientsDto.class));
        return dto;
    }

    @Override
    public Report convertFromDTOToEntity(ReportDto dto, Report entity) {
        return entity;
    }

    /**
     * @param entity constructor from entity to dto
     */
    public ReportDto(Report entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.query = entity.getQuery();
        this.reportDate = entity.getReportDate().toString();
        this.frequency = entity.getFrequency();
        this.version = entity.getVersion();
        this.reportParameters = new ArrayList<>();
        this.reportRecipients = new ArrayList<>();
    }

}
