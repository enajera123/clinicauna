package cr.ac.una.clinicaunaws.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import static cr.ac.una.clinicaunaws.util.Database.*;
import java.io.Serializable;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.UserDto;
import lombok.NoArgsConstructor;

/**
 *
 * @author arayaroma
 */
@Entity
@NoArgsConstructor
@Table(name = "TBL_USER", schema = SCHEMA)
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "User.findByUsernameAndPassword", query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "User.findByActivationCode", query = "SELECT u FROM User u WHERE u.activationCode = :activationCode", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), })
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_USER, sequenceName = SCHEMA + "." + SEQ_USER, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_USER)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Doctor doctor;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "USERNAME", unique = true)
    private String username;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "PASSWORD")
    private String password;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "FIRSTLASTNAME")
    private String firstLastname;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "SECONDLASTNAME")
    private String secondLastname;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 9)
    @Column(name = "IDENTIFICATION", unique = true)
    private String identification;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 13)
    @Column(name = "ROLE")
    private String role;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "ISACTIVE")
    private String isActive;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "ISADMIN")
    private String isAdmin;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "PASSWORDCHANGED")
    private String passwordChanged;

    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "ACTIVATIONCODE")
    private String activationCode;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 12)
    @Column(name = "LANGUAGE")
    private String language;

    @Lob
    @Column(name = "PROFILEPHOTO")
    private byte[] profilePhoto;

    @OneToMany(mappedBy = "scheduledBy", fetch = FetchType.LAZY)
    private List<MedicalAppointment> medicalAppointments;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from dto to entity
     */
    public User(UserDto dto) {
        this.id = dto.getId();
        updateUser(dto);
    }

    /**
     * @param dto to be updated
     */
    public void updateUser(UserDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.name = dto.getName();
        this.firstLastname = dto.getFirstLastname();
        this.secondLastname = dto.getSecondLastname();
        this.identification = dto.getIdentification();
        this.email = dto.getEmail();
        this.role = dto.getRole();
        this.phoneNumber = dto.getPhoneNumber();
        this.isActive = dto.getIsActive();
        this.isAdmin = dto.getIsAdmin();
        this.passwordChanged = dto.getPasswordChanged();
        this.activationCode = dto.getActivationCode();
        this.language = dto.getLanguage();
        this.profilePhoto = dto.getProfilePhoto();
        this.version = dto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastname() {
        return firstLastname;
    }

    public void setFirstLastname(String firstLastname) {
        this.firstLastname = firstLastname;
    }

    public String getSecondLastname() {
        return secondLastname;
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname = secondLastname;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(String passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<MedicalAppointment> getMedicalAppointments() {
        return medicalAppointments;
    }

    public void setMedicalAppointments(List<MedicalAppointment> medicalAppointments) {
        this.medicalAppointments = medicalAppointments;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    

}
