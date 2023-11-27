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
import cr.ac.una.clinicaunaws.dto.ReportParametersDto;
import jakarta.persistence.QueryHint;

/**
 * 
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_REPORT_PARAMETERS", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "ReportParameters.findAll", query = "SELECT r FROM ReportParameters r",hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "ReportParameters.findById", query = "SELECT r FROM ReportParameters r WHERE r.id = :id",hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
})
public class ReportParameters implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_REPORT_PARAMETERS, sequenceName = SCHEMA + "."
            + SEQ_REPORT_PARAMETERS, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_REPORT_PARAMETERS)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REPORT", referencedColumnName = "ID")
    private Report report;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "VALUE")
    private String value;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto constructor from dto to entity
     */
    public ReportParameters(ReportParametersDto dto) {
        this.id = dto.getId();
        updateReport(dto);
    }

    /**
     * @param dto update the entity from dto
     */
    public void updateReport(ReportParametersDto dto) {
        this.name = dto.getName();
        this.value = dto.getValue();
        this.version = dto.getVersion();
    }
}
