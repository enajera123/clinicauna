package cr.ac.una.clinicaunaws.util;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 
 * @author arayaroma
 */
public interface DtoMapper<E, D> {

    static final Logger log = Logger.getLogger(DtoMapper.class.getName());

    D convertFromEntityToDTO(E entity, D dto);

    E convertFromDTOToEntity(D dto, E entity);

    /**
     * FIXME: Need to test this.
     * 
     * @param <E>
     * @param <D>
     * @param entities
     * @param dtoClass
     * @return
     */
    public static <E, D> List<D> fromEntityList(List<E> entities, Class<D> dtoClass) {
        try {
            List<D> dtos = entities.stream()
                    .map(entity -> convertToDto(entity, dtoClass))
                    .collect(Collectors.toList());
            return dtos;

        } catch (Exception e) {
            log.severe("Error converting entity list to DTO list: " + e.getMessage());
            return null;
        }
    }

    /**
     * FIXME: Need to test this.
     * 
     * @param <E>
     * @param <D>
     * @param dtos
     * @param entityClass
     * @return
     */
    public static <E, D> List<E> fromDtoList(List<D> dtos, Class<E> entityClass) {
        try {
            List<E> entities = dtos.stream()
                    .map(dto -> convertToEntity(dto, entityClass))
                    .collect(Collectors.toList());
            return entities;
        } catch (Exception e) {
            log.severe("Error converting DTO list to entity list: " + e.getMessage());
            return null;
        }
    }

    public static <T, D> D convertToDto(T entity, Class<D> dtoClass) {
        try {
            dtoClass.getConstructor(entity.getClass());
            Constructor<D> constructor = dtoClass.getConstructor(entity.getClass());
            D dto = constructor.newInstance(entity);
            return dto;
        } catch (Exception e) {
            log.severe("Error converting entity to DTO: " + e.getMessage());
            return null;
        }
    }

    public static <T, D> T convertToEntity(D dto, Class<T> entityClass) {
        try {
            Constructor<T> constructor = entityClass.getConstructor(dto.getClass());
            T entity = constructor.newInstance(dto);
            return entity;
        } catch (Exception e) {
            log.severe("Error converting DTO to entity: " + e.getMessage());
            return null;
        }
    }
}
