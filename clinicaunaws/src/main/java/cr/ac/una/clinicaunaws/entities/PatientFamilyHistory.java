package cr.ac.una.clinicaunaws.entities;

import java.io.Serializable;
import cr.ac.una.clinicaunaws.dto.PatientFamilyHistoryDto;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static cr.ac.una.clinicaunaws.util.Database.*;
import jakarta.persistence.QueryHint;

/**
 *
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_PATIENT_FAMILY_HISTORY", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "PatientFamilyHistory.findAll", query = "SELECT p FROM PatientFamilyHistory p", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "PatientFamilyHistory.findById", query = "SELECT p FROM PatientFamilyHistory p WHERE p.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), })
public class PatientFamilyHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_PATIENT_FAMILY_HISTORY, sequenceName = SCHEMA + "."
            + SEQ_PATIENT_FAMILY_HISTORY, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_PATIENT_FAMILY_HISTORY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PATIENT", referencedColumnName = "ID")
    private Patient patient;

    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "DISEASE")
    private String disease;

    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "RELATIONSHIP")
    private String relationship;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from dto to entity
     */
    public PatientFamilyHistory(PatientFamilyHistoryDto dto) {
        this.id = dto.getId();
        updatePatientFamilyHistory(dto);
    }

    /**
     * @param dto constructor from dto to entity
     */
    public void updatePatientFamilyHistory(PatientFamilyHistoryDto dto) {
        this.id = dto.getId();
        this.disease = dto.getDisease();
        this.relationship = dto.getRelationship();
        this.version = dto.getVersion();
    }

}
