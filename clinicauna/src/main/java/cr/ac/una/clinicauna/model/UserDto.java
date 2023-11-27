package cr.ac.una.clinicauna.model;

import cr.ac.una.clinicauna.util.DtoMapper;
import cr.ac.una.clinicauna.util.QueryManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author estebannajera
 * @author arayaroma
 */
public class UserDto implements DtoMapper<UserDto, UserDto> {

    private Long id;
    private DoctorDto doctor;
    public SimpleStringProperty username;
    public SimpleStringProperty password;
    public SimpleStringProperty name;
    public SimpleStringProperty firstLastname;
    public SimpleStringProperty secondLastname;
    public SimpleStringProperty identification;
    public SimpleStringProperty email;
    public ObjectProperty<String> role;
    public SimpleStringProperty phoneNumber;
    public SimpleStringProperty isActive;
    public SimpleStringProperty isAdmin;
    public SimpleStringProperty passwordChanged;
    public SimpleStringProperty activationCode;
    public SimpleStringProperty language;
    private byte[] profilePhoto;
    private String token;
    private QueryManager<?> queryManager = new QueryManager<>();
    private Long version;

    @Override
    public UserDto convertFromGeneratedToDTO(UserDto generated, UserDto dto) {
        return dto;
    }

    @Override
    public UserDto convertFromDTOToGenerated(UserDto dto, UserDto generated) {
        dto.parseLanguage(generated.getLanguage());
        dto.parseRole(generated.getRole());
        return dto;
    }

    public UserDto() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.firstLastname = new SimpleStringProperty();
        this.secondLastname = new SimpleStringProperty();
        this.identification = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.role = new SimpleObjectProperty<>();
        this.phoneNumber = new SimpleStringProperty();
        this.isActive = new SimpleStringProperty();
        this.isAdmin = new SimpleStringProperty();
        this.passwordChanged = new SimpleStringProperty();
        this.activationCode = new SimpleStringProperty();
        this.language = new SimpleStringProperty();
    }

    public UserDto(UserDto userDto) {
        this();
        setId(userDto.getId());
        setUsername(userDto.getUsername());
        setPassword(userDto.getPassword());
        setName(userDto.getName());
        setFirstLastname(userDto.getFirstLastname());
        setSecondLastname(userDto.getSecondLastname());
        setIdentification(userDto.getIdentification());
        setEmail(userDto.getEmail());
        setRole(userDto.getRole());
        setPhoneNumber(userDto.getPhoneNumber());
        setIsActive(userDto.getIsActive());
        setIsAdmin(userDto.getIsAdmin());
        setPasswordChanged(userDto.getPasswordChanged());
        setActivationCode(userDto.getActivationCode());
        setLanguage(userDto.getLanguage());
        setProfilePhoto(userDto.getProfilePhoto());
        setToken(userDto.getToken());
        setVersion(userDto.getVersion());
    }

    public Long getId() {
        return id;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public String getUsername() {
        return this.username.getValue();
    }

    public String getPassword() {
        return this.password.getValue();
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getFirstLastname() {
        return this.firstLastname.getValue();
    }

    public String getSecondLastname() {
        return this.secondLastname.getValue();
    }

    public String getIdentification() {
        return this.identification.getValue();
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public String getRole() {
        return this.role.getValue();
    }

    public String getPhoneNumber() {
        return this.phoneNumber.getValue();
    }

    public String getIsActive() {
        return this.isActive.getValue();
    }

    public String getIsAdmin() {
        return this.isAdmin.getValue();
    }

    public String getPasswordChanged() {
        return this.passwordChanged.getValue();
    }

    public String getActivationCode() {
        return this.activationCode.getValue();
    }

    public String getLanguage() {
        return this.language.getValue();
    }

    public byte[] getProfilePhoto() {
        return this.profilePhoto;
    }

    public String getToken() {
        return this.token;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setFirstLastname(String firstLastname) {
        this.firstLastname.set(firstLastname);
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname.set(secondLastname);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public void setIsActive(String isActive) {
        this.isActive.set(isActive);
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin.set(isAdmin);
    }

    public void setPasswordChanged(String passwordChanged) {
        this.passwordChanged.set(passwordChanged);
    }

    public void setActivationCode(String activationCode) {
        this.activationCode.set(activationCode);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setIdentification(String identification) {
        this.identification.set(identification);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public QueryManager<?> getQueryManager() {
        return this.queryManager;
    }

    public void setQueryManager(QueryManager<?> queryManager) {
        this.queryManager = queryManager;
    }

    public String parseLanguage(String language) {
        switch (language) {
            case "Inglés":
                return "ENGLISH";
            case "Español":
                return "SPANISH";
            case "English":
                return "ENGLISH";
            case "Spanish":
                return "SPANISH";
        }
        return "SPANISH";
    }

    public String parseRole(String role) {
        switch (role) {
            case "administrador":
                setIsAdmin("Y");
                return "ADMINISTRATOR";
            case "administrator":
                setIsAdmin("Y");
                return "ADMINISTRATOR";
            case "doctor":
                setIsAdmin("N");
                return "DOCTOR";
            case "recepcionista":
                setIsAdmin("N");
                return "RECEPCIONIST";
            case "recepcionist":
                setIsAdmin("N");
                return "RECEPCIONIST";
        }
        return "RECEPCIONIST";
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", doctor='" + getDoctor() + "'" +
                ", username='" + getUsername() + "'" +
                ", password='" + getPassword() + "'" +
                ", name='" + getName() + "'" +
                ", firstLastname='" + getFirstLastname() + "'" +
                ", secondLastname='" + getSecondLastname() + "'" +
                ", identification='" + getIdentification() + "'" +
                ", email='" + getEmail() + "'" +
                ", role='" + getRole() + "'" +
                ", phoneNumber='" + getPhoneNumber() + "'" +
                ", isActive='" + getIsActive() + "'" +
                ", isAdmin='" + getIsAdmin() + "'" +
                ", passwordChanged='" + getPasswordChanged() + "'" +
                ", activationCode='" + getActivationCode() + "'" +
                ", language='" + getLanguage() + "'" +
                ", profilePhoto='" + getProfilePhoto() + "'" +
                ", token='" + getToken() + "'" +
                ", version='" + getVersion() + "'" +
                "}";
    }
}
