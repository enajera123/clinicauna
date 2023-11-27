package cr.ac.una.clinicauna.controller;

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
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class ReportGeneratorModuleController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private JFXTextField txfSearchReport;
    @FXML
    private ComboBox<String> cbSearchParameter;
    @FXML
    private TableView<ReportDto> tblReportsView;
    @FXML
    private TableColumn<ReportDto, String> tcQuery;
    @FXML
    private TableColumn<ReportDto, String> tcDate;
    @FXML
    private TableColumn<ReportDto, String> tcFrequency;
    @FXML
    private HBox containerButtons;
    @FXML
    private Button btnEdit;

    private List<ReportDto> reportDtos = new ArrayList<>();
    private ReportService reportService = new ReportService();
    private ReportRecipientsService recipientsService = new ReportRecipientsService();
    private ReportParametersService reportParametersService = new ReportParametersService();
    private Data data = Data.getInstance();
    private ReportDto reportBuffer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (data.getLanguageOption().equals("en")) {
            cbSearchParameter.getItems().addAll("Consulta", "Fecha", "Frecuencia");
        } else {
            cbSearchParameter.getItems().addAll("Query", "Date", "Frequency");
        }
        btnEdit.setDisable(true);
        initializeList();
        reportDtos = (List<ReportDto>) reportService.getAllReports().getData();
        loadReports(reportDtos);

        txfSearchReport.setOnKeyPressed(e -> searchReportAction(e));
    }

    @FXML
    private void btnNewReport(ActionEvent event) throws IOException {
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("ReportGeneratorRegister").load());
    }

    @FXML
    private void btnEditReportAction(ActionEvent event) throws IOException {
        data.setData("reportBuffer", reportBuffer);
        Animate.MakeDefaultFadeTransition(parent, App.getFXMLLoader("ReportGeneratorRegister").load());

    }

    @FXML
    private void btnDeleteReportAction(ActionEvent event) {
        if (reportBuffer != null) {
            if (deleteParameters(reportBuffer.getReportParameters()) && deleteRecipients(reportBuffer.getReportRecipients())) {
                ResponseWrapper response = reportService.deleteReport(reportBuffer.getId());
                if (response.getCode() == ResponseCode.OK) {
                    tblReportsView.getItems().remove(reportBuffer);
                }
            }
        }
    }

    private boolean deleteParameters(List<ReportParametersDto> reportParametersDtos) {
        for (ReportParametersDto i : reportParametersDtos) {
            ResponseWrapper response = reportParametersService.deleteReportParameters(i.getId());
            if (response.getCode() != ResponseCode.OK) {
                return false;
            }
        }
        return true;
    }

    private boolean deleteRecipients(List<ReportRecipientsDto> recipientsDtos) {
        for (ReportRecipientsDto i : recipientsDtos) {
            ResponseWrapper response = recipientsService.deleteReportRecipients(i.getId());
            if (response.getCode() != ResponseCode.OK) {
                return false;
            }
        }
        return true;
    }

    private void searchReportAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String key = txfSearchReport.getText(), parameterKey = cbSearchParameter.getValue();
            if (key.isBlank() || parameterKey == null) {
                loadReports(reportDtos);
                return;
            }
            loadReports(filterReports(reportDtos, parameterKey, key));
        }
    }

    private List<ReportDto> filterReports(List<ReportDto> reports, String parameter, String key) {
        List<ReportDto> reportsFiltered = new ArrayList<>();
        if (reports != null) {
            parameter = parameter.toLowerCase();
            if (parameter.equals("frequency") || parameter.equals("frecuencia")) {
                reportsFiltered = reports
                        .stream()
                        .filter(report -> report.getFrequency().toLowerCase().contains(key))
                        .collect(Collectors.toList());
            } else if (parameter.equals("consulta") || parameter.equals("query")) {
                reportsFiltered = reports
                        .stream()
                        .filter(report -> report.getQuery().toLowerCase().contains(key))
                        .collect(Collectors.toList());
            } else if (parameter.equals("fecha") || parameter.equals("date")) {
                reportsFiltered = reports
                        .stream()
                        .filter(report -> report.getReportDate().toLowerCase().contains(key))
                        .collect(Collectors.toList());
            }
        }
        return reportsFiltered;
    }

    private void loadReports(List<ReportDto> reports) {
        tblReportsView.setItems(FXCollections.observableArrayList(reports));
    }

    private void initializeList() {
        tcDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        tcFrequency.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tcQuery.setCellValueFactory(new PropertyValueFactory<>("query"));
        tblReportsView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    reportBuffer = newValue;
                    if (reportBuffer != null) {
                        btnEdit.setDisable(false);
                        return;
                    }
                    btnEdit.setDisable(true);
                });
    }

}
