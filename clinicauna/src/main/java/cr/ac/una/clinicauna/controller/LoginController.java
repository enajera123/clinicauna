package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.animations.FadeIn;
import cr.ac.una.clinicauna.model.GeneralInformationDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.GeneralInformationService;
import cr.ac.una.clinicauna.services.UserService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.ImageLoader;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txfUsername;
    @FXML
    private JFXPasswordField txfPassword;
    @FXML
    private VBox mainView;
    @FXML
    private VBox recoveryPasswordView;
    @FXML
    private JFXTextField txfRecoveryEmail;
    @FXML
    private StackPane parent;
    @FXML
    private Label lblLanguage;
    @FXML
    private VBox aboutUsView;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private Label lblName;
    @FXML
    private Label lblEmail;

    private UserService userService = new UserService();
    private GeneralInformationService generalInformationService = new GeneralInformationService();
    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (data.getLanguageOption().equals("en")) {
                lblLanguage.setText("EN/ES");
            } else {
                lblLanguage.setText("ES/EN");
            }
            txfPassword.setOnKeyPressed(event -> keyLoginHandler(event));
            txfUsername.setOnKeyPressed(event -> keyLoginHandler(event));

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void btnLogInAction(ActionEvent event) throws IOException {
        String user = txfUsername.getText(), password = txfPassword.getText();
        if (user.isBlank() || password.isBlank()) {
            Message.showNotification("Ups", MessageType.ERROR, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    ResponseWrapper response = userService.verifyUser(user, password);
                    if (response.getCode() == ResponseCode.OK) {
                        UserDto userDto = (UserDto) response.getData();
                        if (userDto.getIsActive().equals("N")) {
                            Message.showNotification("Ups", MessageType.INFO, "theUserIsNotActive");
                            return;
                        }
                        data.setData("Token", userDto.getToken());
                        data.setData("userLoggued", userDto);
                        loadLanguage(userDto);
                        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("Main").load());
                        return;
                    }
                    Message.showNotification(response.getCode().name(), MessageType.ERROR, response.getMessage());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });
        }).start();
    }

    @FXML
    private void forgotYourPasswordAction(MouseEvent event) {
        new FadeIn(recoveryPasswordView).play();
        recoveryPasswordView.toFront();
    }

    @FXML
    private void changeLanguajeAction(MouseEvent event) throws IOException {
        String option = data.getLanguageOption();
        switch (option) {
            case "en":
                data.setLanguageOption("es");
                break;
            case "es":
                data.setLanguageOption("en");
                break;
        }
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("Login").load());
    }

    @FXML
    private void backToLoginAction(MouseEvent event) {
        new FadeIn(mainView).play();
        mainView.toFront();
    }

    @FXML
    private void btnSendRecoveryEmailAction(ActionEvent event) {
        String email = txfRecoveryEmail.getText();
        if (email.isBlank()) {
            Message.showNotification("Ups", MessageType.ERROR, "fieldsEmpty");
            return;
        }
        ResponseWrapper response = userService.recoverPassword(email);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification("Sending", MessageType.INFO, "sendingEmail");
        } else {
            Message.showNotification("Error", MessageType.ERROR, response.getMessage());
        }
    }

    @FXML
    private void aboutUsAction(MouseEvent event) {
        loadGeneralInformation();
        new FadeIn(aboutUsView).play();
        aboutUsView.toFront();
    }

    private void keyLoginHandler(KeyEvent ev) {
        try {
            if (ev.getCode() == KeyCode.ENTER) {
                btnLogInAction(null);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void loadLanguage(UserDto userDto) {
        if (userDto.getLanguage().toLowerCase().equals("english")) {
            data.setLanguageOption("en");
            return;
        }
        data.setLanguageOption("es");
    }

    private void loadGeneralInformation() {
        List<GeneralInformationDto> informationDtos = (List<GeneralInformationDto>) generalInformationService.getAllGeneralInformation().getData();
        if (informationDtos != null && !informationDtos.isEmpty()) {
            GeneralInformationDto generalInformationDto = informationDtos.get(0);
            if (generalInformationDto != null) {
                lblEmail.setText(generalInformationDto.getEmail());
                lblName.setText(generalInformationDto.getName());
                if (generalInformationDto.getPhoto() != null) {
                    imgPhoto.setImage(ImageLoader.setImage(generalInformationDto.getPhoto()));
                }
            }

        }
    }
}
