package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.clinicaunaws.dto.AgendaDto;
import cr.ac.una.clinicaunaws.entities.Agenda;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class AgendaService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    /**
     * Create a new Agenda
     *
     * @param agendaDto to be created
     * @return ResponseWrapper with the created Agenda
     */
    public ResponseWrapper createAgenda(AgendaDto agendaDto) {
        try {
            Agenda agenda = agendaDto.convertFromDTOToEntity(agendaDto, new Agenda(agendaDto));
            em.persist(agenda);
            em.flush();
            agendaDto = new AgendaDto(agenda);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Agenda created.",
                    agendaDto.convertFromEntityToDTO(agenda, agendaDto));
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not create the Agenda: " + e.getMessage(),
                    null);
        }
    }

    /**
     *
     * @param id of the Agenda to be retrieved
     * @return ResponseWrapper with the retrieved Agenda
     */
    public ResponseWrapper getAgendaById(Long id) {
        try {
            Agenda agenda = em.createNamedQuery("Agenda.findById", Agenda.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (agenda != null) {
                AgendaDto agendaDto = new AgendaDto(agenda);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Agenda retrieved.",
                        agendaDto.convertFromEntityToDTO(agenda, agendaDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Agenda not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the Agenda: " + e.getMessage(),
                    null);
        }
    }

    /**
     *
     * get all Agenda
     *
     * @return ResponseWrapper with the retrieved Agenda
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllAgenda() {
        try {
            Query query = em.createNamedQuery("Agenda.findAll");
            List<Agenda> agendaList = (List<Agenda>) query.getResultList();
            List<AgendaDto> agendaDtoList = new ArrayList<>();

            for (Agenda agenda : agendaList) {
                AgendaDto agendaDto = new AgendaDto(agenda);
                agendaDtoList.add(agendaDto.convertFromEntityToDTO(agenda, agendaDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Agenda retrieved.",
                    agendaDtoList);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not retrieve the Agenda: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Update a Agenda
     *
     * @param agendaDto to be updated
     * @return ResponseWrapper with the updated Agenda
     */
    public ResponseWrapper updateAgenda(AgendaDto agendaDto) {
        try {
            Agenda agenda = em.find(Agenda.class, agendaDto.getId());
            if (agenda != null) {
                agenda.updateAgenda(agendaDto);
                em.merge(agenda);
                em.flush();
                agendaDto = new AgendaDto(agenda);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Agenda updated.",
                        agendaDto.convertFromEntityToDTO(agenda, agendaDto));
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Agenda not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not update the Agenda: " + e.getMessage(),
                    null);
        }
    }

    /**
     * Delete a Agenda
     *
     * @param id of the Agenda to be deleted
     * @return ResponseWrapper informing if the Agenda was deleted successfully
     * or not
     */
    public ResponseWrapper deleteAgenda(Long id) {
        try {
            Agenda agenda = em.find(Agenda.class, id);
            if (agenda != null) {
                em.remove(agenda);
                em.flush();
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Agenda deleted.",
                        null);
            } else {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Agenda not found.",
                        null);
            }
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Could not delete the Agenda: " + e.getMessage(),
                    null);
        }
    }

}
