package pl.lodz.p.cti.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.cti.messages.ForceRefreshMessage;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.services.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class CronScheduler {

    private ScheduleService scheduleService;

    private SimpMessagingTemplate template;

    @Autowired
    public CronScheduler(ScheduleService scheduleService, SimpMessagingTemplate template){
        this.scheduleService = scheduleService;
        this.template = template;
    }

    @Scheduled(cron="0 * * * * ?")
    public void toExecuteEveryMinute(){
        LocalDateTime currentTime = LocalDateTime.now();
        for (ScheduleModel schedule : scheduleService.findAll())
        {
        	if (schedule.getStartTime().compareTo(currentTime) <= 0 && schedule.getEndTime().compareTo(currentTime) >= 0 && schedule.getStartTime().getHour() == currentTime.getHour() && schedule.getStartTime().getMinute() == currentTime.getMinute())
        	{
        		//Brak powtarzalności
        		if (schedule.getRecurrence() == null || schedule.getRecurrence().length() == 0)
        		{
        			forceRefresh(schedule.getTv().getId());
        		}
        		else
        		{
        			String[] recData = schedule.getRecurrence().split("_");
        			//Co X dni
        			if (recData[0].equals("day") && (currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) % Integer.parseInt(recData[1]) == 0)
        			{
            			forceRefresh(schedule.getTv().getId());
        			}
        			//Co X tygodni w [Y...] dniach tygodnia
        			if (recData[0].equals("week"))
        			{
        				if (((int)((currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) / 7)) % Integer.parseInt(recData[1]) == 0)
        				{
	        				String[] days = recData[4].split(",");
	        				for (String day : days)
	        				{
	        					int weekDay = Integer.parseInt(day);
	        					//Sunday fix
	        					if (weekDay == 0) weekDay = 7;
	        					if (currentTime.toLocalDate().getDayOfWeek().equals(DayOfWeek.of(weekDay)))
	        					{
	                    			forceRefresh(schedule.getTv().getId());
	        					}
	        				}
        				}
        			}
        		}
        	}
        }
    }

    private void forceRefresh(Long tvId) {
        System.out.println("ForceRefresh!");
        this.template.convertAndSend("/topic/forceRefresh", new ForceRefreshMessage(tvId));
    }


}
