package pl.lodz.p.cti.utils;

import pl.lodz.p.cti.models.ScheduleModel;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ActualScheduleFinder {

    public static ScheduleModel findActualSchedule(List<ScheduleModel> scheduleList){
        /*PresentationModel lastBeforeCurrentTime = null;
        PresentationModel last = null;
        LocalTime currentTime = LocalTime.now();
        LocalTime currentTimeRoundedIntoMinutes = LocalTime.MIN.withMinute(currentTime.getMinute()).withHour(currentTime.getHour());
        for(PresentationModel presentationModel : presentationModelList){
            if(presentationModel.getStartTime().compareTo(currentTimeRoundedIntoMinutes)==0){
                return presentationModel;
            }
            if((lastBeforeCurrentTime==null||lastBeforeCurrentTime.getStartTime().isBefore(presentationModel.getStartTime()))&&presentationModel.getStartTime().isBefore(currentTimeRoundedIntoMinutes)){
                lastBeforeCurrentTime = presentationModel;
            }
            if(last==null||presentationModel.getStartTime().isAfter(last.getStartTime())){
                last = presentationModel;
            }
        }
        return lastBeforeCurrentTime!=null ? lastBeforeCurrentTime : last;*/
        LocalDateTime currentTime = LocalDateTime.now();
        List<ScheduleModel> matchingFilter = new ArrayList<>();
        //Filtrowanie bez czasu
        for (ScheduleModel schedule : scheduleList)
        {
        	if (schedule.getStartTime().compareTo(currentTime) <= 0 && schedule.getEndTime().compareTo(currentTime) >= 0)
        	{
        		//Brak powtarzalności
        		System.out.println(schedule.getRecurrence());
        		if (schedule.getRecurrence() == null || schedule.getRecurrence().length() == 0)
        		{
        			matchingFilter.add(schedule);
        		}
        		else
        		{
        			String[] recData = schedule.getRecurrence().split("_");
        			//Co X dni
        			if (recData[0].equals("day") && (currentTime.toLocalDate().toEpochDay() - schedule.getStartTime().toLocalDate().toEpochDay()) % Integer.parseInt(recData[1]) == 0)
        			{
            			matchingFilter.add(schedule);
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
	        	        			matchingFilter.add(schedule);
	        					}
	        				}
        				}
        			}
        		}
        	}
        }
        System.out.println(matchingFilter);
        for (ScheduleModel schedule : matchingFilter)
        {
    		if (schedule.getRecurrence() == null || schedule.getRecurrence().length() == 0)
    		{
    			return schedule;
    		}
    		if (schedule.getStartTime().toLocalTime().isBefore(schedule.getEndTime().toLocalTime()) && (schedule.getStartTime().toLocalTime().isBefore(currentTime.toLocalTime()) || schedule.getStartTime().toLocalTime().equals(currentTime.toLocalTime())) && (schedule.getEndTime().toLocalTime().isAfter(currentTime.toLocalTime()) || schedule.getEndTime().toLocalTime().equals(currentTime.toLocalTime())))
    		{
    			return schedule;
    		}
    		if (schedule.getStartTime().toLocalTime().isAfter(schedule.getEndTime().toLocalTime()) && (schedule.getStartTime().toLocalTime().isBefore(currentTime.toLocalTime()) || schedule.getStartTime().toLocalTime().equals(currentTime.toLocalTime()) || schedule.getEndTime().toLocalTime().isAfter(currentTime.toLocalTime()) || schedule.getEndTime().toLocalTime().equals(currentTime.toLocalTime())))
    		{
    			return schedule;
    		}
        }
        return null;
    }
}
