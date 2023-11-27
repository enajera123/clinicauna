package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.GeneralInformationDto;
import cr.ac.una.clinicaunaws.entities.GeneralInformation;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 * 
 * @author arayaroma
 */
@Stateless
@LocalBean
public class GeneralInformationService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new General Information
     * 
     * @param generalInformationDto to be created
     * @return ResponseWrapper with the created General Information
     */
    public ResponseWrapper createGeneralInformation(GeneralInformationDto generalInformationDto) {
        try {
            GeneralInformation generalInformation = new GeneralInformation(generalInformationDto);
            em.persist(generalInformation);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "General Information created.",
                    generalInformationDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the General Information: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Get a General Information by id
     * 
     * @param id of the General Information to be retrieved
     * @return ResponseWrapper with the retrieved General Information
     */
    public ResponseWrapper getGeneralInformationById(Long id) {
        try {
            GeneralInformation generalInformation = em.find(GeneralInformation.class, id);
            if (generalInformation == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "General Information not found.",
                        id);
            }
            GeneralInformationDto generalInformationDto = new GeneralInformationDto(generalInformation);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "General Information found.",
                    generalInformationDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not get the General Information: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Get all General Information
     * 
     * @return ResponseWrapper with the General Information
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllGeneralInformation() {
        try {
            Query query = em.createNamedQuery("GeneralInformation.findAll", GeneralInformation.class);
            List<GeneralInformation> generalInformationList = (List<GeneralInformation>) query.getResultList();
            List<GeneralInformationDto> generalInformationDtos = new ArrayList<>();

            for (GeneralInformation generalInformation : generalInformationList) {
                GeneralInformationDto generalInformationDto = new GeneralInformationDto(generalInformation);
                generalInformationDtos.add(generalInformationDto);
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "General Information found.",
                    generalInformationDtos);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not get the General Information: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Update an existing General Information
     * 
     * @param generalInformationDto to be updated
     * @return ResponseWrapper with the updated General Information
     */
    public ResponseWrapper updateGeneralInformation(GeneralInformationDto generalInformationDto) {
        try {
            GeneralInformation generalInformation = em.find(GeneralInformation.class, generalInformationDto.getId());
            if (generalInformation == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "General Information not found.",
                        null);

            }
            generalInformation.updateGeneralInformation(generalInformationDto);
            em.merge(generalInformation);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "General Information updated.",
                    generalInformationDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the General Information: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Delete an existing General Information
     * 
     * @param id of the General Information to be deleted
     * @return ResponseWrapper with the deleted General Information
     */
    public ResponseWrapper deleteGeneralInformation(Long id) {
        try {
            GeneralInformation generalInformation = em.find(GeneralInformation.class, id);
            if (generalInformation == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "General Information not found.",
                        null);
            }
            em.remove(generalInformation);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "General Information deleted.",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the General Information: " + e.getMessage(),
                    null);
        }
    }

}
