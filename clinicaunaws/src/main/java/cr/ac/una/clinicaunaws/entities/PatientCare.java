package cr.ac.una.clinicaunaws.entities;

import static cr.ac.una.clinicaunaws.util.Database.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.PatientCareDto;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

/**
 *
 * @author arayaroma
 */
@Entity
@NoArgsConstructor
@Table(name = "TBL_PATIENT_CARE", schema = SCHEMA)
@NamedQueries({
        @NamedQuery(name = "PatientCare.findAll", query = "SELECT p FROM PatientCare p", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "PatientCare.findById", query = "SELECT p FROM PatientCare p WHERE p.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), })
public class PatientCare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_PATIENT_CARE, sequenceName = SCHEMA + "."
            + SEQ_PATIENT_CARE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_PATIENT_CARE)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Basic(optional = false)
    @Column(name = "PATIENTCAREDATE")
    private LocalDate patientCareDate;

    @ManyToOne
    @JoinColumn(name = "PATIENTHISTORY", referencedColumnName = "ID")
    private PatientPersonalHistory patientHistory;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "BLOODPRESSURE")
    private String bloodPressure;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "HEARTRATE")
    private String heartRate;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "WEIGHT")
    private String weight;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "HEIGHT")
    private String height;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "TEMPERATURE")
    private String temperature;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "BODYMASSINDEX")
    private String bodyMassIndex;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "BODYMASSINDEXIDEAL")
    private String bodyMassIndexIdeal;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "NURSINGNOTES")
    private String nursingNotes;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "REASON")
    private String reason;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "CAREPLAN")
    private String carePlan;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "OBSERVATIONS")
    private String observations;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "PHYSICALEXAM")
    private String physicalExam;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "TREATMENT")
    private String treatment;

    @OneToMany(mappedBy = "patientCare", fetch = FetchType.LAZY)
    private List<MedicalAppointment> medicalAppointments;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from entity to dto
     */
    public PatientCare(PatientCareDto dto) {
        this.id = dto.getId();
        updatePatientCare(dto);
    }

    /**
     * @param dto constructor from dto to entity
     */
    public void updatePatientCare(PatientCareDto dto) {
        this.patientCareDate = LocalDate.parse(dto.getPatientCareDate());
        this.bloodPressure = dto.getBloodPressure();
        this.heartRate = dto.getHeartRate();
        this.weight = dto.getWeight();
        this.height = dto.getHeight();
        this.temperature = dto.getTemperature();
        this.bodyMassIndex = dto.getBodyMassIndex();
        this.bodyMassIndexIdeal = dto.getBodyMassIndexIdeal();
        this.nursingNotes = dto.getNursingNotes();
        this.reason = dto.getReason();
        this.carePlan = dto.getCarePlan();
        this.observations = dto.getObservations();
        this.physicalExam = dto.getPhysicalExam();
        this.treatment = dto.getTreatment();
        this.version = dto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPatientCareDate() {
        return patientCareDate;
    }

    public void setPatientCareDate(LocalDate patientCareDate) {
        this.patientCareDate = patientCareDate;
    }

    public PatientPersonalHistory getPatientHistory() {
        return patientHistory;
    }

    public void setPatientHistory(PatientPersonalHistory patientHistory) {
        this.patientHistory = patientHistory;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBodyMassIndex() {
        return bodyMassIndex;
    }

    public void setBodyMassIndex(String bodyMassIndex) {
        this.bodyMassIndex = bodyMassIndex;
    }

    public String getBodyMassIndexIdeal() {
        return bodyMassIndexIdeal;
    }

    public void setBodyMassIndexIdeal(String bodyMassIndexIdeal) {
        this.bodyMassIndexIdeal = bodyMassIndexIdeal;
    }

    public String getNursingNotes() {
        return nursingNotes;
    }

    public void setNursingNotes(String nursingNotes) {
        this.nursingNotes = nursingNotes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(String carePlan) {
        this.carePlan = carePlan;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getPhysicalExam() {
        return physicalExam;
    }

    public void setPhysicalExam(String physicalExam) {
        this.physicalExam = physicalExam;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
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
