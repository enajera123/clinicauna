package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.UserService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.ImageLoader;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class MainController implements Initializable {

    @FXML
    private JFXHamburger hamburguerMenu;
    @FXML
    private VBox menuView;
    @FXML
    private JFXDrawer sliderMenu;
    @FXML
    private Label lblUserLoggued;
    @FXML
    private ImageView imgProfilePhoto;
    @FXML
    private StackPane menuLateral;
    @FXML
    private BorderPane parent;
    @FXML
    private StackPane container;
    @FXML
    private StackPane stack;
    @FXML
    private HBox profileContainer;
    @FXML
    private VBox changePasswordView;
    @FXML
    private Label lblChangePasswordInfo;
    @FXML
    private JFXPasswordField txfNewPassword;
    @FXML
    private JFXPasswordField txfConfirmPassword;
    @FXML
    private Button btnAgendaModule;
    @FXML
    private Button btnUserModule;
    @FXML
    private Button btnDoctorModule;
    @FXML
    private Button btnPatientModule;
    @FXML
    private Button btnReportModule;
    @FXML
    private Button btnGeneralInformationModule;

    private UserService userService = new UserService();
    private UserDto userLoggued;
    private Data data = Data.getInstance();
    private Node buttonSelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            data.setData("mainController", this);
            intializeSliderMenu();
            userLoggued = (UserDto) data.getData("userLoggued");
            if (userLoggued != null) {
                lblUserLoggued.setText(userLoggued.getName());

                imgProfilePhoto
                        .setClip(new Circle(imgProfilePhoto.getFitWidth() / 2, imgProfilePhoto.getFitHeight() / 2, 30));
                if (userLoggued.getProfilePhoto() != null) {
                    imgProfilePhoto.setImage(ImageLoader.setImage(userLoggued.getProfilePhoto()));
                }
            }
            loadPrivileges();
        } catch (Exception e) {
            try {
                Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("Login").load());
                System.out.println(e.toString());
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void btnUserModuleAction(ActionEvent event) throws IOException {
        focusButton(btnUserModule);
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader userLoader = App.getFXMLLoader("UserModule");
                    container.getChildren().clear();
                    container.getChildren().add(userLoader.load());
                } catch (Exception e) {
                }

            });
        }).start();
    }

    @FXML
    private void btnPatientModuleAction(ActionEvent event) throws IOException {
        focusButton(btnPatientModule);
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader patientLoader = App.getFXMLLoader("PatientModule");
                    container.getChildren().clear();
                    container.getChildren().add(patientLoader.load());
                } catch (Exception e) {
                }

            });
        }).start();
    }

    /**
     * FIXME: Add catch block
     */
    @FXML
    private void btnLogOutAction(ActionEvent event) {
        try {

            Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("Login").load());
            Data.getInstance().clearData();
        } catch (IOException e) {
        }
    }

    @FXML
    private void btnDoctorModuleAction(ActionEvent event) throws IOException {
        focusButton(btnDoctorModule);

        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader doctorLoader = App.getFXMLLoader("DoctorModule");
                    container.getChildren().clear();
                    container.getChildren().add(doctorLoader.load());
                } catch (Exception e) {
                }

            });
        }).start();
    }

    @FXML
    private void editUserLogguedAction(MouseEvent event) throws IOException {
        userLoggued = (UserDto) userService.findUserById(userLoggued.getId()).getData();
        data.setData("userBuffer", userLoggued);
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("UserRegister").load());
    }

    @FXML
    private void discardChangesAction(ActionEvent event) throws IOException {
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("Login").load());
    }

    @FXML
    private void submitChangesAction(ActionEvent event) {
        String password = txfNewPassword.getText(), confirmPassword = txfConfirmPassword.getText();
        if (password.isBlank() || !password.equals(confirmPassword)) {
            Message.showNotification("Warning", MessageType.ERROR, "youMustToWriteASamePassword");
            return;
        }
        ResponseWrapper response = userService.changePassword(userLoggued.getId(), userLoggued.getPassword(), password);
        if (response.getCode() == ResponseCode.OK) {
            Message.showNotification("Succeed", MessageType.INFO, "passwordChangedSuccess");
            changePasswordView.setVisible(false);
            menuLateral.setDisable(false);
            profileContainer.setDisable(false);
            return;
        }
        Message.showNotification("Internal Error", MessageType.ERROR, response.getMessage());
    }

    @FXML
    private void passwordsEquals(KeyEvent event) {
        if (!txfNewPassword.getText().equals(txfConfirmPassword.getText())) {
            lblChangePasswordInfo.setText("New password and confirm is not equals");
            lblChangePasswordInfo.getStyleClass().add("red-color");
        } else {
            lblChangePasswordInfo.setText("");
        }
    }

    @FXML
    private void btnGeneralInformationModuleAction(ActionEvent event) throws IOException {
        focusButton(btnGeneralInformationModule);
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader generalLoader = App.getFXMLLoader("GeneralInformationModule");
                    container.getChildren().clear();
                    container.getChildren().add(generalLoader.load());
                } catch (Exception e) {
                }

            });
        }).start();
    }

    @FXML
    private void btnAgendaModuleAction(ActionEvent event) throws IOException {
        focusButton(btnAgendaModule);
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader agendaLoader = App.getFXMLLoader("AgendaModule");
                    container.getChildren().clear();
                    container.getChildren().add(agendaLoader.load());
                    AgendaModuleController controller = agendaLoader.getController();
                    if (controller != null) {
                        controller.loadView((DoctorDto) Data.getInstance().getData("doctorBuffer"));
                    }
                } catch (Exception e) {
                }

            });
        }).start();
    }

    @FXML
    private void btnReportModuleAction(ActionEvent event) throws IOException {
        focusButton(btnReportModule);
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader reportLoader = App.getFXMLLoader("ReportModule");
                    container.getChildren().clear();
                    container.getChildren().add(reportLoader.load());
                    ReportModuleController controller = reportLoader.getController();
                    if (controller != null) {
                        String option = (String) Data.getInstance().getData("option");
                        controller.loadView(option);
                    }
                } catch (Exception e) {
                }
            });
        }).start();
    }

    private void intializeSliderMenu() {
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburguerMenu);
        sliderMenu.setSidePane(menuLateral);
        sliderMenu.open();
        transition.setRate(1);
        // transition.play();
        hamburguerMenu.setOnMouseClicked(t -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (sliderMenu.isOpened()) {
                sliderMenu.close();
                sliderMenu.setOnDrawerClosed((event) -> {
                    parent.getChildren().remove(sliderMenu);
                });
            } else {
                parent.setLeft(sliderMenu);
                sliderMenu.open();
            }
        });
    }

    private void focusButton(Node node) {
        if (node != null) {
            if (buttonSelected != null) {
                buttonSelected.getStyleClass().remove("bg-gray");
            }
            buttonSelected = node;
            buttonSelected.getStyleClass().add("bg-gray");
        }
    }

    /**
     * FIXME: Add catch block
     *
     * @param option
     * @param values
     */
    public void loadView(String option) {
        try {
            option = option.toLowerCase();
            if (option.equals("usermodule")) {
                btnUserModuleAction(null);
            }
            if (option.equals("doctormodule")) {
                btnDoctorModuleAction(null);
            }
            if (option.equals("patientmodule")) {
                btnPatientModuleAction(null);
            }
            if (option.equals("agendamodule")) {
                btnAgendaModuleAction(null);
            }
            if (option.equals("reportmodule")) {
                btnReportModuleAction(null);
            }

        } catch (IOException e) {
        }
    }

    private void loadPrivileges() {
        verifyPasswordChanged();
        if (userLoggued != null) {
            if (userLoggued.getRole().toLowerCase().equals("recepcionist")) {
                menuView.getChildren().removeAll(btnDoctorModule,
                        btnGeneralInformationModule,
                        btnUserModule,
                        btnReportModule);
            } else if (userLoggued.getRole().toLowerCase().equals("doctor")) {
                menuView.getChildren().removeAll(btnDoctorModule,
                        btnGeneralInformationModule,
                        btnUserModule);
            }

        }

    }

    private void verifyPasswordChanged() {
        if (userLoggued != null && userLoggued.getPasswordChanged().equals("Y")) {
            changePasswordView.setVisible(true);
            menuLateral.setDisable(true);
            hamburguerMenu.setDisable(true);
            profileContainer.setDisable(true);
        } else {
            changePasswordView.setVisible(false);
            menuLateral.setDisable(false);
            hamburguerMenu.setDisable(false);
        }
    }
}
