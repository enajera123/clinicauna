package cr.ac.una.clinicaunaws.entities;

import java.io.Serializable;
import java.time.LocalDate;
import cr.ac.una.clinicaunaws.dto.MedicalAppointmentDto;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static cr.ac.una.clinicaunaws.util.Database.*;

/**
 *
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_MEDICAL_APPOINTMENT", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "MedicalAppointment.findAll", query = "SELECT m FROM MedicalAppointment m"),
    @NamedQuery(name = "MedicalAppointment.findById", query = "SELECT m FROM MedicalAppointment m WHERE m.id = :id"),})
public class MedicalAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_MEDICAL_APPOINTMENT, sequenceName = SCHEMA + "."
            + SEQ_MEDICAL_APPOINTMENT, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_MEDICAL_APPOINTMENT)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENDA", referencedColumnName = "ID")
    private Agenda agenda;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT", referencedColumnName = "ID")
    private Patient patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENTCARE", referencedColumnName = "ID")
    private PatientCare patientCare;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULEDBY", referencedColumnName = "ID")
    private User scheduledBy;

    @NotNull
    @Basic(optional = false)
    @Column(name = "SLOTSNUMBER")
    private Long slotsNumber;

    @NotNull
    @Basic(optional = false)
    @Column(name = "SCHEDULEDDATE")
    private LocalDate scheduledDate;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 5)
    @Column(name = "SCHEDULEDSTARTTIME")
    private String scheduledStartTime;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 5)
    @Column(name = "SCHEDULEDENDTIME")
    private String scheduledEndTime;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 9)
    @Column(name = "STATE")
    private String state;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "REASON")
    private String reason;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 8)
    @Column(name = "PATIENTPHONENUMBER")
    private String patientPhoneNumber;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "PATIENTEMAIL")
    private String patientEmail;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from entity to dto
     */
    public MedicalAppointment(MedicalAppointmentDto dto) {
        this.id = dto.getId();
        updateMedicalAppointment(dto);
    }

    /**
     * @param dto constructor from dto to entity
     */
    public void updateMedicalAppointment(MedicalAppointmentDto dto) {
        this.slotsNumber = dto.getSlotsNumber();
        this.scheduledDate = LocalDate.parse(dto.getScheduledDate());
        this.scheduledStartTime = dto.getScheduledStartTime();
        this.scheduledEndTime = dto.getScheduledEndTime();
        this.state = dto.getState();
        this.reason = dto.getReason();
        this.patientPhoneNumber = dto.getPatientPhoneNumber();
        this.patientEmail = dto.getPatientEmail();
        this.version = dto.getVersion();
    }
}
