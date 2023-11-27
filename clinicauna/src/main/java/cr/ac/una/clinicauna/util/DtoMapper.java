package cr.ac.una.clinicauna.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author arayaroma
 */
public interface DtoMapper<G, D> {

    D convertFromGeneratedToDTO(G generated, D dto);

    G convertFromDTOToGenerated(D dto, G generated);

    public static <E, D> List<D> fromGeneratedList(List<E> generated, Class<D> dtoClass) {
        try {

            if (generated == null || generated.isEmpty()) {
                return new ArrayList<>();
            }
            List<D> dtos = generated.stream()
                    .map(gen -> convertToDto(gen, dtoClass))
                    .collect(Collectors.toList());

            return dtos;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static <E, D> List<E> fromDtoList(List<D> dtos, Class<E> generatedClass) {
        try {

            if (dtos == null || dtos.isEmpty()) {
                return new ArrayList<>();
            }
            List<E> generated = dtos.stream()
                    .map(dto -> convertToGenerated(dto, generatedClass))
                    .collect(Collectors.toList());
            return generated;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static <T, D> D convertToDto(T generated, Class<D> dtoClass) {
        try {
            dtoClass.getConstructor(generated.getClass());
            Constructor<D> constructor = dtoClass.getConstructor(generated.getClass());
            D dto = constructor.newInstance(generated);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting Generated to DTO", e);
        }
    }

    public static <T, D> T convertToGenerated(D dto, Class<T> generatedClass) {
        try {
            Constructor<T> constructor = generatedClass.getConstructor(dto.getClass());// No existe este constructor en
            // las clases mapeadas
            T gen = constructor.newInstance(dto);
            return gen;
        } catch (Exception e) {
            throw new RuntimeException("Error converting DTO to Generated: " + e.toString(), e);
        }
    }
}
