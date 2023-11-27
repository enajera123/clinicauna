package cr.ac.una.clinicauna.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author estebannajera
 */
public class GeneralInformationDto {

    private Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty email;
    private byte[] photo;
    private Long version;

    public GeneralInformationDto() {
        name = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    public String getEmail() {
        return email.get();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Long getVersion() {
        return version;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
