package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.PatientCareDto;
import cr.ac.una.clinicaunaws.entities.PatientCare;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import jakarta.ejb.EJB;

/**
 * 
 * @author arayaroma
 */
@Stateless
@LocalBean
public class PatientCareService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    @EJB
    EmailService eService;

    /**
     * Create a new PatientCare
     * 
     * @param patientCareDto to be created
     * @return ResponseWrapper with the created PatientCare
     */
     public ResponseWrapper createPatientCare(PatientCareDto patientCareDto) {
        try {
            System.out.println(patientCareDto.toString());
            PatientCare patientCare = patientCareDto.convertFromDTOToEntity(patientCareDto,
                    new PatientCare(patientCareDto));
            em.persist(patientCare);
            em.flush();
            try{
                eService.sendAppointmentResults(patientCareDto);
            }catch(Exception e){
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "PatientCare created, but email could not be sent: " + e.getMessage(),
                        patientCareDto);
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientCare created.",
                    patientCareDto.convertFromEntityToDTO(patientCare, patientCareDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the PatientCare: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a PatientCare by id
     * 
     * @param id of the PatientCare to be retrieved
     * @return ResponseWrapper with the retrieved PatientCare
     */
    public ResponseWrapper getPatientCareById(Long id) {
        try {
            PatientCare patientCare = em.createNamedQuery("PatientCare.findById", PatientCare.class).setParameter("id", id).getSingleResult();
            if (patientCare == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientCare with id: " + id + " not found",
                        null);
            }
            PatientCareDto patientCareDto = new PatientCareDto();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientCare retrieved.",
                    patientCareDto.convertFromEntityToDTO(patientCare, patientCareDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the PatientCare: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all PatientCare
     * 
     * @return ResponseWrapper with the retrieved PatientCare
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllPatientCare() {
        try {
            Query query = em.createNamedQuery("PatientCare.findAll", PatientCare.class);
            List<PatientCare> patientCareList = (List<PatientCare>) query.getResultList();
            List<PatientCareDto> patientCareDtos = new ArrayList<>();

            for (PatientCare patientCare : patientCareList) {
                PatientCareDto patientCareDto = new PatientCareDto();
                patientCareDtos.add(patientCareDto.convertFromEntityToDTO(patientCare, patientCareDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientCare retrieved.",
                    patientCareDtos);

        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the PatientCare: " + e.getMessage(),
                    null);
        }
    }

    /**
     * update a PatientCare
     * 
     * @param patientCareDto to be updated
     * @return ResponseWrapper with the updated PatientCare
     */
    public ResponseWrapper updatePatientCare(PatientCareDto patientCareDto) {
        try {
            PatientCare patientCare = em.find(PatientCare.class, patientCareDto.getId());
            if (patientCare == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientCare not found.",
                        null);
            }
            patientCare.updatePatientCare(patientCareDto);
            em.merge(patientCare);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientCare updated.",
                    patientCareDto.convertFromEntityToDTO(patientCare, patientCareDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the PatientCare: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a PatientCare by id
     * 
     * @param id to be deleted
     * @return ResponseWrapper with the deleted PatientCare
     */
    public ResponseWrapper deletePatientCare(Long id) {
        try {
            PatientCare patientCare = em.find(PatientCare.class, id);
            if (patientCare == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "PatientCare not found.",
                        null);
            }
            em.remove(patientCare);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "PatientCare deleted.",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the PatientCare: " + e.getMessage(),
                    null);
        }
    }

}
