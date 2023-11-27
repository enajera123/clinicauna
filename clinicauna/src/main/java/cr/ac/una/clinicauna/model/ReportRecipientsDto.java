package cr.ac.una.clinicauna.model;

/**
 * 
 * @author arayaroma
 */
public class ReportRecipientsDto {
    private Long id;
    private ReportDto report;
    private String email;
    private Long version;

    public ReportRecipientsDto() {
    }

    public ReportRecipientsDto(ReportRecipientsDto reportRecipientsDto) {
        this();
        setId(reportRecipientsDto.getId());
        setReport(reportRecipientsDto.getReport());
        setEmail(reportRecipientsDto.getEmail());
        setVersion(reportRecipientsDto.getVersion());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportDto getReport() {
        return this.report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", report='" + getReport() + "'" +
                ", email='" + getEmail() + "'" +
                ", version='" + getVersion() + "'" +
                "}";
    }

}
