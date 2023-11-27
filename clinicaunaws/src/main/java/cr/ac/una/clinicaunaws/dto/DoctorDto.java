package cr.ac.una.clinicaunaws.dto;

import java.util.List;
import cr.ac.una.clinicaunaws.entities.Doctor;
import cr.ac.una.clinicaunaws.entities.User;
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
@Schema(name = "DoctorDto", description = "DTO for Doctor entity", requiredProperties = { "id", "user", "code",
        "idCard", "shiftStartTime", "shiftEndTime", "hourlySlots", "agendas", "version" })
public class DoctorDto implements DtoMapper<Doctor, DoctorDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "UserDto", implementation = UserDto.class)
    private UserDto user;

    @Size(min = 1, max = 10, message = "The code must be between 1 and 10 characters")
    @Schema(name = "code", example = "123456789", required = true)
    private String code;

    @Schema(name = "idCard", example = "123456789", required = true)
    private Long idCard;

    @Size(min = 1, max = 5, message = "The shiftStartTime must be between 1 and 5 characters")
    @Schema(name = "shiftStartTime", example = "08:00", required = true)
    private String shiftStartTime;

    @Size(min = 1, max = 5, message = "The shiftEndTime must be between 1 and 5 characters")
    @Schema(name = "shiftEndTime", example = "17:00", required = true)
    private String shiftEndTime;

    @Schema(name = "hourlySlots", example = "3", required = true)
    private Long hourlySlots;

    @Schema(name = "AgendaDto", implementation = AgendaDto.class)
    private List<AgendaDto> agendas;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    @Override
    public DoctorDto convertFromEntityToDTO(Doctor entity, DoctorDto dto) {
        User userEntity = entity.getUser();
        if (userEntity != null) {
            dto.setUser(new UserDto(userEntity));
        }
        dto.setAgendas(DtoMapper.fromEntityList(entity.getAgendas(), AgendaDto.class));
        return dto;
    }

    @Override
    public Doctor convertFromDTOToEntity(DoctorDto dto, Doctor entity) {
        if (dto.getUser() != null) {
            entity.setUser(new User(dto.getUser()));
        }
        return entity;
    }

    /**
     * @param doctor constructor from entity to dto
     */
    public DoctorDto(Doctor entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.idCard = entity.getIdCard();
        this.shiftStartTime = entity.getShiftStartTime();
        this.shiftEndTime = entity.getShiftEndTime();
        this.hourlySlots = entity.getHourlySlots();
        this.version = entity.getVersion();
        this.agendas = new ArrayList<>();
    }

}
