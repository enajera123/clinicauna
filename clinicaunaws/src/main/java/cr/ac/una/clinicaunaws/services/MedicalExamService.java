package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.MedicalExamDto;
import cr.ac.una.clinicaunaws.entities.MedicalExam;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class MedicalExamService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new MedicalExam
     *
     * @param medicalExamDto to be created
     * @return ResponseWrapper with the created MedicalExam
     */
    public ResponseWrapper createMedicalExam(MedicalExamDto medicalExamDto) {
        try {
            MedicalExam medicalExam = medicalExamDto.convertFromDTOToEntity(medicalExamDto,
                    new MedicalExam(medicalExamDto));
            em.persist(medicalExam);
            em.flush();
            medicalExamDto = new MedicalExamDto(medicalExam);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalExam created.",
                    medicalExamDto.convertFromEntityToDTO(medicalExam,
                            medicalExamDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the MedicalExam: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a MedicalExam by id
     *
     * @param id of the MedicalExam to be retrieved
     * @return ResponseWrapper with the retrieved MedicalExam
     */
    public ResponseWrapper getMedicalExamById(Long id) {
        try {
            MedicalExam medicalExam = em.find(MedicalExam.class, id);
            if (medicalExam != null) {
                MedicalExamDto medicalExamDto = new MedicalExamDto(medicalExam);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "MedicalExam retrieved.",
                        medicalExamDto.convertFromEntityToDTO(medicalExam,
                                medicalExamDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalExam with id: " + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the MedicalExam: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all MedicalExam
     *
     * @return ResponseWrapper with all the MedicalExam
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllMedicalExams() {
        try {
            Query query = em.createNamedQuery("MedicalExam.findAll", MedicalExam.class);
            List<MedicalExam> medicalExamList = (List<MedicalExam>) query.getResultList();
            List<MedicalExamDto> medicalExamDtos = new ArrayList<>();

            for (MedicalExam medicalExam : medicalExamList) {
                MedicalExamDto medicalExamDto = new MedicalExamDto(medicalExam);
                medicalExamDtos.add(medicalExamDto.convertFromEntityToDTO(medicalExam, medicalExamDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "All MedicalExam retrieved.",
                    medicalExamDtos);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve all the MedicalExam: " + e.getMessage(),
                    null);
        }
    }

    /**
     * update a MedicalExam
     *
     * @param medicalExamDto to be updated
     * @return ResponseWrapper with the updated MedicalExam
     */
    public ResponseWrapper updateMedicalExam(MedicalExamDto medicalExamDto) {
        try {
            MedicalExam medicalExam = em.find(MedicalExam.class, medicalExamDto.getId());
            if (medicalExam == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalExam not found.",
                        null);
            }
            medicalExam.updateMedicalExam(medicalExamDto);
            em.merge(medicalExam);
            em.flush();
            medicalExamDto = new MedicalExamDto(medicalExam);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalExam updated.",
                    medicalExamDto.convertFromEntityToDTO(medicalExam, medicalExamDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the MedicalExam: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a MedicalExam by id
     *
     * @param id of the MedicalExam to be deleted
     * @return ResponseWrapper informing if the MedicalExam was deleted
     * successfully
     */
    public ResponseWrapper deleteMedicalExam(Long id) {
        try {
            MedicalExam medicalExam = em.find(MedicalExam.class, id);
            if (medicalExam == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalExam not found.",
                        null);
            }
            em.remove(medicalExam);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalExam deleted.",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the MedicalExam: " + e.getMessage(),
                    null);
        }
    }

}
