package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.p.cti.messages.ForceRefreshMessage;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.models.MapJSModel;
import pl.lodz.p.cti.models.ScheduleJSModel;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.services.ConfigurationService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.ScheduleService;
import pl.lodz.p.cti.services.TvService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ConfigurationController {

    private final CollectionService collectionService;
    private final ConfigurationService configurationService;
    private final ObjectService objectService;
    private final TvService tvService;
    private final ScheduleService scheduleService;
    private final SimpMessagingTemplate template;

    @RequestMapping(value = {"/configuration"}, method = RequestMethod.GET)
    public String modifyConfigurationGET(Model model) {
        ConfigurationModel displayTime = configurationService.findByName("displayTime");
        ConfigurationModel displayBubbles = configurationService.findByName("displayBubbles");
        ConfigurationModel displayHeader = configurationService.findByName("displayHeader");
        ConfigurationModel headerText = configurationService.findByName("headerText");
        ConfigurationModel placeholder = configurationService.findByName("placeholder");
        model.addAttribute("displayTime", displayTime == null ? "5" : displayTime.getValue());
        model.addAttribute("displayBubbles", displayBubbles == null ? "true" : displayBubbles.getValue());
        model.addAttribute("displayHeader", displayHeader == null ? "true" : displayHeader.getValue());
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        model.addAttribute("placeholder", placeholder == null ? 0 : Long.valueOf(placeholder.getValue()));
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }

    @RequestMapping(value = {"/configuration"}, method = RequestMethod.POST)
    public String modifyConfigurationPOST(Model model, String displayTime, String displayBubbles, String displayHeader, String headerText, String placeholder) {
        ConfigurationModel displayTimeObj = configurationService.findByName("displayTime");
        if (displayTimeObj == null) {
            displayTimeObj = new ConfigurationModel();
            displayTimeObj.setName("displayTime");
            displayTimeObj.setValue("5");
        }
        try {
            int time = Integer.parseInt(displayTime);
            if (time > 0) displayTimeObj.setValue(displayTime);
        } catch (NumberFormatException nfe) {
            //Ignore
        }
        configurationService.save(displayTimeObj);
        ConfigurationModel displayBubblesObj = configurationService.findByName("displayBubbles");
        if (displayBubblesObj == null) {
            displayBubblesObj = new ConfigurationModel();
            displayBubblesObj.setName("displayBubbles");
            displayBubblesObj.setValue("true");
        }
        if (displayBubbles.equals("true") || displayBubbles.equals("false")) displayBubblesObj.setValue(displayBubbles);
        configurationService.save(displayBubblesObj);
        ConfigurationModel displayHeaderObj = configurationService.findByName("displayHeader");
        if (displayHeaderObj == null) {
            displayHeaderObj = new ConfigurationModel();
            displayHeaderObj.setName("displayHeader");
            displayHeaderObj.setValue("true");
        }
        if (displayHeader.equals("true") || displayHeader.equals("false")) displayHeaderObj.setValue(displayHeader);
        configurationService.save(displayHeaderObj);
        ConfigurationModel headerTextObj = configurationService.findByName("headerText");
        if (headerTextObj == null) {
            headerTextObj = new ConfigurationModel();
            headerTextObj.setName("headerText");
            headerTextObj.setValue("");
        }
        headerTextObj.setValue(headerText);
        configurationService.save(headerTextObj);
        ConfigurationModel placeholderObj = configurationService.findByName("placeholder");
        if (placeholderObj == null) {
            placeholderObj = new ConfigurationModel();
            placeholderObj.setName("placeholder");
            placeholderObj.setValue("0");
        }
        try {
            Long id = Long.valueOf(placeholder);
            if (objectService.findOne(id) != null) placeholderObj.setValue(placeholder);
        } catch (NumberFormatException nfe) {
            //Ignore
        }
        configurationService.save(placeholderObj);
        for (TvModel tv : tvService.findAll()) {
            forceRefresh(tv.getId());
        }
        model.addAttribute("displayTime", displayTimeObj.getValue());
        model.addAttribute("displayBubbles", displayBubblesObj.getValue());
        model.addAttribute("displayHeader", displayHeaderObj.getValue());
        model.addAttribute("headerText", headerTextObj.getValue());
        model.addAttribute("placeholder", Long.valueOf(placeholderObj.getValue()));
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }

    @RequestMapping(value = {"/modifySchedule"}, method = RequestMethod.GET)
    public String modifyScheduleGET(Model model, @RequestParam(value = "tvId", required = true) Long tvId) {
        TvModel tvModel = tvService.findOne(tvId);
        if (tvModel == null) {
            return "redirect:/modifyPresentation";
        }
        model.addAttribute("collections", collectionService.findAll().stream().map(e -> (new MapJSModel(e))).collect(Collectors.toList()));
        model.addAttribute("schedules", scheduleService.findByTvId(tvId).stream().map(e -> (new ScheduleJSModel(e))).collect(Collectors.toList()));
        model.addAttribute("tv", tvModel);
        return "modifySchedule";
    }

    @RequestMapping(value = {"/modifySchedule"}, method = RequestMethod.POST)
    public String modifySchedulePOST(Model model, @RequestParam(value = "tvId", required = true) Long tvId, @RequestBody List<ScheduleJSModel> schedules) {
        TvModel tvModel = tvService.findOne(tvId);
        if (tvModel == null) {
            return "redirect:/modifyPresentation";
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        List<ScheduleModel> schedulesToSave = new ArrayList<>();
        List<ScheduleModel> schedulesToRemove = scheduleService.findByTvId(tvId);
        for (ScheduleJSModel m : schedules) {
            ScheduleModel scheduleModel = null;
            if (m.getId() != null) scheduleModel = scheduleService.findOne(m.getId());
            if (scheduleModel == null) scheduleModel = new ScheduleModel();
            scheduleModel.setCollection(collectionService.findOne(m.getCollection()));
            scheduleModel.setEndTime(LocalDateTime.parse(m.getEnd_date(), format));
            if (m.getEvent_length() != null) scheduleModel.setEventLength(m.getEvent_length());
            else scheduleModel.setEventLength(0L);
            if (m.getEvent_parent() != null) scheduleModel.setEventParent(scheduleService.findOne(m.getEvent_parent()));
            scheduleModel.setRecurrence(m.getRec_type());
            scheduleModel.setStartTime(LocalDateTime.parse(m.getStart_date(), format));
            scheduleModel.setText(m.getText());
            scheduleModel.setTv(tvModel);
            scheduleService.save(scheduleModel);
            schedulesToSave.add(scheduleModel);
        }
        schedulesToRemove.removeAll(schedulesToSave);
        for (ScheduleModel m : schedulesToRemove) {
            scheduleService.delete(m.getId());
        }
        model.addAttribute("collections", collectionService.findAll().stream().map(e -> (new MapJSModel(e))).collect(Collectors.toList()));
        model.addAttribute("schedules", schedules);
        model.addAttribute("tv", tvModel);
        forceRefresh(tvModel.getId());
        return "modifySchedule";
    }

    private void forceRefresh(Long tvId) {
        log.info("ForceRefresh!");
        this.template.convertAndSend("/topic/forceRefresh", new ForceRefreshMessage(tvId));
    }

}
