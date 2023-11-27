package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.services.UserService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 * @author arayaroma
 */
public class DoctorModuleController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private JFXTextField txfSearchDoctor;
    @FXML
    private ComboBox<String> cbSearchParameter;
    @FXML
    private TableView<DoctorDto> tblDoctorsView;
    @FXML
    private TableColumn<DoctorDto, String> tcCarne;
    @FXML
    private TableColumn<DoctorDto, String> tcCode;
    @FXML
    private TableColumn<DoctorDto, String> tcEndingTIme;
    @FXML
    private TableColumn<DoctorDto, String> tcStartingTime;
    @FXML
    private Button btnEdit;
    private DoctorDto doctorBuffer;
    private UserService userService = new UserService();
    private List<UserDto> userDtos = new ArrayList<>();

    private Data data = Data.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        if (data.getLanguageOption().equals("en")) {
            cbSearchParameter.getItems().addAll("Carne", "Code", "Starting Time", "Ending Time");
        } else {
            cbSearchParameter.getItems().addAll("Carne", "Codigo", "Hora de Inicio", "Hora de Fin");
        }
        btnEdit.setDisable(true);
        initializeList();
        userDtos = (List<UserDto>) userService.getUsers().getData();
        loadDoctors(userDtos);
        txfSearchDoctor.setOnKeyPressed(e -> searchUserAction(e));
    }

    @FXML
    private void btnNewUserAction(ActionEvent event) throws IOException {
        data.removeData("userBuffer");
        loadUserRegisterView();
    }

    @FXML
    private void btnEditDoctorAction(ActionEvent event) throws IOException {
        UserDto userBuffer = (UserDto) userService.findUserById(doctorBuffer.getId()).getData();
        data.setData("userBuffer", userBuffer);
        loadUserRegisterView();
    }

    @FXML
    private void btnDeleteDoctorAction(ActionEvent event) {
        UserDto userBuffer = (UserDto) userService.findUserById(doctorBuffer.getId()).getData();
        if (userBuffer != null && doctorBuffer != null) {
            ResponseWrapper response = userService.deleteUser(userBuffer);
            if (response.getCode() == ResponseCode.OK) {
                tblDoctorsView.getItems().remove(doctorBuffer);
            } else {
                Message.showNotification("Error", MessageType.ERROR, response.getMessage());
            }
        }
    }

    private void loadUserRegisterView() {
        try {
            FXMLLoader loader = App.getFXMLLoader("UserRegister");
            Animate.MakeDefaultFadeTransition(parent, loader.load());
            UserRegisterController controller = loader.getController();
            if (controller != null) {
                controller.loadFlags(true);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Searching bar
     *
     * @param event
     */
    private void searchUserAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchDoctor.getText(), parameterKey = cbSearchParameter.getValue();
            if (key.isBlank() || parameterKey == null) {
                loadDoctors(userDtos);
                return;
            }
            loadDoctors(filterUsers(userDtos, parameterKey, key));
        }
    }

    private List<UserDto> filterUsers(List<UserDto> users, String parameter, String key) {
        if (users == null) {
            return Collections.emptyList();
        }

        String lowerKey = key.toLowerCase();
        return users.stream()
                .filter(user -> user.getDoctor() != null && containsIgnoreCase(user, parameter, lowerKey))
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(UserDto user, String parameter, String key) {
        switch (parameter.toLowerCase()) {
            case "carne":
                return user.getDoctor().getIdCard().toString().toLowerCase().contains(key);
            case "code":
            case "codigo":
                return user.getDoctor().getCode().toLowerCase().contains(key);
            case "starting time":
            case "hora de inicio":
                return user.getDoctor().getShiftStartTime().toLowerCase().contains(key);
            case "ending time":
            case "hora de fin":
                return user.getDoctor().getShiftEndTime().toLowerCase().contains(key);
            default:
                return false;
        }
    }

    private void initializeList() {

        tcCarne.setCellValueFactory(new PropertyValueFactory<>("idCard"));
        tcCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tcStartingTime.setCellValueFactory(new PropertyValueFactory<>("shiftStartTime"));
        tcEndingTIme.setCellValueFactory(new PropertyValueFactory<>("shiftEndTime"));
        tblDoctorsView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    doctorBuffer = newValue;
                    if (doctorBuffer != null) {
                        btnEdit.setDisable(false);
                        return;
                    }
                    btnEdit.setDisable(true);

                });
    }

    private void loadDoctors(List<UserDto> users) {
        if (users != null) {
            List<DoctorDto> doctorDtos = users.stream().filter(user -> user.getDoctor() != null).map(t -> t.getDoctor())
                    .collect(Collectors.toList());
            tblDoctorsView.setItems(FXCollections.observableArrayList(doctorDtos));
        }
    }

}
