package ms.order.service.domain.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER_DD_MM_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD_T_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD_T_HH_MM_SS_Z = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss'Z'");
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD_T_HH_MM = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm");

    private static final DateTimeFormatter FORMATTER_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final List<DateTimeFormatter> formattersWithTime = List.of(
            FORMATTER_DD_MM_YYYY_HH_MM_SS,
            FORMATTER_YYYY_MM_DD_T_HH_MM_SS,
            FORMATTER_YYYY_MM_DD_T_HH_MM_SS_Z,
            FORMATTER_YYYY_MM_DD_T_HH_MM
    );

    private static final List<DateTimeFormatter> formattersWithoutTime = List.of(
            FORMATTER_DD_MM_YYYY,
            FORMATTER_YYYY_MM_DD
    );

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();

        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter dateTimeFormatter: formattersWithTime) {
            try {
                return LocalDateTime.parse(dateString.replace("-", "/"), dateTimeFormatter);
            } catch (Exception ignored) {}
        }

        for (DateTimeFormatter dateTimeFormatter: formattersWithoutTime) {
            try {
                String date = dateString.replace(" ", "T").replace("-", "/").split("T")[0];

                return LocalDateTime.of(LocalDate.parse(date, dateTimeFormatter), LocalTime.MIDNIGHT);
            } catch (Exception ignored) {}
        }

        throw new IOException("Date '" + p.getText() + "' could not be parsed");
    }
}
