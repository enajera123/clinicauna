package cr.ac.una.clinicaunaws.services;

import cr.ac.una.clinicaunaws.dto.DoctorDto;
import cr.ac.una.clinicaunaws.entities.Doctor;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vargas
 * @author arayaroma
 */
@Stateless
@LocalBean
public class DoctorService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     *
     * @param doctorDto to be created
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper createDoctor(DoctorDto doctorDto) {
        try {
            Doctor doctor = new Doctor(doctorDto);
            doctor = doctorDto.convertFromDTOToEntity(doctorDto, doctor);
            em.persist(doctor);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Doctor created successfully.",
                    doctorDto.convertFromEntityToDTO(doctor, doctorDto));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while creating doctor: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id user id to be retrieved
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper getDoctorById(Long id) {
        try {
            Doctor doctor;
            doctor = em.createNamedQuery("Doctor.findById", Doctor.class).setParameter("id", id).getSingleResult();
            if (doctor == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Doctor not found, id: " + id + ")",
                        null);
            }
            DoctorDto doctorDto = new DoctorDto(doctor);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Doctor retrieved successfully.",
                    doctorDto.convertFromEntityToDTO(doctor, doctorDto));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving doctor: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getDoctors() {
        try {
            Query query = em.createNamedQuery("Doctor.findAll", Doctor.class);
            List<Doctor> doctors = (List<Doctor>) query.getResultList();
            List<DoctorDto> doctorsDto = new ArrayList<>();

            for (Doctor doc : doctors) {
                DoctorDto doctorDto = new DoctorDto(doc);
                doctorsDto.add(doctorDto.convertFromEntityToDTO(doc, doctorDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Doctors retrieved successfully.",
                    doctorsDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving users: " + e.getMessage(),
                    null);
        }
    }

    /**
     * @param doctorDto User to be updated
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper updateDoctor(DoctorDto doctorDto) {
        try {
            Doctor doctor;
            doctor = em.find(Doctor.class, doctorDto.getId());
            if (doctor == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Doctor not found, id: " + doctorDto.getId() + ")",
                        null);
            }
            doctor.updateDoctor(doctorDto);
            em.merge(doctor);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Doctor updated successfully.",
                    new DoctorDto(doctor));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while updating doctor: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id id from user to be deleted
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper deleteDoctorById(Long id) {
        try {
            Doctor doctor;
            doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Doctor not found, id: " + id + ")",
                        null);
            }
            em.remove(doctor);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Doctor deleted successfully.",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while deleting doctor: " + ex.getMessage(),
                    null);
        }
    }

}
