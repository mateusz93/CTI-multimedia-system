package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.p.cti.models.ScheduleJSModel;
import pl.lodz.p.cti.services.ConfigurationService;
import pl.lodz.p.cti.services.ScheduleService;

import java.util.List;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final ScheduleService scheduleService;

    @RequestMapping(value = {"/configuration"}, method = RequestMethod.GET)
    public String modifyGET(Model model) {
        return configurationService.getActualConfiguration(model);
    }

    @RequestMapping(value = {"/configuration"}, method = RequestMethod.POST)
    public String modifyPOST(Model model, String displayTime, String displayBubbles, String displayHeader, String headerText, String placeholder) {
        return configurationService.modify(model, displayTime, displayBubbles, displayHeader, headerText, placeholder);
    }

    @RequestMapping(value = {"/modifySchedule"}, method = RequestMethod.GET)
    public String modifyScheduleGET(Model model, @RequestParam(value = "tvId") Long tvId) {
        return scheduleService.getActualConfiguration(model, tvId);
    }

    @RequestMapping(value = {"/modifySchedule"}, method = RequestMethod.POST)
    public String modifySchedulePOST(Model model, @RequestParam(value = "tvId") Long tvId, @RequestBody List<ScheduleJSModel> schedules) {
        return scheduleService.modify(model, tvId, schedules);
    }

}
