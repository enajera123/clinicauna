package cr.ac.una.clinicauna.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author arayaroma
 */
public class ReportParametersDto {
    private Long id;
    private ReportDto report;
    public SimpleStringProperty name;
    public SimpleStringProperty value;
    private Long version;

    public ReportParametersDto() {
        name = new SimpleStringProperty();
        value = new SimpleStringProperty();
    }

    public ReportParametersDto(ReportParametersDto reportParametersDto) {
        this();
        setId(reportParametersDto.getId());
        setReport(reportParametersDto.getReport());
        setName(reportParametersDto.getName());
        setValue(reportParametersDto.getValue());
        setVersion(reportParametersDto.getVersion());
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

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getValue() {
        return this.value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
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
                ", name='" + getName() + "'" +
                ", value='" + getValue() + "'" +
                ", version='" + getVersion() + "'" +
                "}";
    }

}