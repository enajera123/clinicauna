package cr.ac.una.clinicaunaws.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
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
import cr.ac.una.clinicaunaws.dto.GeneralInformationDto;

/**
 * @author arayaroma
 */
@Entity
@Table(name = "TBL_GENERAL_INFORMATION", schema = SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "GeneralInformation.findAll", query = "SELECT g FROM GeneralInformation g", hints = {
                @QueryHint(name = "eclipselink.refresh", value = "true") }),
        @NamedQuery(name = "GeneralInformation.findById", query = "SELECT g FROM GeneralInformation g WHERE g.id = :id", hints = {
                @QueryHint(name = "eclipselink.refresh", value = "true") }),
        @NamedQuery(name = "GeneralInformation.findByName", query = "SELECT g FROM GeneralInformation g WHERE g.name = :name", hints = {
                @QueryHint(name = "eclipselink.refresh", value = "true") }),
})
public class GeneralInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = SEQ_GENERAL_INFORMATION, sequenceName = SCHEMA + "."
            + SEQ_GENERAL_INFORMATION, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_GENERAL_INFORMATION)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "EMAIL")
    private String email;

    @Lob
    @Column(name = "PHOTO")
    private byte[] photo;

    @Lob
    @Column(name = "HTMLTEMPLATE")
    private String htmltemplate;

    @Version
    @Column(name = "VERSION")
    private Long version;

    /**
     * @param dto the dto to convert to entity
     */
    public GeneralInformation(GeneralInformationDto dto) {
        this.id = dto.getId();
        updateGeneralInformation(dto);
    }

    /**
     * @param entity the entity to convert to dto
     */
    public void updateGeneralInformation(GeneralInformationDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.photo = dto.getPhoto();
        this.htmltemplate = dto.getHtmltemplate();
        this.version = dto.getVersion();
    }

}
