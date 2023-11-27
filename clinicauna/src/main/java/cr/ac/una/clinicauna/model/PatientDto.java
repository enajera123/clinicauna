package cr.ac.una.clinicauna.model;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PatientDto {

    public Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty firstLastname;
    public SimpleStringProperty secondLastname;
    public SimpleStringProperty identification;
    public SimpleStringProperty phoneNumber;
    public SimpleStringProperty email;
    public SimpleStringProperty gender;
    private SimpleStringProperty language;
    public ObjectProperty<LocalDate> birthDate;
    private PatientPersonalHistoryDto patientPersonalHistory;
    private List<PatientFamilyHistoryDto> patientFamilyHistories;
    public Long version;

    public PatientDto() {
        name = new SimpleStringProperty();
        language = new SimpleStringProperty();
        firstLastname = new SimpleStringProperty();
        secondLastname = new SimpleStringProperty();
        identification = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        email = new SimpleStringProperty();
        gender = new SimpleStringProperty();
        birthDate = new SimpleObjectProperty<>();
    }

    public PatientDto(PatientDto patientDto) {
        this();
        setId(patientDto.getId());
        setVersion(patientDto.getVersion());
        setName(patientDto.getName());
        setFirstLastname(patientDto.getFirstLastname());
        setSecondLastname(patientDto.getSecondLastname());
        setIdentification(patientDto.getIdentification());
        setPhoneNumber(patientDto.getPhoneNumber());
        setEmail(patientDto.getEmail());
        setGender(patientDto.getGender());
        setBirthDate(patientDto.getBirthDate());
    }

    public List<PatientFamilyHistoryDto> getPatientFamilyHistories() {
        return patientFamilyHistories;
    }

    public void setPatientFamilyHistories(List<PatientFamilyHistoryDto> patientFamilyHistories) {
        this.patientFamilyHistories = patientFamilyHistories;
    }

    public PatientPersonalHistoryDto getPatientPersonalHistory() {
        return patientPersonalHistory;
    }

    public void setPatientPersonalHistory(PatientPersonalHistoryDto patientPersonalHistoryDto) {
        this.patientPersonalHistory = patientPersonalHistoryDto;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getLanguage() {
        return language.get();
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public String getFirstLastname() {
        return firstLastname.get();
    }

    public String getSecondLastname() {
        return secondLastname.get();
    }

    public String getIdentification() {
        return identification.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getBirthDate() {
        return birthDate.get().toString();
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

    public void setIdentification(String identification) {
        this.identification.set(identification);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(LocalDate.parse(birthDate));
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
