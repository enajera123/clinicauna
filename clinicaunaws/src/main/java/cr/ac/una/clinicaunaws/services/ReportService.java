package cr.ac.una.clinicaunaws.services;

import cr.ac.una.clinicaunaws.util.ResponseCode;
import net.sf.jasperreports.engine.*;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import cr.ac.una.clinicaunaws.dto.ReportDto;
import cr.ac.una.clinicaunaws.dto.ReportParametersDto;
import cr.ac.una.clinicaunaws.dto.ReportRecipientsDto;
import cr.ac.una.clinicaunaws.entities.Report;
import cr.ac.una.clinicaunaws.util.Constants;
import cr.ac.una.clinicaunaws.util.ExcelGenerator;
import java.io.File;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author varga
 * @author arayaroma
 */
@Stateless
@LocalBean
public class ReportService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    EmailService emailService;

    /**
     * Get the result of a query and set it to the report
     *
     * @param <D> Generic class type
     * @param report Report to be set
     * @return List of query results
     */
    @SuppressWarnings("unchecked")
    public <D> List<D> getQueryResult(ReportDto report) {
        if (report == null) {
            return null;
        }
        List<ReportParametersDto> params = report.getReportParameters();
        String query = report.getQuery();
        for (ReportParametersDto parameter : params) {
            query = query.replace(":" + parameter.getName(), parameter.getValue());
        }
        System.out.println("Query: " + query);
        List<D> queryResponse = (List<D>) em.createNativeQuery(query).getResultList();
        report.getQueryManager().setResult(queryResponse);
        report.getQueryManager().setStatus(ResponseCode.OK.getCode().toString());
        System.out.println(queryResponse);
        return queryResponse;
    }

    /**
     * Create a new Report
     *
     * @param reportDto to be created
     * @return ResponseWrapper with the created Report
     */
    public ResponseWrapper createReport(ReportDto reportDto) {
        try {
            Report report = reportDto.convertFromDTOToEntity(reportDto, new Report(reportDto));
            em.persist(report);
            em.flush();
            List<?> result = getQueryResult(reportDto);
            reportDto.getQueryManager().setResult(result);
            reportDto.getQueryManager().setQuery(report.getQuery());
            reportDto = new ReportDto(report);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report created.",
                    reportDto.convertFromEntityToDTO(report, reportDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the Report: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a Report by id
     *
     * @param id of the Report to be retrieved
     * @return ResponseWrapper with the retrieved Report
     */
    public ResponseWrapper getReportById(Long id) {
        try {
            Report report = em.createNamedQuery("Report.findById", Report.class)
                    .setParameter("id", id).getSingleResult();
            if (report == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Report not found.",
                        null);
            }
            ReportDto reportDto = new ReportDto(report);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report retrieved.",
                    reportDto.convertFromEntityToDTO(report, reportDto));
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the Report: " + e.toString(),
                    null);
        }
    }

    /**
     * get all Reports
     *
     * @return ResponseWrapper with the retrieved Reports
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReports() {
        try {
            Query query = em.createNamedQuery("Report.findAll", Report.class);
            List<Report> reportList = (List<Report>) query.getResultList();
            List<ReportDto> reportDtoList = new ArrayList<>();
            for (Report report : reportList) {
                ReportDto reportDto = new ReportDto(report);
                reportDtoList.add(reportDto.convertFromEntityToDTO(report, reportDto));
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Reports retrieved.",
                    reportDtoList);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the Reports: " + e.getMessage(),
                    null);
        }
    }

    public void sendReport(ReportDto reportDto) {
        try {
            File file = ExcelGenerator.getInstance().generateExcelReport(reportDto);
            if (file != null) {
                for (ReportRecipientsDto i : reportDto.getReportRecipients()) {
                    emailService.sendGeneratedReport(i.getEmail(), file);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * update a Report
     *
     * @param reportDto to be updated
     * @return ResponseWrapper with the updated Report
     */
    public ResponseWrapper updateReport(ReportDto reportDto) {
        try {
            Report report = em.find(Report.class, reportDto.getId());
            if (report == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Report with id: " + reportDto.getId() + " not found",
                        null);
            }
            report.updateReport(reportDto);
            List<?> result = getQueryResult(reportDto);
            reportDto.getQueryManager().setResult(result);
            reportDto.getQueryManager().setQuery(report.getQuery());

            em.merge(report);
            em.flush();
            reportDto = new ReportDto(report);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report updated.",
                    reportDto.convertFromEntityToDTO(report, reportDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the Report: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a Report by id
     *
     * @param id of the Report to be deleted
     * @return ResponseWrapper with the deleted Report
     */
    public ResponseWrapper deleteReport(Long id) {
        try {
            Report report = em.find(Report.class, id);
            if (report == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Report with id: " + id + " not found",
                        null);
            }
            em.remove(report);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report deleted.",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the Report: " + e.getMessage(),
                    null);
        }
    }

    /**
     * FIXME: add returns null with errors
     *
     * @param id patient id to be retrieved
     * @return ResponseWrapper with the response and report from database, or
     * null if an exception occurred
     * @throws java.io.IOException
     * @throws net.sf.jasperreports.engine.JRException
     */
    public ResponseWrapper createPatientReport(Long id, String language) throws IOException, JRException {

        try {

            JasperReport jReport = compileJasper("medicalRecord");
            ResourceBundle bundle = ResourceBundle.getBundle("jrxml/language", new Locale(language));

            if (jReport == null) {
                System.out.println("ERROR: NO EXISTE EL JASPER");
            }

            Map<String, Object> par = new HashMap<>();
            par.put("idPatientCare", id);
            par.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);  // Pasar el ResourceBundle al informe

            try (Connection connection = DriverManager.getConnection(Constants.URL_DB, Constants.USER_DB,
                    Constants.PASS_DB)) {
                // Llenar el informe con datos y la conexión a la base de datos
                JasperPrint jasperPrint = JasperFillManager.fillReport(jReport, par, connection);
                byte[] pdfbytes = JasperExportManager.exportReportToPdf(jasperPrint);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Report created successfully.",
                        pdfbytes);
            } catch (SQLException e) {
                return new ResponseWrapper(
                        ResponseCode.CONFLICT.getCode(),
                        ResponseCode.CONFLICT,
                        "No connected: " + e.getMessage(),
                        null);
            }
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el informe", e);
        }
    }

    public ResponseWrapper createAgendaReport(Long doctorId, String startDate, String endDate, String language)
            throws IOException, JRException {

        try {

            JasperReport jReport = compileJasper("agendaReport");
            ResourceBundle bundle = ResourceBundle.getBundle("jrxml/language", new Locale(language));

            if (jReport == null) {
                System.out.println("ERROR: NO EXISTE EL JASPER");
            }

            Map<String, Object> par = new HashMap<>();
            par.put("doctorId", doctorId);
            par.put("startDate", startDate);
            par.put("endDate", endDate);
            par.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);  // Pasar el ResourceBundle al informe

            try (Connection connection = DriverManager.getConnection(Constants.URL_DB, Constants.USER_DB,
                    Constants.PASS_DB)) {
                // Llenar el informe con datos y la conexión a la base de datos
                JasperPrint jasperPrint = JasperFillManager.fillReport(jReport, par, connection);
                byte[] pdfbytes = JasperExportManager.exportReportToPdf(jasperPrint);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Report created successfully.",
                        pdfbytes);
            } catch (SQLException e) {
                return new ResponseWrapper(
                        ResponseCode.CONFLICT.getCode(),
                        ResponseCode.CONFLICT,
                        "No connected: " + e.getMessage(),
                        null);
            }
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el informe", e);
        }
    }

    public ResponseWrapper createMedicalExamReport(Long patId, String language)
            throws IOException, JRException {

        JasperReport jReport = compileJasper("medicalExam");
        ResourceBundle bundle = ResourceBundle.getBundle("jrxml/language", new Locale(language));

        try {
            Map<String, Object> par = new HashMap<>();
            par.put("idPatient", patId);
            par.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);  // Pasar el ResourceBundle al informe

            try (Connection connection = DriverManager.getConnection(Constants.URL_DB, Constants.USER_DB,
                    Constants.PASS_DB)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jReport, par, connection);
                byte[] pdfbytes = JasperExportManager.exportReportToPdf(jasperPrint);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Report created successfully.",
                        pdfbytes);
            } catch (SQLException e) {
                return new ResponseWrapper(
                        ResponseCode.CONFLICT.getCode(),
                        ResponseCode.CONFLICT,
                        "No connected: " + e.getMessage(),
                        null);
            }
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el informe", e);
        }
    }

    public JasperReport compileJasper(String jrxmlName) {
        try {
            String absolutePath = "";
            String jasperPath = "jrxml/" + jrxmlName + ".jrxml";
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(jasperPath);

            if (resource == null) {
                System.out.println("No se pudo encontrar el archivo: " + jasperPath);
            } else {
                absolutePath = resource.getFile();
                System.out.println("Ruta absoluta del archivo: " + absolutePath);
            }

            JasperReport jReport = JasperCompileManager.compileReport(absolutePath);

            if (jReport == null) {
                System.out.println("ERROR: NO EXISTE EL JASPER");
                return null;
            }
            return jReport;
        } catch (JRException e) {
            throw new RuntimeException("Error al generar el informe", e);
        }
    }

}
