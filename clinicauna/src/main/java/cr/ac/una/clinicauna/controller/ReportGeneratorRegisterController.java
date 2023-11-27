package cr.ac.una.clinicauna.controller;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.animations.Animate;
import cr.ac.una.clinicauna.model.ReportDto;
import cr.ac.una.clinicauna.model.ReportParametersDto;
import cr.ac.una.clinicauna.model.ReportRecipientsDto;
import cr.ac.una.clinicauna.services.ReportParametersService;
import cr.ac.una.clinicauna.services.ReportRecipientsService;
import cr.ac.una.clinicauna.services.ReportService;
import cr.ac.una.clinicauna.util.Data;
import cr.ac.una.clinicauna.util.Message;
import cr.ac.una.clinicauna.util.MessageType;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author arayaroma
 */
public class ReportGeneratorRegisterController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private DatePicker dpReportDate;
    @FXML
    private ComboBox<String> cbReportFrequency;
    @FXML
    private TableView<ReportParametersDto> tbParameters;
    @FXML
    private TableColumn<ReportParametersDto, String> tcParameter;
    @FXML
    private TableColumn<ReportParametersDto, String> tcValue;
    @FXML
    private JFXTextField txfParameterValue;
    @FXML
    private JFXTextField txfRecipientEmail;
    @FXML
    private JFXTextField txfReportName;
    @FXML
    private JFXTextField txfReportDescription;
    @FXML
    private JFXTextArea txfReportQuery;
    @FXML
    private TableView<ReportRecipientsDto> tbEmails;
    @FXML
    private TableColumn<ReportRecipientsDto, String> tcEmail;

    private ReportService reportService = new ReportService();
    private ReportRecipientsService reportRecipientService = new ReportRecipientsService();
    private ReportParametersService reportParametersService = new ReportParametersService();
    private ReportDto reportBuffer;
    private ReportParametersDto reportParameterBuffer;
    private ReportRecipientsDto reportRecipientBuffer;
    private boolean isEditing;
    private Data data = Data.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbReportFrequency.getItems().addAll("ONCE", "DAILY", "WEEKLY", "MONTHLY", "ANNUALLY");

        reportBuffer = (ReportDto) data.getData("reportBuffer");
        reportBuffer = reportBuffer == null ? new ReportDto() : reportBuffer;
        isEditing = reportBuffer.getId() != null;
        bindReport();
        initializeList();
    }

    @FXML
    private void btnLoadParametersAction(ActionEvent event) {
        if (!reportBuffer.getQuery().isBlank()) {
            List<String> parameter = extractParameters(reportBuffer.getQuery());
            if (cleanParameters(reportBuffer.getReportParameters())) {
                reportBuffer.getReportParameters().clear();
                for (String i : parameter) {
                    ReportParametersDto reportParametersDto = new ReportParametersDto();
                    reportParametersDto.setName(i);
                    reportBuffer.getReportParameters().add(reportParametersDto);
                }
                loadParameters();
            }
        }
    }

    @FXML
    private void btnSaveAction(ActionEvent event) {
        if (!verifyFields()) {
            Message.showNotification("Error", MessageType.INFO, "fieldsEmpty");
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    List<ReportRecipientsDto> recipientsDtos = reportBuffer.getReportRecipients();
                    ResponseWrapper response = isEditing ? reportService.updateReport(reportBuffer)
                            : reportService.createReport(reportBuffer);
                    if (response.getCode() == ResponseCode.OK) {
                        ReportDto report = (ReportDto) response.getData();
                        if (!saveReportRecipients(recipientsDtos, report)) {
                            Message.showNotification("ERROR", MessageType.ERROR, "errorSavingEmails");
                        }
                        if (!saveReportParameters(reportBuffer.getReportParameters(), report)) {
                            Message.showNotification("ERROR", MessageType.ERROR, "errorSavingParameters");
                        }
                        Message.showNotification("Success", MessageType.INFO, response.getMessage());
                        backAction(null);
                        return;
                    }
                    Message.showNotification("ERROR", MessageType.ERROR, response.getMessage());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });
        }).start();
    }

    @FXML
    private void backAction(MouseEvent event) {
        try {
            FXMLLoader loader = App.getFXMLLoader("Main");
            Animate.MakeDefaultFadeTransition(parent, loader.load());
            MainController controller = loader.getController();
            if (controller != null) {
                Data.getInstance().setData("option", "reportgenerator");
                controller.loadView("reportModule");
            }
            data.removeData("reportBuffer");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void btnAddEmailAction(ActionEvent event) {
        String email = txfRecipientEmail.getText();
        if (!email.isBlank()) {
            ReportRecipientsDto recipientsDto = new ReportRecipientsDto();
            recipientsDto.setEmail(email);
            if (reportRecipientBuffer == null) {
                reportBuffer.getReportRecipients().add(recipientsDto);
            } else {
                reportRecipientBuffer.setEmail(email);
                reportBuffer.getReportRecipients().remove(reportRecipientBuffer);
                reportBuffer.getReportRecipients().add(reportRecipientBuffer);
            }
            loadEmails();
            txfRecipientEmail.setText("");
            reportRecipientBuffer = null;
        }
    }

    @FXML
    private void btnDeleteEmailAction(ActionEvent event) {
        if (reportRecipientBuffer != null) {
            if (reportRecipientBuffer.getId() != null) {
                ResponseWrapper response = reportRecipientService.deleteReportRecipients(reportRecipientBuffer.getId());
                if (response.getCode() == ResponseCode.OK) {
                    reportBuffer.getReportRecipients().remove(reportRecipientBuffer);
                    loadEmails();
                    txfRecipientEmail.setText("");
                    return;
                }
                Message.showNotification("ERROR", MessageType.ERROR, response.getMessage());
            } else {
                reportBuffer.getReportRecipients().remove(reportRecipientBuffer);
                loadEmails();
            }
        }
    }

    @FXML
    private void btnAddValueParameterAction(ActionEvent event) {
        if (reportParameterBuffer != null) {
            reportParameterBuffer.setValue(txfParameterValue.getText());
            reportBuffer.getReportParameters().remove(reportParameterBuffer);
            reportBuffer.getReportParameters().add(reportParameterBuffer);
            loadParameters();
        }
    }

    @FXML
    private void reportDateAction(ActionEvent event) {
        LocalDate date = dpReportDate.getValue();
        if (date != null && date.isBefore(LocalDate.now())) {
            Message.showNotification("INFO", MessageType.INFO, "dateInvalid");
            dpReportDate.setValue(null);
            return;
        }
    }

    private boolean cleanParameters(List<ReportParametersDto> reportParametersDtos) {
        for (ReportParametersDto i : reportParametersDtos) {
            if (i.getId() != null) {
                ResponseWrapper response = reportParametersService.deleteReportParameters(i.getId());
                if (response.getCode() != ResponseCode.OK) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean saveReportRecipients(List<ReportRecipientsDto> recipientsDtos, ReportDto reportDto) {
        for (ReportRecipientsDto i : recipientsDtos) {
            i.setReport(new ReportDto(reportDto));
            ResponseWrapper response = i.getId() == null ? reportRecipientService.createReportRecipients(i)
                    : reportRecipientService.updateReportRecipients(i);
            if (response.getCode() != ResponseCode.OK) {
                return false;
            }
        }
        return true;
    }

    private boolean saveReportParameters(List<ReportParametersDto> parametersDtos, ReportDto reportDto) {
        for (ReportParametersDto i : parametersDtos) {
            i.setReport(new ReportDto(reportDto));
            ResponseWrapper response = i.getId() == null ? reportParametersService.createReportParameters(i)
                    : reportParametersService.updateReportParameters(i);
            if (response.getCode() != ResponseCode.OK) {
                return false;
            }
        }
        return true;
    }

    private void loadParameters() {
        tbParameters.getItems().clear();
        tbParameters.setItems(FXCollections.observableArrayList(reportBuffer.getReportParameters()));
    }

    private void loadEmails() {
        tbEmails.getItems().clear();
        tbEmails.setItems(FXCollections.observableArrayList(reportBuffer.getReportRecipients()));
    }

    private void initializeList() {
        tcParameter.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbEmails.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    reportRecipientBuffer = newValue;
                    if (reportRecipientBuffer != null) {
                        txfRecipientEmail.setText(reportRecipientBuffer.getEmail());
                    }
                });
        tbParameters.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    reportParameterBuffer = newValue;
                    if (reportParameterBuffer != null) {
                        txfParameterValue.setText(reportParameterBuffer.getValue());
                    }
                });
    }

    private List<String> extractParameters(String query) {
        Pattern pattern = Pattern.compile(":\\w+");
        Matcher matcher = pattern.matcher(query);
        List<String> parameters = new ArrayList<>();
        while (matcher.find()) {
            parameters.add(matcher.group().substring(1));
        }
        return parameters;
    }

    private void bindReport() {
        txfReportDescription.textProperty().bindBidirectional(reportBuffer.description);
        txfReportName.textProperty().bindBidirectional(reportBuffer.name);
        txfReportQuery.textProperty().bindBidirectional(reportBuffer.query);
        dpReportDate.valueProperty().bindBidirectional(reportBuffer.reportDate);
        cbReportFrequency.valueProperty().bindBidirectional(reportBuffer.frequency);
        loadEmails();
        loadParameters();
    }

    private boolean verifyFields() {
        List<Node> fields = Arrays.asList(txfReportDescription, txfReportName, txfReportQuery, cbReportFrequency,
                dpReportDate);
        for (Node i : fields) {
            if (i instanceof JFXTextField && ((JFXTextField) i).getText() != null
                    && ((JFXTextField) i).getText().isBlank()) {
                return false;
            } else if (i instanceof ComboBox && ((ComboBox) i).getValue() == null) {
                return false;
            } else if (i instanceof DatePicker && ((DatePicker) i).getValue() == null) {
                return false;
            }
        }
        return true;
    }

}
