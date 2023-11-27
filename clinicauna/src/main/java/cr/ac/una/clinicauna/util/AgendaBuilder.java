package cr.ac.una.clinicauna.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arayaroma
 */
public class AgendaBuilder {

    public List<LocalDate> calculateWeekDays(LocalDate inputDayOfWeek) {
        List<LocalDate> result = new ArrayList<>();
        LocalDate firstDayOfWeek = inputDayOfWeek.withDayOfMonth(inputDayOfWeek.getDayOfMonth()).with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            result.add(firstDayOfWeek);
            firstDayOfWeek = firstDayOfWeek.plusDays(1);
        }
        return result;
    }

    private AgendaBuilder(LocalDate actualDate) {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private LocalDate actualDate = LocalDate.now();

        public Builder withActualDate(LocalDate actualDate) {
            this.actualDate = actualDate;
            return this;
        }

        public AgendaBuilder build() {
            return new AgendaBuilder(actualDate);
        }

    }
}
