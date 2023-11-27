package cr.ac.una.clinicauna.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicauna.util.QueryManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author arayaroma
 */
public class ReportDto {
    private Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public SimpleStringProperty query;
    public SimpleObjectProperty<LocalDate> reportDate;
    public SimpleStringProperty frequency;
    private List<ReportParametersDto> reportParameters;
    private List<ReportRecipientsDto> reportRecipients;
    private QueryManager<?> queryManager = new QueryManager<>();
    private Long version;

    public ReportDto() {
        name = new SimpleStringProperty();
        description = new SimpleStringProperty();
        query = new SimpleStringProperty();
        reportDate = new SimpleObjectProperty<>();
        frequency = new SimpleStringProperty();
        reportParameters = new ArrayList<>();
        reportRecipients = new ArrayList<>();
    }

    public ReportDto(ReportDto reportDto) {
        this();
        setId(reportDto.getId());
        setName(reportDto.getName());
        setDescription(reportDto.getDescription());
        setQuery(reportDto.getQuery());
        setReportDate(reportDto.getReportDate());
        setFrequency(reportDto.getFrequency());
        setVersion(reportDto.getVersion());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return this.description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getQuery() {
        return this.query.get();
    }

    public void setQuery(String query) {
        this.query.set(query);
    }

    public String getReportDate() {
        return this.reportDate.get().toString();
    }

    public void setReportDate(String reportDate) {
        this.reportDate.set(LocalDate.parse(reportDate));
    }

    public String getFrequency() {
        return this.frequency.get();
    }

    public void setFrequency(String frequency) {
        this.frequency.set(frequency);
    }

    public List<ReportParametersDto> getReportParameters() {
        return this.reportParameters;
    }

    public void setReportParameters(List<ReportParametersDto> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public List<ReportRecipientsDto> getReportRecipients() {
        return this.reportRecipients;
    }

    public void setReportRecipients(List<ReportRecipientsDto> reportRecipients) {
        this.reportRecipients = reportRecipients;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public QueryManager<?> getQueryManager() {
        return this.queryManager;
    }

    public void setQueryManager(QueryManager<?> queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", query='" + getQuery() + "'" +
                ", reportDate='" + getReportDate() + "'" +
                ", frequency='" + getFrequency() + "'" +
                ", reportParameters='" + getReportParameters() + "'" +
                ", reportRecipients='" + getReportRecipients() + "'" +
                ", queryManager='" + getQueryManager() + "'" +
                ", version='" + getVersion() + "'" +
                "}";
    }

}
