package pl.lodz.p.cti.utils;

import pl.lodz.p.cti.exceptions.IncorrectTimeException;

import java.time.LocalTime;
import java.util.StringTokenizer;

public class StringToLocalTimeConverter {

    private final static String DELIMITER = ":";

    private String rawTime;

    public StringToLocalTimeConverter(String rawTime){
        this.rawTime = rawTime;
    }

    public LocalTime getLocalTime() throws IncorrectTimeException{
        if(!rawTime.contains(":")){
            throw new IncorrectTimeException(rawTime);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(rawTime,DELIMITER);
        if(stringTokenizer.countTokens()!=2){
            throw new IncorrectTimeException(rawTime);
        }
        try {
            int hour = Integer.valueOf(stringTokenizer.nextToken());
            int minute = Integer.valueOf(stringTokenizer.nextToken());
            return LocalTime.MIN.withHour(hour).withMinute(minute);
        } catch(Exception e){
            throw new IncorrectTimeException(rawTime);
        }
    }
}
