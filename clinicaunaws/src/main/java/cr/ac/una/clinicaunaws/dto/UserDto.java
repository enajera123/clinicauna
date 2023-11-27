package cr.ac.una.clinicaunaws.dto;

import cr.ac.una.clinicaunaws.entities.Doctor;
import java.util.List;
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
@Schema(name = "UserDto", description = "DTO for User entity", requiredProperties = { "id", "username", "password",
        "name", "firstLastname", "secondLastname", "identification", "email", "role", "phoneNumber", "isActive",
        "isAdmin", "passwordChanged", "activationCode", "language", "profilePhoto", "medicalAppointments", "token",
        "version" })
public class UserDto implements DtoMapper<User, UserDto> {

    @Schema(name = "id", example = "1", required = true)
    private Long id;

    @Schema(name = "DoctorDto", implementation = DoctorDto.class)
    private DoctorDto doctor;

    @Size(min = 1, max = 20, message = "The username must be between 1 and 20 characters")
    @Schema(name = "username", example = "mr_robot", required = true)
    private String username;

    @Size(min = 1, max = 16, message = "The password must be between 1 and 16 characters")
    @Schema(name = "password", example = "mr_robot", required = true)
    private String password;

    @Size(min = 1, max = 32, message = "The name must be between 1 and 32 characters")
    @Schema(name = "name", example = "Elliot", required = true)
    private String name;

    @Size(min = 1, max = 32, message = "The firstLastname must be between 1 and 32 characters")
    @Schema(name = "firstLastname", example = "Alderson", required = true)
    private String firstLastname;

    @Size(min = 1, max = 32, message = "The secondLastname must be between 1 and 32 characters")
    @Schema(name = "secondLastname", example = "Alderson", required = true)
    private String secondLastname;

    @Size(min = 1, max = 16, message = "The identification must be between 1 and 16 characters")
    @Schema(name = "identification", example = "123456789", required = true)
    private String identification;

    @Size(min = 1, max = 128, message = "The email must be between 1 and 128 characters")
    @Schema(name = "email", example = "elliot@proton.com", required = true)
    private String email;

    @Size(min = 1, max = 13, message = "The role must be between 1 and 13 characters")
    @Schema(name = "role", description = "Different roles within the clinic", example = "ADMINISTRATOR", required = true, allowableValues = {
            "ADMINISTRATOR",
            "DOCTOR",
            "RECEPTIONIST" })
    private String role;

    @Size(min = 1, max = 16, message = "The phoneNumber must be between 1 and 16 characters")
    @Schema(name = "phoneNumber", example = "12345678", required = true)
    private String phoneNumber;

    @Size(min = 1, max = 1, message = "The isActive must be between 1 and 1 characters")
    @Schema(name = "isActive", example = "Y", required = true, allowableValues = { "Y", "N" }, defaultValue = "N")
    private String isActive;

    @Size(min = 1, max = 1, message = "The isAdmin must be between 1 and 1 characters")
    @Schema(name = "isAdmin", example = "Y", required = true, allowableValues = { "Y", "N" }, defaultValue = "N")
    private String isAdmin;

    @Size(min = 1, max = 1, message = "The passwordChanged must be between 1 and 1 characters")
    @Schema(name = "passwordChanged", example = "N", required = true, allowableValues = { "Y",
            "N" }, defaultValue = "N")
    private String passwordChanged;

    @Size(min = 1, max = 100, message = "The activationCode must be between 1 and 100 characters")
    @Schema(name = "activationCode", example = "123456", required = true)
    private String activationCode;

    @Size(min = 1, max = 12, message = "The language must be between 1 and 12 characters")
    @Schema(name = "language", example = "ENGLISH", required = true, allowableValues = { "ENGLISH", "SPANISH" })
    private String language;

    @Schema(name = "profilePhoto")
    private byte[] profilePhoto;

    @Schema(name = "medicalAppointments", implementation = MedicalAppointmentDto.class)
    private List<MedicalAppointmentDto> medicalAppointments;

    @Schema(name = "token", example = "", required = true)
    private String token;

    @Schema(name = "version", example = "1", required = true)
    private Long version;

    /**
     * @param entity Entity to be converted
     * @param dto    DTO to be updated
     * @return DTO with the updated information
     */
    @Override
    public UserDto convertFromEntityToDTO(User entity, UserDto dto) {
        Doctor doctorEntity = entity.getDoctor();
        if (doctorEntity != null) {
            dto.setDoctor(new DoctorDto(doctorEntity));
        }

        return dto;
    }

    /**
     * @param dto    DTO to be converted
     * @param entity Entity to be updated
     * @return Entity with the updated information
     */
    @Override
    public User convertFromDTOToEntity(UserDto dto, User entity) {
        return entity;
    }

    /**
     * @param user constructor from entity to dto
     */
    public UserDto(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.firstLastname = entity.getFirstLastname();
        this.secondLastname = entity.getSecondLastname();
        this.identification = entity.getIdentification();
        this.email = entity.getEmail();
        this.role = entity.getRole();
        this.phoneNumber = entity.getPhoneNumber();
        this.isActive = entity.getIsActive();
        this.isAdmin = entity.getIsAdmin();
        this.passwordChanged = entity.getPasswordChanged();
        this.activationCode = entity.getActivationCode();
        this.language = entity.getLanguage();
        this.profilePhoto = entity.getProfilePhoto();
        this.version = entity.getVersion();
        this.medicalAppointments = new ArrayList<>();
    }
}
