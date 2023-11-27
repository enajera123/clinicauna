package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.PatientFamilyHistoryDto;
import cr.ac.una.clinicaunaws.entities.PatientFamilyHistory;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 * 
 * @author arayaroma
 */
@Stateless
@LocalBean
public class PatientFamilyHistoryService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new PatientFamilyHistory
     * 
     * @param patientFamilyHistoryDto to be created
     * @return ResponseWrapper with the created PatientFamilyHistory
     */
    public ResponseWrapper createPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto) {
        try {
            PatientFamilyHistory patientFamilyHistory = patientFamilyHistoryDto
                    .convertFromDTOToEntity(patientFamilyHistoryDto,
                            new PatientFamilyHistory(patientFamilyHistoryDto));
            em.persist(patientFamilyHistory);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientFamilyHistory created.",
                    patientFamilyHistoryDto.convertFromEntityToDTO(patientFamilyHistory,
                            patientFamilyHistoryDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the PatientFamilyHistory: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a PatientFamilyHistory by id
     * 
     * @param id of the PatientFamilyHistory to be retrieved
     * @return ResponseWrapper with the retrieved PatientFamilyHistory
     */
    public ResponseWrapper getPatientFamilyHistoryById(Long id) {
        try {
            PatientFamilyHistory patientFamilyHistory = em.find(PatientFamilyHistory.class, id);
            if (patientFamilyHistory == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientFamilyHistory with id: " + id + " not found",
                        null);
            }
            PatientFamilyHistoryDto patientFamilyHistoryDto = new PatientFamilyHistoryDto(patientFamilyHistory);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientFamilyHistory retrieved.",
                    patientFamilyHistoryDto.convertFromEntityToDTO(patientFamilyHistory, patientFamilyHistoryDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the PatientFamilyHistory: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all PatientFamilyHistory
     * 
     * @return ResponseWrapper with the retrieved PatientFamilyHistory
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllPatientFamilyHistory() {
        try {
            Query query = em.createNamedQuery("PatientFamilyHistory.findAll", PatientFamilyHistory.class);
            List<PatientFamilyHistory> patientFamilyHistoryList = (List<PatientFamilyHistory>) query
                    .getResultList();
            List<PatientFamilyHistoryDto> patientFamilyHistoryDtoList = new ArrayList<>();

            for (PatientFamilyHistory patientFamilyHistory : patientFamilyHistoryList) {
                PatientFamilyHistoryDto patientFamilyHistoryDto = new PatientFamilyHistoryDto(patientFamilyHistory);
                patientFamilyHistoryDtoList
                        .add(patientFamilyHistoryDto.convertFromEntityToDTO(patientFamilyHistory,
                                patientFamilyHistoryDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientFamilyHistory retrieved.",
                    patientFamilyHistoryDtoList);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the PatientFamilyHistory: " + e.getMessage(),
                    null);
        }
    }

    /**
     * update a PatientFamilyHistory
     * 
     * @param patientFamilyHistoryDto to be updated
     * @return ResponseWrapper with the updated PatientFamilyHistory
     */
    public ResponseWrapper updatePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto) {
        try {
            PatientFamilyHistory patientFamilyHistory = em.find(PatientFamilyHistory.class,
                    patientFamilyHistoryDto.getId());
            if (patientFamilyHistory == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientFamilyHistory with id: " + patientFamilyHistoryDto.getId() + " not found.",
                        null);
            }
            patientFamilyHistory.updatePatientFamilyHistory(patientFamilyHistoryDto);
            em.merge(patientFamilyHistory);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientFamilyHistory updated.",
                    patientFamilyHistoryDto.convertFromEntityToDTO(patientFamilyHistory,
                            patientFamilyHistoryDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the PatientFamilyHistory: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a PatientFamilyHistory by id
     * 
     * @param id of the PatientFamilyHistory to be deleted
     * @return ResponseWrapper informing if the PatientFamilyHistory was deleted
     */
    public ResponseWrapper deletePatientFamilyHistory(Long id) {
        try {
            PatientFamilyHistory patientFamilyHistory = em.find(PatientFamilyHistory.class, id);
            if (patientFamilyHistory == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientFamilyHistory with id: " + id + " not found.",
                        null);
            }
            em.remove(patientFamilyHistory);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientFamilyHistory deleted.",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the PatientFamilyHistory: " + e.getMessage(),
                    null);
        }
    }

}
