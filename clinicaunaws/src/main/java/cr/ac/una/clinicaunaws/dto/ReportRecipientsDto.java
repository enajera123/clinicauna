package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Report;
import cr.ac.una.clinicaunaws.entities.ReportRecipients;
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
@Schema(name = "ReportRecipientsDto", description = "DTO for ReportRecipients entity", requiredProperties = {
        "id", "report", "email", "version" })
public class ReportRecipientsDto implements DtoMapper<ReportRecipients, ReportRecipientsDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "ReportDto", implementation = ReportDto.class, required = true)
    private ReportDto report;

    @Size(min = 1, max = 64, message = "The email must be between 1 and 64 characters")
    @Schema(name = "email", example = "darayaroma@gmail.com", required = true)
    private String email;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public ReportRecipientsDto convertFromEntityToDTO(ReportRecipients entity, ReportRecipientsDto dto) {
        dto.setReport(new ReportDto(entity.getReport()));
        return dto;
    }

    @Override
    public ReportRecipients convertFromDTOToEntity(ReportRecipientsDto dto, ReportRecipients entity) {
        entity.setReport(new Report(dto.getReport()));
        return entity;
    }

    /**
     * Constructor from entity
     * 
     * @param entity the entity to convert to DTO
     */
    public ReportRecipientsDto(ReportRecipients entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.version = entity.getVersion();
    }

}
