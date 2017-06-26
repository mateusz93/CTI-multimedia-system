package pl.lodz.p.cti.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.lodz.p.cti.models.ScheduleModel;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActualScheduleFinder {

    private static final String RECURRENCE_SPLITTER = "-";
    private static final String DAYS_SPLITTER = ",";

    public static ScheduleModel findActualSchedule(List<ScheduleModel> scheduleList) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<ScheduleModel> matchingFilter = new ArrayList<>();
        //Filtrowanie bez czasu
        Optional.ofNullable(scheduleList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(schedule -> schedule.getStartTime().compareTo(currentTime) <= 0
                        && schedule.getEndTime().compareTo(currentTime) >= 0)
                .forEach(schedule -> {
                    //Brak powtarzalności
                    log.info(schedule.getRecurrence());
                    if (StringUtils.isEmpty(schedule.getRecurrence())) {
                        matchingFilter.add(schedule);
                    } else {
                        String[] recData = schedule.getRecurrence().split(RECURRENCE_SPLITTER);
                        //Co X dni
                        if (isDay(currentTime, schedule, recData)) {
                            matchingFilter.add(schedule);
                        }
                        //Co X tygodni w [Y...] dniach tygodnia
                        if (isWeek(currentTime, schedule, recData)) {
                            refresh(currentTime, matchingFilter, schedule, recData);
                        }
                    }
                });

        log.info(matchingFilter.toString());
        for (ScheduleModel schedule : matchingFilter) {
            if (StringUtils.isEmpty(schedule.getRecurrence())) {
                return schedule;
            }
            if (schedule.getStartTime().toLocalTime().isBefore(schedule.getEndTime().toLocalTime())
                    && (schedule.getStartTime().toLocalTime().isBefore(currentTime.toLocalTime())
                    || schedule.getStartTime().toLocalTime().equals(currentTime.toLocalTime()))
                    && (schedule.getEndTime().toLocalTime().isAfter(currentTime.toLocalTime())
                    || schedule.getEndTime().toLocalTime().equals(currentTime.toLocalTime()))) {
                return schedule;
            }
            if (schedule.getStartTime().toLocalTime().isAfter(schedule.getEndTime().toLocalTime())
                    && (schedule.getStartTime().toLocalTime().isBefore(currentTime.toLocalTime())
                    || schedule.getStartTime().toLocalTime().equals(currentTime.toLocalTime())
                    || schedule.getEndTime().toLocalTime().isAfter(currentTime.toLocalTime())
                    || schedule.getEndTime().toLocalTime().equals(currentTime.toLocalTime()))) {
                return schedule;
            }
        }
        return null;
    }

    private static void refresh(LocalDateTime currentTime, List<ScheduleModel> matchingFilter, ScheduleModel schedule, String[] recData) {
        String[] days = recData[4].split(DAYS_SPLITTER);
        for (String day : days) {
            int weekDay = Integer.parseInt(day);
            //Sunday fix
            if (weekDay == 0) weekDay = 7;
            if (currentTime.toLocalDate().getDayOfWeek().equals(DayOfWeek.of(weekDay))) {
                matchingFilter.add(schedule);
            }
        }
    }

    private static boolean isWeek(LocalDateTime currentTime, ScheduleModel schedule, String[] recData) {
        return recData[0].equals("week") &&
                ((int) ((currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) / 7)) % Integer.parseInt(recData[1]) == 0;
    }

    private static boolean isDay(LocalDateTime currentTime, ScheduleModel schedule, String[] recData) {
        return recData[0].equals("day") &&
                (currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) % Integer.parseInt(recData[1]) == 0;
    }
}
