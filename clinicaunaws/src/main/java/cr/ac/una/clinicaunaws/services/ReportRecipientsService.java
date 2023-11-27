package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.ReportRecipientsDto;
import cr.ac.una.clinicaunaws.entities.ReportRecipients;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class ReportRecipientsService {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new ReportRecipients
     *
     * @param reportRecipientsDto to be created
     * @return ResponseWrapper with the created ReportRecipients
     */
    public ResponseWrapper createReportRecipients(ReportRecipientsDto reportRecipientsDto) {
        try {
            ReportRecipients reportRecipients = reportRecipientsDto.convertFromDTOToEntity(reportRecipientsDto,
                    new ReportRecipients(reportRecipientsDto));
            em.persist(reportRecipients);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportRecipients created.",
                    reportRecipientsDto.convertFromEntityToDTO(reportRecipients,
                            reportRecipientsDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the ReportRecipients: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a ReportRecipients by id
     *
     * @param id of the ReportRecipients to be retrieved
     * @return ResponseWrapper with the retrieved ReportRecipients
     */
    public ResponseWrapper getReportRecipientsById(Long id) {
        try {
            ReportRecipients reportRecipients = em.createNamedQuery("ReportRecipients.findById", ReportRecipients.class)
                    .setParameter("id", id).getSingleResult();
            if (reportRecipients != null) {
                ReportRecipientsDto reportRecipientsDto = new ReportRecipientsDto(reportRecipients);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Success.",
                        reportRecipientsDto.convertFromEntityToDTO(reportRecipients,
                                reportRecipientsDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportRecipients with id: " + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "ReportRecipients could not be retrieved: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all ReportRecipients
     *
     * @return ResponseWrapper with all the ReportRecipients
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReportRecipients() {
        try {
            Query query = em.createNamedQuery("ReportRecipients.findAll", ReportRecipients.class);
            List<ReportRecipients> reportRecipientsList = (List<ReportRecipients>) query.getResultList();
            List<ReportRecipientsDto> reportRecipientsDtoList = new ArrayList<>();
            
            for (ReportRecipients reportRecipients : reportRecipientsList) {
                ReportRecipientsDto reportRecipientsDto = new ReportRecipientsDto(reportRecipients);
                reportRecipientsDtoList.add(reportRecipientsDto.convertFromEntityToDTO(reportRecipients,
                        reportRecipientsDto));
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Success.",
                    reportRecipientsDtoList);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "ReportRecipients could not be retrieved: " + e.getMessage(),
                    null);
        }
    }

    /**
     * update a ReportRecipients
     *
     * @param reportRecipientsDto to be updated
     * @return ResponseWrapper with the updated ReportRecipients
     */
    public ResponseWrapper updateReportRecipients(ReportRecipientsDto reportRecipientsDto) {
        try {
            ReportRecipients reportRecipients = em.find(ReportRecipients.class, reportRecipientsDto.getId());
            if (reportRecipients != null) {
                reportRecipients.updateReportRecipients(reportRecipientsDto);
                em.merge(reportRecipients);
                em.flush();
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "ReportRecipients updated.",
                        reportRecipientsDto.convertFromEntityToDTO(reportRecipients,
                                reportRecipientsDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportRecipients with id: " + reportRecipientsDto.getId() + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "ReportRecipients could not be updated: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a ReportRecipients by id
     *
     * @param id of the ReportRecipients to be deleted
     * @return ResponseWrapper informing if the ReportRecipients was deleted
     */
    public ResponseWrapper deleteReportRecipients(Long id) {
        try {
            ReportRecipients reportRecipients = em.find(ReportRecipients.class, id);
            if (reportRecipients != null) {
                em.remove(reportRecipients);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "ReportRecipients deleted.",
                        null);
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportRecipients with id: " + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "ReportRecipients could not be deleted: " + e.getMessage(),
                    null);
        }
    }
    
}
