package com.solaramps.api.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.solaramps.api.constants.Constants.PAGE_SIZE;

@Component
public class Utility {

    public static String getFormattedMillis(long millis) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static LocalDateTime getLocalDateTime(String dateTimeString) {
        if (dateTimeString != null) {
            return LocalDate.parse(dateTimeString).atStartOfDay();
        }
        return null;
    }

    public static Pageable getPageable(int pageNumber, Integer pageSize, String sort, Integer sortDirection) {
        Sort sortBy;
        Sort.Direction direction = null;
        if (sortDirection == null || sortDirection == 1) {
            direction = Sort.Direction.ASC;
        } else if (sortDirection == 2) {
            direction = Sort.Direction.DESC;
        }
        if ("-1".equals(sort)) {
            sortBy = Sort.by(direction, "-1".equals(sort) ? "createdAt" : sort);
        } else {
            List<String> sortColumns = Arrays.stream(sort.split(",")).map(s -> s.trim()).collect(Collectors.toList());
            sortBy = Sort.by(direction, sortColumns.get(0));
            for (int i = 1; i < sortColumns.size(); i++) {
                sortBy = sortBy.and(Sort.by(direction, sortColumns.get(i)));
            }
        }
        return PageRequest.of(pageNumber, pageSize == null ? PAGE_SIZE : pageSize, sortBy);
    }
}
