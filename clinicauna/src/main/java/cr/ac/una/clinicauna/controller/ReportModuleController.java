package cr.ac.una.clinicauna.controller;

import cr.ac.una.clinicauna.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author estebannajera
 */
public class ReportModuleController implements Initializable {

    @FXML
    private VBox parent;
    @FXML
    private VBox container;
    @FXML
    private Button btnDoctorReport;
    @FXML
    private Button btnPatientReport;
    @FXML
    private Button btnReportGenerator;
    @FXML
    private Button btnMedicalExamReport;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        doctorReportAction(null);
    }

    @FXML
    private void doctorReportAction(ActionEvent event) {
        try {
            FXMLLoader loader = App.getFXMLLoader("DoctorReport");
            container.getChildren().clear();
            container.getChildren().add(loader.load());
            btnReportGenerator.getStyleClass().remove("tab-selected");
            btnPatientReport.getStyleClass().remove("tab-selected");
            btnMedicalExamReport.getStyleClass().remove("tab-selected");
            btnDoctorReport.getStyleClass().remove("tab-selected");
            btnDoctorReport.getStyleClass().add("tab-selected");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void patientReportAction(ActionEvent event) {
        try {
            FXMLLoader loader = App.getFXMLLoader("PatientCareReport");
            container.getChildren().clear();
            container.getChildren().add(loader.load());
            btnDoctorReport.getStyleClass().remove("tab-selected");
            btnReportGenerator.getStyleClass().remove("tab-selected");
            btnMedicalExamReport.getStyleClass().remove("tab-selected");
            btnPatientReport.getStyleClass().remove("tab-selected");
            btnPatientReport.getStyleClass().add("tab-selected");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void reportGeneratorAction(ActionEvent event) {
        try {
            FXMLLoader loader = App.getFXMLLoader("ReportGeneratorModule");
            container.getChildren().clear();
            container.getChildren().add(loader.load());
            btnDoctorReport.getStyleClass().remove("tab-selected");
            btnPatientReport.getStyleClass().remove("tab-selected");
            btnMedicalExamReport.getStyleClass().remove("tab-selected");
            btnReportGenerator.getStyleClass().remove("tab-selected");
            btnReportGenerator.getStyleClass().add("tab-selected");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void btnMedicalExamReportAction(ActionEvent event) {
        try {
            FXMLLoader loader = App.getFXMLLoader("PatientModule");
            container.getChildren().clear();
            container.getChildren().add(loader.load());
            btnDoctorReport.getStyleClass().remove("tab-selected");
            btnPatientReport.getStyleClass().remove("tab-selected");
            btnReportGenerator.getStyleClass().remove("tab-selected");
            btnMedicalExamReport.getStyleClass().remove("tab-selected");
            btnMedicalExamReport.getStyleClass().add("tab-selected");
            PatientModuleController controller = loader.getController();
            if (controller != null) {
                controller.loadView("medicalexamreport");
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void loadView(String option) {
        if (option != null) {
            option = option.toLowerCase();
            if (option.equals("patientreport")) {
                patientReportAction(null);
            }
            if (option.equals("reportgenerator")) {
                reportGeneratorAction(null);
            }
            if (option.equals("doctorreport")) {
                doctorReportAction(null);
            }
            if (option.equals("medicalexamreport")) {
                btnMedicalExamReportAction(null);
            }
        }
    }

}
