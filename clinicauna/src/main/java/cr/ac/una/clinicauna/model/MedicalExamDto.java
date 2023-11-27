package cr.ac.una.clinicauna.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author estebannajera
 */
public class MedicalExamDto {

    private Long id;
    private PatientPersonalHistoryDto patientHistory;
    public SimpleStringProperty name;
    public SimpleStringProperty medicalExamDate;
    public SimpleStringProperty notes;
    private Long version;

    public MedicalExamDto() {
        name = new SimpleStringProperty();
        medicalExamDate = new SimpleStringProperty();
        notes = new SimpleStringProperty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientPersonalHistoryDto getPatientHistory() {
        return patientHistory;
    }

    public void setPatientHistory(PatientPersonalHistoryDto patientHistory) {
        this.patientHistory = patientHistory;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getMedicalExamDate() {
        return medicalExamDate.get();
    }

    public void setMedicalExamDate(String medicalExamDate) {
        this.medicalExamDate.set(medicalExamDate);
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
