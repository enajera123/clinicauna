package cr.ac.una.clinicaunaws.entities;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static cr.ac.una.clinicaunaws.util.Database.*;
import java.io.Serializable;
import java.time.LocalDate;
import cr.ac.una.clinicaunaws.dto.MedicalExamDto;

/**
 * 
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_MEDICAL_EXAM", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "MedicalExam.findAll", query = "SELECT m FROM MedicalExam m"),
        @NamedQuery(name = "MedicalExam.findById", query = "SELECT m FROM MedicalExam m WHERE m.id = :id"),
})
public class MedicalExam implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_MEDICAL_EXAM, sequenceName = SCHEMA + "." + SEQ_MEDICAL_EXAM, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_MEDICAL_EXAM)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PATIENTHISTORY", referencedColumnName = "ID")
    private PatientPersonalHistory patientHistory;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Column(name = "MEDICALEXAMDATE")
    private LocalDate medicalExamDate;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "NOTES")
    private String notes;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto to convert to entity
     */
    public MedicalExam(MedicalExamDto dto) {
        this.id = dto.getId();
        updateMedicalExam(dto);
    }

    /**
     * @param dto to update entity
     */
    public void updateMedicalExam(MedicalExamDto dto) {
        this.name = dto.getName();
        this.medicalExamDate = LocalDate.parse(dto.getMedicalExamDate());
        this.notes = dto.getNotes();
        this.version = dto.getVersion();
    }

}
