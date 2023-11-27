package cr.ac.una.clinicauna.components;

import cr.ac.una.clinicauna.model.MedicalAppointmentDto;
 

import javafx.scene.layout.VBox;

/**
 *
 * @author estebannajera
 */
public class AppointmentNode extends VBox {

    private MedicalAppointmentDto medicalAppointmentDto;

    public AppointmentNode(MedicalAppointmentDto medicalAppointmentDto) {
        super();
        this.medicalAppointmentDto = medicalAppointmentDto;
    }

    public MedicalAppointmentDto getMedicalAppointmentDto() {
        return medicalAppointmentDto;
    }
    
    public void setMedicalAppointmentDto(MedicalAppointmentDto medicalAppointmentDto) {
        this.medicalAppointmentDto = medicalAppointmentDto;
    }

}
