package cr.ac.una.clinicauna.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author estebannajera
 */
public class PatientFamilyHistoryDto {

    private Long id;
    private PatientDto patient;
    private SimpleStringProperty disease;
    private SimpleStringProperty relationship;
    private Long version;

    public PatientFamilyHistoryDto() {
        disease = new SimpleStringProperty();
        relationship = new SimpleStringProperty();
    }

    public String getDisease() {
        return disease.get();
    }

    public Long getId() {
        return id;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public String getRelationship() {
        return relationship.get();
    }

    public Long getVersion() {
        return version;
    }

    public void setDisease(String disease) {
        this.disease.set(disease);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public void setRelationship(String relationship) {
        this.relationship.set(relationship);
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
