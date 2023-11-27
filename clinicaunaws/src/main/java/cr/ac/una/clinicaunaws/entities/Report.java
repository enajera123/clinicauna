package cr.ac.una.clinicaunaws.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import static cr.ac.una.clinicaunaws.util.Database.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.ReportDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.QueryHint;

/**
 *
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_REPORT", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Report.findById", query = "SELECT r FROM Report r WHERE r.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),})
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_REPORT, sequenceName = SCHEMA + "." + SEQ_REPORT, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_REPORT)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @Basic(optional = false)
    @Column(name = "QUERY")
    private String query;

    @NotNull
    @Basic(optional = false)
    @Column(name = "REPORTDATE")
    private LocalDate reportDate;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 32)
    @Column(name = "FREQUENCY")
    private String frequency;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    private List<ReportParameters> reportParameters;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    private List<ReportRecipients> reportRecipients;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param entity constructor from dto to entity
     */
    public Report(ReportDto entity) {
        this.id = entity.getId();
        updateReport(entity);
    }

    /**
     * @param dto constructor from entity to dto
     */
    public void updateReport(ReportDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.query = dto.getQuery();
        System.out.println(dto.getReportDate());
        this.reportDate = LocalDate.parse(dto.getReportDate());
        this.frequency = dto.getFrequency();
        this.version = dto.getVersion();
    }

}
