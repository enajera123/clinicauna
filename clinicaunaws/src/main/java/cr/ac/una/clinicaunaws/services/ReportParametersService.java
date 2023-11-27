package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.ReportParametersDto;
import cr.ac.una.clinicaunaws.entities.ReportParameters;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class ReportParametersService {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new ReportValues
     *
     * @param reportParametersDto
     * @param reportValuesDto to be created
     * @return ResponseWrapper with the created ReportValues
     */
    public ResponseWrapper createReportParameters(ReportParametersDto reportParametersDto) {
        try {
            ReportParameters reportParameters = reportParametersDto.convertFromDTOToEntity(reportParametersDto,
                    new ReportParameters(reportParametersDto));
            em.persist(reportParameters);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters created.",
                    reportParametersDto.convertFromEntityToDTO(reportParameters,
                            reportParametersDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the ReportParameters: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a ReportParameters by id
     *
     * @param id of the ReportParameters to be retrieved
     * @return ResponseWrapper with the retrieved ReportParameters
     */
    public ResponseWrapper getReportParametersById(Long id) {
        try {
            ReportParameters reportParameters = em.createNamedQuery("ReportParameters.findById", ReportParameters.class)
                    .setParameter("id", id).getSingleResult();
            if (reportParameters != null) {
                ReportParametersDto reportParametersDto = new ReportParametersDto(reportParameters);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "ReportParameters retrieved.",
                        reportParametersDto.convertFromEntityToDTO(reportParameters,
                                reportParametersDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportParameters with id: " + id + " not found",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the ReportParameters: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all ReportParameters
     *
     * @return ResponseWrapper with all the ReportParameters
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReportParameters() {
        try {
            Query query = em.createNamedQuery("ReportParameters.findAll", ReportParameters.class);
            List<ReportParameters> reportParametersList = (List<ReportParameters>) query.getResultList();
            List<ReportParametersDto> reportParametersDtoList = new ArrayList<>();
            
            for (ReportParameters reportParameters : reportParametersList) {
                ReportParametersDto reportParametersDto = new ReportParametersDto(reportParameters);
                reportParametersDtoList.add(reportParametersDto.convertFromEntityToDTO(reportParameters,
                        reportParametersDto));
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters retrieved.",
                    reportParametersDtoList);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the ReportParameters: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Update a ReportParameters
     *
     * @param reportParametersDto to be updated
     * @return ResponseWrapper with the updated ReportParameters
     */
    public ResponseWrapper updateReportParameters(ReportParametersDto reportParametersDto) {
        try {
            ReportParameters reportParameters = em.find(ReportParameters.class, reportParametersDto.getId());
            if (reportParameters != null) {
                reportParameters.updateReport(reportParametersDto);
                em.merge(reportParameters);
                em.flush();
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "ReportParameters updated.",
                        reportParametersDto.convertFromEntityToDTO(reportParameters,
                                reportParametersDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportParameters with id: " + reportParametersDto.getId() + " not found",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the ReportParameters: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Delete a ReportParameters by id
     *
     * @param id of the ReportParameters to be deleted
     * @return ResponseWrapper informing if the ReportParameters was deleted
     */
    public ResponseWrapper deleteReportParameters(Long id) {
        try {
            ReportParameters reportParameters = em.find(ReportParameters.class, id);
            if (reportParameters != null) {
                em.remove(reportParameters);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "ReportParameters deleted.",
                        null);
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "ReportParameters with id: " + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "ReportParameters could not be deleted: " + e.getMessage(),
                    null);
        }
    }
    
}
