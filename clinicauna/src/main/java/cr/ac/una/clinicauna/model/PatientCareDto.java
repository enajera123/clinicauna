package cr.ac.una.clinicauna.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class PatientCareDto {

    private Long id;
    private PatientPersonalHistoryDto patientHistory;
    public SimpleStringProperty patientCareDate;
    public SimpleStringProperty bloodPressure;
    public SimpleStringProperty heartRate;
    public SimpleStringProperty weight;
    public SimpleStringProperty height;
    public SimpleStringProperty temperature;
    public SimpleStringProperty bodyMassIndex;
    private SimpleStringProperty bodyMassIndexIdeal;
    public SimpleStringProperty nursingNotes;
    public SimpleStringProperty reason;
    public SimpleStringProperty carePlan;
    public SimpleStringProperty observations;
    public SimpleStringProperty physicalExam;
    public SimpleStringProperty treatment;
    private List<MedicalAppointmentDto> medicalAppointments;
    private Long version;
    
    public PatientCareDto() {
        bodyMassIndexIdeal = new SimpleStringProperty();
        bloodPressure = new SimpleStringProperty();
        patientCareDate = new SimpleStringProperty();
        heartRate = new SimpleStringProperty();
        weight = new SimpleStringProperty();
        height = new SimpleStringProperty();
        temperature = new SimpleStringProperty();
        bodyMassIndex = new SimpleStringProperty();
        nursingNotes = new SimpleStringProperty();
        reason = new SimpleStringProperty();
        carePlan = new SimpleStringProperty();
        observations = new SimpleStringProperty();
        physicalExam = new SimpleStringProperty();
        treatment = new SimpleStringProperty();
    }

    public PatientCareDto(PatientCareDto patientCareDto) {
        setId(patientCareDto.getId());
        setPatientCareDate(patientCareDto.getPatientCareDate());
        setHeartRate(patientCareDto.getHeartRate());
        setWeight(patientCareDto.getWeight());
        setHeight(patientCareDto.getHeight());
        setTemperature(patientCareDto.getTemperature());
        setBodyMassIndex(patientCareDto.getBodyMassIndex());
        setNursingNotes(patientCareDto.getNursingNotes());
        setReason(patientCareDto.getReason());
        setCarePlan(patientCareDto.getCarePlan());
        setObservations(patientCareDto.getObservations());
        setPhysicalExam(patientCareDto.getPhysicalExam());
        setTreatment(patientCareDto.getTreatment());
        setBloodPressure(patientCareDto.getBloodPressure());
        setBodyMassIndexIdeal(patientCareDto.getBodyMassIndexIdeal());
        setVersion(patientCareDto.getVersion());
    }
    
    public String getBodyMassIndexIdeal() {
        return bodyMassIndexIdeal.get();
    }
    
    public void setBodyMassIndexIdeal(String bodyMassIndexIdeal) {
        this.bodyMassIndexIdeal.set(bodyMassIndexIdeal);
    }
    
    public String getPatientCareDate() {
        return patientCareDate.get();
    }

    public String getHeartRate() {
        return heartRate.get();
    }

    public String getWeight() {
        return weight.get();
    }

    public String getHeight() {
        return height.get();
    }

    public String getTemperature() {
        return temperature.get();
    }

    public String getBodyMassIndex() {
        return bodyMassIndex.get();
    }

    public String getNursingNotes() {
        return nursingNotes.get();
    }

    public String getReason() {
        return reason.get();
    }

    public String getCarePlan() {
        return carePlan.get();
    }

    public String getObservations() {
        return observations.get();
    }

    public String getPhysicalExam() {
        return physicalExam.get();
    }

    public String getTreatment() {
        return treatment.get();
    }

    public Long getId() {
        return id;
    }

    public PatientPersonalHistoryDto getPatientHistory() {
        return patientHistory;
    }

    public String getBloodPressure() {
        return bloodPressure.get();
    }

    public List<MedicalAppointmentDto> getMedicalAppointments() {
        return medicalAppointments;
    }

    public Long getVersion() {
        return version;
    }

    public void setPatientCareDate(String patientCareDate) {
        this.patientCareDate.set(patientCareDate);
    }

    public void setHeartRate(String heartRate) {
        this.heartRate.set(heartRate);
    }

    public void setWeight(String weight) {
        this.weight.set(weight);
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public void setTemperature(String temperature) {
        this.temperature.set(temperature);
    }

    public void setBodyMassIndex(String bodyMassIndex) {
        this.bodyMassIndex.set(bodyMassIndex);
    }

    public void setNursingNotes(String nursingNotes) {
        this.nursingNotes.set(nursingNotes);
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public void setCarePlan(String carePlan) {
        this.carePlan.set(carePlan);
    }

    public void setObservations(String observations) {
        this.observations.set(observations);
    }

    public void setPhysicalExam(String physicalExam) {
        this.physicalExam.set(physicalExam);
    }

    public void setTreatment(String treatment) {
        this.treatment.set(treatment);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientHistory(PatientPersonalHistoryDto patientHistory) {
        this.patientHistory = patientHistory;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure.set(bloodPressure);
    }

    public void setMedicalAppointments(List<MedicalAppointmentDto> medicalAppointments) {
        this.medicalAppointments = medicalAppointments;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
}
