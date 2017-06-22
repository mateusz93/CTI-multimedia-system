package pl.lodz.p.cti.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.cti.messages.ForceRefreshMessage;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.services.PresentationService;

import java.time.LocalTime;
import java.util.List;

@Component
public class CronScheduler {

    private PresentationService presentationService;

    @Autowired
    public CronScheduler(PresentationService presentationService){
        this.presentationService = presentationService;
    }

    @Scheduled(cron="0 * * * * ?")
    public void toExecuteEveryMinute(){
        LocalTime currentTime = LocalTime.now();
        List<PresentationModel> presentationModelList = presentationService.findAll();
        for(PresentationModel presentationModel : presentationModelList){
            if(currentTime.getHour()==presentationModel.getStartTime().getHour() &&
                    currentTime.getMinute()==presentationModel.getStartTime().getMinute()){
                forceRefresh(presentationModel.getTv().getId());
            }
        }
    }

    @SendTo("/topic/forceRefresh")
    private ForceRefreshMessage forceRefresh(Long tvId) {
        System.out.println("ForceRefresh!");
        return new ForceRefreshMessage(tvId);
    }


}