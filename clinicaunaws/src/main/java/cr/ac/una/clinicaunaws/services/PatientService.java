package cr.ac.una.clinicaunaws.services;

import cr.ac.una.clinicaunaws.dto.PatientDto;
import cr.ac.una.clinicaunaws.entities.Patient;
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
public class PatientService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     *
     * @param patientDto to be created
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper createPatient(PatientDto patientDto) {
        try {
            Patient patient = patientDto.convertFromDTOToEntity(patientDto, new Patient(patientDto));
            em.persist(patient);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Patient created successfully.",
                    patientDto.convertFromEntityToDTO(patient, patientDto));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while creating patient: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id user id to be retrieved
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper getPatientById(Long id) {
        try {
            Patient patient;
            patient = em.createNamedQuery("Patient.findById", Patient.class).setParameter("id", id).getSingleResult();
            if (patient == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Patient not found, id: " + id,
                        null);
            }
            PatientDto patientDto = new PatientDto(patient);
            patientDto = patientDto.convertFromEntityToDTO(patient, patientDto);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Patient retrieved successfully.",
                    patientDto);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving patient: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getPatients() {
        try {
            Query query = em.createNamedQuery("Patient.findAll", Patient.class);
            List<Patient> patients = (List<Patient>) query.getResultList();
            List<PatientDto> patientsDto = new ArrayList<>();

            for (Patient pat : patients) {
                PatientDto patientDto = new PatientDto(pat);
                patientsDto.add(patientDto.convertFromEntityToDTO(pat, patientDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Patients retrieved successfully.",
                    patientsDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving patients: " + e.getMessage(),
                    null);
        }
    }

    /**
     * @param patientDto User to be updated
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper updatePatient(PatientDto patientDto) {
        try {
            Patient patient;
            patient = em.find(Patient.class, patientDto.getId());
            if (patient == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Doctor not found, id: " + patientDto.getId(),
                        null);
            }
            patient.updatePatient(patientDto);
            em.merge(patient);
            em.flush();
            patientDto = new PatientDto(patient);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Patient updated successfully.",
                    patientDto.convertFromEntityToDTO(patient, patientDto));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while updating patient: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id id from user to be deleted
     * @return ResponseWrapper with the response from database, or null if an
     * exception occurred
     */
    public ResponseWrapper deletePatientById(Long id) {
        try {
            Patient patient;
            patient = em.find(Patient.class, id);
            if (patient == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Patient not found, id: " + id,
                        null);
            }
            em.remove(patient);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Patient deleted successfully.",
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
