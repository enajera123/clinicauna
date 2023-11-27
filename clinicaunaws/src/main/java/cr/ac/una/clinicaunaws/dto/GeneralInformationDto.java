package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.GeneralInformation;
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
@Schema(name = "GeneralInformationDto", description = "DTO for GeneralInformation entity", requiredProperties = { "id",
        "name", "email",
        "photo", "htmltemplate", "version" })
public class GeneralInformationDto implements DtoMapper<GeneralInformation, GeneralInformationDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "Clinicauna", required = true)
    private String name;

    @Size(min = 1, max = 64, message = "The email must be between 1 and 64 characters")
    @Schema(name = "email", example = "clinicaunaws@gmail.com", required = true)
    private String email;

    @Schema(name = "photo", required = true)
    private byte[] photo;

    @Schema(name = "htmltemplate", example = "htmltemplate", required = true)
    private String htmltemplate;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    /**
     * @param entity the entity to be converted
     * @param dto    the dto to be updated
     * @return the dto with the updated information
     */
    @Override
    public GeneralInformationDto convertFromEntityToDTO(GeneralInformation entity, GeneralInformationDto dto) {
        return dto;
    }

    /**
     * @param dto    the dto to be converted
     * @param entity the entity to be updated
     * @return the entity with the updated information
     */
    @Override
    public GeneralInformation convertFromDTOToEntity(GeneralInformationDto dto, GeneralInformation entity) {
        return entity;
    }

    /**
     * @param generalInformation constructor to convert an entity to a dto
     */
    public GeneralInformationDto(GeneralInformation generalInformation) {
        this.id = generalInformation.getId();
        this.name = generalInformation.getName();
        this.email = generalInformation.getEmail();
        this.photo = generalInformation.getPhoto();
        this.htmltemplate = generalInformation.getHtmltemplate();
        this.version = generalInformation.getVersion();
    }

}