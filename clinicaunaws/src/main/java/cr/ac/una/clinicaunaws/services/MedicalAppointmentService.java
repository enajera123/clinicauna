package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.MedicalAppointmentDto;
import cr.ac.una.clinicaunaws.entities.MedicalAppointment;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import jakarta.ejb.EJB;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class MedicalAppointmentService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    EmailService emailService;

    /**
     * Create a new MedicalAppointment
     *
     * @param medicalAppointmentDto to be created
     * @return ResponseWrapper with the created MedicalAppointment
     */
    public ResponseWrapper createMedicalAppointment(MedicalAppointmentDto medicalAppointmentDto) {
        try {
            MedicalAppointment medicalAppointment = medicalAppointmentDto.convertFromDTOToEntity(medicalAppointmentDto,
                    new MedicalAppointment(medicalAppointmentDto));
            System.out.println(medicalAppointment.toString());
            em.persist(medicalAppointment);
            em.flush();
            try {
                sendScheduledEmail(medicalAppointmentDto);
            } catch (Exception e) {
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Appointment created, but email could not be sent: " + e.getMessage(),
                        medicalAppointmentDto);
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalAppointment created.",
                    medicalAppointmentDto.convertFromEntityToDTO(medicalAppointment,
                            medicalAppointmentDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the MedicalAppointment: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get a MedicalAppointment by id
     *
     * @param id of the MedicalAppointment to be retrieved
     * @return ResponseWrapper with the retrieved MedicalAppointment
     */
    public ResponseWrapper getMedicalAppointmentById(Long id) {
        try {
            MedicalAppointment medicalAppointment = em.find(MedicalAppointment.class, id);
            if (medicalAppointment != null) {
                MedicalAppointmentDto medicalAppointmentDto = new MedicalAppointmentDto(medicalAppointment);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "MedicalAppointment found.",
                        medicalAppointmentDto.convertFromEntityToDTO(medicalAppointment,
                                medicalAppointmentDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalAppointment with id:" + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not get the MedicalAppointment: " + e.getMessage(),
                    null);
        }
    }

    /**
     * get all MedicalAppointments
     *
     * @return ResponseWrapper with the retrieved MedicalAppointments
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllMedicalAppointments() {
        try {
            Query query = em.createNamedQuery("MedicalAppointment.findAll");
            List<MedicalAppointment> medicalAppointments = (List<MedicalAppointment>) query.getResultList();
            List<MedicalAppointmentDto> medicalAppointmentDtos = new ArrayList<>();

            for (MedicalAppointment medicalAppointment : medicalAppointments) {
                MedicalAppointmentDto medicalAppointmentDto = new MedicalAppointmentDto(medicalAppointment);
                medicalAppointmentDtos.add(medicalAppointmentDto.convertFromEntityToDTO(medicalAppointment,
                        medicalAppointmentDto));
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalAppointments found.",
                    medicalAppointmentDtos);

        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not get the MedicalAppointments: " + e.getMessage(),
                    null);
        }
    }

    /**
     * update a MedicalAppointment
     *
     * @param medicalAppointmentDto to be updated
     * @return ResponseWrapper with the updated MedicalAppointment
     */
    public ResponseWrapper updateMedicalAppointment(MedicalAppointmentDto medicalAppointmentDto) {
        try {
            MedicalAppointment medicalAppointment = em.find(MedicalAppointment.class,
                    medicalAppointmentDto.getId());
            if (medicalAppointment != null) {
                medicalAppointment.updateMedicalAppointment(medicalAppointmentDto);
                medicalAppointment = medicalAppointmentDto.convertFromDTOToEntity(medicalAppointmentDto, medicalAppointment);
                em.merge(medicalAppointment);
                em.flush();
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "MedicalAppointment updated.",
                        medicalAppointmentDto.convertFromEntityToDTO(medicalAppointment,
                                new MedicalAppointmentDto(medicalAppointment)));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalAppointment with id: " + medicalAppointmentDto.getId() + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the MedicalAppointment: " + e.getMessage(),
                    null);
        }
    }

    /**
     * delete a MedicalAppointment by id
     *
     * @param id to be deleted
     * @return ResponseWrapper with the deleted MedicalAppointment
     */
    public ResponseWrapper deleteMedicalAppointment(Long id) {
        try {
            MedicalAppointment medicalAppointment = em.find(MedicalAppointment.class, id);
            if (medicalAppointment != null) {
                em.remove(medicalAppointment);
                em.flush();
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "MedicalAppointment deleted.",
                        null);
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "MedicalAppointment with id: " + id + " not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the MedicalAppointment: " + e.getMessage(),
                    null);
        }
    }

    private ResponseWrapper sendScheduledEmail(MedicalAppointmentDto meApp) {
        try {
            String language = "ENGLISH";
            if (meApp.getPatient() != null) {
                language = meApp.getPatient().getLanguage();
            }
            emailService.sendScheduledAppointment(meApp, language);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalAppointment created successfully.",
                    meApp);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "MedicalAppointment created successfully, but email could not be sent: " + ex.getMessage(),
                    meApp);
        }
    }

}
