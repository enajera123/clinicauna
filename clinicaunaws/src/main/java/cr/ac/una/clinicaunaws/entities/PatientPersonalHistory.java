package cr.ac.una.clinicaunaws.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import static cr.ac.una.clinicaunaws.util.Database.*;
import java.io.Serializable;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.PatientPersonalHistoryDto;
import jakarta.persistence.QueryHint;
import lombok.NoArgsConstructor;

/**
 *
 * @author arayaroma
 */
@Entity
@NoArgsConstructor
@Table(name = "TBL_PATIENT_PERSONAL_HISTORY", schema = SCHEMA)
@NamedQueries({
        @NamedQuery(name = "PatientPersonalHistory.findAll", query = "SELECT p FROM PatientPersonalHistory p", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "PatientPersonalHistory.findById", query = "SELECT p FROM PatientPersonalHistory p WHERE p.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), })
public class PatientPersonalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_PATIENT_PERSONAL_HISTORY, sequenceName = SCHEMA + "."
            + SEQ_PATIENT_PERSONAL_HISTORY, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_PATIENT_PERSONAL_HISTORY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID", referencedColumnName = "ID", unique = true)
    private Patient patient;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "PATHOLOGICAL")
    private String pathological;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "HOSPITALIZATIONS")
    private String hospitalizations;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "SURGICAL")
    private String surgical;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "ALLERGIES")
    private String allergies;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "TREATMENTS")
    private String treatments;

    @OneToMany(mappedBy = "patientHistory", fetch = FetchType.LAZY)
    private List<MedicalExam> medicalExams;

    @OneToMany(mappedBy = "patientHistory", fetch = FetchType.LAZY)
    private List<PatientCare> patientCares;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from dto to entity
     */
    public PatientPersonalHistory(PatientPersonalHistoryDto dto) {
        this.id = dto.getId();
        updatePatientPersonalHistory(dto);
    }

    /**
     * @param dto to be updated
     */
    public void updatePatientPersonalHistory(PatientPersonalHistoryDto dto) {
        this.pathological = dto.getPathological();
        this.hospitalizations = dto.getHospitalizations();
        this.surgical = dto.getSurgical();
        this.allergies = dto.getAllergies();
        this.treatments = dto.getTreatments();
        this.version = dto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPathological() {
        return pathological;
    }

    public void setPathological(String pathological) {
        this.pathological = pathological;
    }

    public String getHospitalizations() {
        return hospitalizations;
    }

    public void setHospitalizations(String hospitalizations) {
        this.hospitalizations = hospitalizations;
    }

    public String getSurgical() {
        return surgical;
    }

    public void setSurgical(String surgical) {
        this.surgical = surgical;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }

    public List<MedicalExam> getMedicalExams() {
        return medicalExams;
    }

    public void setMedicalExams(List<MedicalExam> medicalExams) {
        this.medicalExams = medicalExams;
    }

    public List<PatientCare> getPatientCares() {
        return patientCares;
    }

    public void setPatientCares(List<PatientCare> patientCares) {
        this.patientCares = patientCares;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
}
