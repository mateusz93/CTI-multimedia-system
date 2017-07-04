package pl.lodz.p.cti.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.repository.ScheduleRepository;
import pl.lodz.p.cti.services.CommonService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CronScheduler {

    private final ScheduleRepository scheduleRepository;
    private final CommonService commonService;

    private static final String RECURRENCE_SPLITTER = "-";
    private static final String DAYS_SPLITTER = ",";

    @Scheduled(cron = "0 * * * * ?")
    public void toExecuteEveryMinute() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Scheduler started.");

        scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getStartTime().compareTo(currentTime) <= 0
                        && schedule.getEndTime().compareTo(currentTime) >= 0
                        && schedule.getStartTime().getHour() == currentTime.getHour()
                        && schedule.getStartTime().getMinute() == currentTime.getMinute())
                .forEach(schedule -> {
                    //Brak powtarzalności
                    if (StringUtils.isEmpty(schedule.getRecurrence())) {
                        commonService.forceTvRefreshById(schedule.getTv().getId());
                    } else {
                        String[] recData = schedule.getRecurrence().split(RECURRENCE_SPLITTER);
                        //Co X dni
                        if (isDay(currentTime, schedule, recData)) {
                            commonService.forceTvRefreshById(schedule.getTv().getId());
                        }
                        //Co X tygodni w [Y...] dniach tygodnia
                        if (isWeek(currentTime, schedule, recData)) {
                            refresh(currentTime, schedule, recData[4]);
                        }
                    }
                });
    }

    private boolean isDay(LocalDateTime currentTime, ScheduleModel schedule, String[] recData) {
        return recData[0].equals("day") && (currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) % Integer.parseInt(recData[1]) == 0;
    }

    private boolean isWeek(LocalDateTime currentTime, ScheduleModel schedule, String[] recData) {
        return recData[0].equals("week") &&
                ((int) ((currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) / 7)) % Integer.parseInt(recData[1]) == 0;
    }

    private void refresh(LocalDateTime currentTime, ScheduleModel schedule, String s) {
        String[] days = s.split(DAYS_SPLITTER);
        for (String day : days) {
            int weekDay = Integer.parseInt(day);
            //Sunday fix
            if (weekDay == 0) {
                weekDay = 7;
            }
            if (currentTime.toLocalDate().getDayOfWeek().equals(DayOfWeek.of(weekDay))) {
                commonService.forceTvRefreshById(schedule.getTv().getId());
            }
        }
    }

}
