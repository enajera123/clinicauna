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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.AgendaDto;
import static cr.ac.una.clinicaunaws.util.Database.*;
import jakarta.persistence.QueryHint;

/**
 *
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_AGENDA", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "Agenda.findById", query = "SELECT a FROM Agenda a WHERE a.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), })
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_AGENDA, sequenceName = SCHEMA + "." + SEQ_AGENDA, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_AGENDA)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DOCTOR", referencedColumnName = "ID")
    private Doctor doctor;

    @NotNull
    @Basic(optional = false)
    @Column(name = "AGENDADATE")
    private LocalDate agendaDate;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 5)
    @Column(name = "SHIFTSTARTTIME")
    private String shiftStartTime;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 5)
    @Column(name = "SHIFTENDTIME")
    private String shiftEndTime;

    @NotNull
    @Basic(optional = false)
    @Column(name = "HOURLYSLOTS")
    private Long hourlySlots;

    @OneToMany(mappedBy = "agenda")
    private List<MedicalAppointment> medicalAppointments;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from dto to entity
     */
    public Agenda(AgendaDto dto) {
        this.id = dto.getId();
        updateAgenda(dto);
    }

    /**
     * @param dto constructor from entity to dto
     */
    public void updateAgenda(AgendaDto dto) {
        this.agendaDate = LocalDate.parse(dto.getAgendaDate());
        this.shiftStartTime = dto.getShiftStartTime();
        this.shiftEndTime = dto.getShiftEndTime();
        this.hourlySlots = dto.getHourlySlots();
        this.version = dto.getVersion();
    }

}
