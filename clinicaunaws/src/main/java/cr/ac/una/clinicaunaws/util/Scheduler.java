package cr.ac.una.clinicaunaws.util;

import cr.ac.una.clinicaunaws.dto.MedicalAppointmentDto;
import cr.ac.una.clinicaunaws.dto.ReportDto;
import cr.ac.una.clinicaunaws.dto.ReportParametersDto;
import cr.ac.una.clinicaunaws.services.EmailService;
import cr.ac.una.clinicaunaws.services.MedicalAppointmentService;
import cr.ac.una.clinicaunaws.services.ReportParametersService;
import cr.ac.una.clinicaunaws.services.ReportService;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author vargas
 */
@Stateless
public class Scheduler {

    @EJB
    MedicalAppointmentService MaService;

    @EJB
    EmailService eService;

    @EJB
    ReportService rService;
    @EJB
    ReportParametersService reportParametersService;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Schedule(second = "0", minute = "*", hour = "*")
    public void checkReminders() {
        System.out.print("Checking reports...");
        checkReports();
    }

    @Schedule(second = "0", minute = "*", hour = "*")
    public void checkMedicalReminders() {
        System.out.print("Checking appointments...");
        checkAppointments();
    }

    public void checkAppointments() {
        try {
            List<MedicalAppointmentDto> appointments = (List<MedicalAppointmentDto>) MaService.getAllMedicalAppointments().getData();
            if (!appointments.isEmpty()) {
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                List<MedicalAppointmentDto> appToSend = appointments.stream()
                        .filter(isSameDayApp(tomorrow))
                        .collect(Collectors.toList());
                for (MedicalAppointmentDto medicalAppointmentDto : appToSend) {
                    String language = "ENGLISH";
                    if (medicalAppointmentDto.getPatient() != null) {
                        language = medicalAppointmentDto.getPatient().getLanguage();
                    }
                    eService.sendAppointmentReminder(medicalAppointmentDto, language);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void checkReports() {
        try {
            List<ReportDto> reports = (List<ReportDto>) rService.getAllReports().getData();
            if (!reports.isEmpty()) {
                LocalDate today = LocalDate.now();
                List<ReportDto> repToSend = reports.stream()
                        .filter(isSameDayRep(today))
                        .collect(Collectors.toList());
                for (ReportDto r : repToSend) {
                    changeReportDate(r);
                    rService.updateReport(r);
                    rService.sendReport(r);
                    for (ReportParametersDto reportParametersDto : r.getReportParameters()) {
                        reportParametersService.updateReportParameters(reportParametersDto);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private Predicate<MedicalAppointmentDto> isSameDayApp(LocalDate compareDate) {
        return appointment -> {
            LocalDate appointmentDate = LocalDate.parse(appointment.getScheduledDate(), dateFormatter);
            return appointmentDate.isEqual(compareDate);
        };
    }

    private Predicate<ReportDto> isSameDayRep(LocalDate compareDate) {
        return report -> {
            LocalDate reportDate = LocalDate.parse(report.getReportDate(), dateFormatter);
            return reportDate.isEqual(compareDate);
        };
    }

    private void changeReportDate(ReportDto report) {
        switch (report.getFrequency()) {
            case "ANNUALLY" ->
                report.setReportDate(plusYears(report.getReportDate(), 1));
            case "MONTHLY" ->
                report.setReportDate(plusMonths(report.getReportDate(), 1));
            case "WEEKLY" ->
                report.setReportDate(plusWeeks(report.getReportDate(), 1));
            case "DAILY" ->
                report.setReportDate(plusDays(report.getReportDate(), 1));
            case "ONCE" ->
                report.setReportDate(plusDays(report.getReportDate(), -1));
            default -> {
            }
        }

        List<ReportParametersDto> parameters = report.getReportParameters();
        for (ReportParametersDto parameter : parameters) {
            if (isDateString(parameter.getValue())) {
                switch (report.getFrequency()) {
                    case "ANNUALLY" ->
                        parameter.setValue(plusYears(parameter.getValue(), 1));
                    case "MONTHLY" ->
                        parameter.setValue(plusMonths(parameter.getValue(), 1));
                    case "WEEKLY" ->
                        parameter.setValue(plusWeeks(parameter.getValue(), 1));
                    case "DAILY" ->
                        parameter.setValue(plusDays(parameter.getValue(), 1));
                    default -> {
                    }
                }
            }
        }
    }

    private String plusYears(String date, int years) {
        LocalDate localDate = LocalDate.parse(date, dateFormatter).plusYears(years);
        return localDate.format(dateFormatter);
    }

    private String plusMonths(String date, int months) {
        LocalDate localDate = LocalDate.parse(date, dateFormatter).plusMonths(months);
        return localDate.format(dateFormatter);
    }

    private String plusWeeks(String date, int weeks) {
        LocalDate localDate = LocalDate.parse(date, dateFormatter).plusWeeks(weeks);
        return localDate.format(dateFormatter);
    }

    private String plusDays(String date, int days) {
        LocalDate localDate = LocalDate.parse(date, dateFormatter).plusDays(days);
        return localDate.format(dateFormatter);
    }

    private boolean isDateString(String value) {
        try {
            LocalDate.parse(value, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
