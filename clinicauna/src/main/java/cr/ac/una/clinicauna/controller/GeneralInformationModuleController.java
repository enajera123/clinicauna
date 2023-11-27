package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.model.GeneralInformationDto;
import cr.ac.una.clinicauna.services.GeneralInformationService;
import cr.ac.una.clinicauna.util.FileLoader;
import cr.ac.una.clinicauna.util.ImageLoader;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class GeneralInformationModuleController implements Initializable {

    @FXML
    private HBox parent;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfEmail;
    private GeneralInformationService generalInformationService = new GeneralInformationService();
    private GeneralInformationDto generalInformationBuffer = new GeneralInformationDto();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        List<GeneralInformationDto> generalInformationDtos = (List<GeneralInformationDto>) generalInformationService.getAllGeneralInformation().getData();
        if (generalInformationDtos != null && !generalInformationDtos.isEmpty()) {
            generalInformationBuffer = generalInformationDtos.get(0);
        }
        if (generalInformationBuffer == null) {
            generalInformationBuffer = new GeneralInformationDto();
        }
        bindGeneralInformation();
    }

    @FXML
    private void selectPhotoProfile(ActionEvent event) {
        File file = FileLoader.selectFile("Images", "*.png", "*.jpg", "*.jpeg");
        if (file != null) {
            imgPhoto.setImage(ImageLoader.setImage(file));
            generalInformationBuffer.setPhoto(ImageLoader.imageToByteArray(file));
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        ResponseWrapper response = generalInformationBuffer.getId() != null ? generalInformationService.updateGeneralInformation(generalInformationBuffer) : generalInformationService.createGeneralInformation(generalInformationBuffer);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification(response.getCode().name(), MessageType.INFO, response.getMessage());
            generalInformationBuffer = (GeneralInformationDto) response.getData();
            return;
        }
        Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
    }

    private void bindGeneralInformation() {
        if (generalInformationBuffer != null) {
            txfEmail.textProperty().bindBidirectional(generalInformationBuffer.email);
            txfName.textProperty().bindBidirectional(generalInformationBuffer.name);
            if (generalInformationBuffer.getPhoto() != null) {
                imgPhoto.setImage(ImageLoader.setImage(generalInformationBuffer.getPhoto()));
            }
        }
    }

}
