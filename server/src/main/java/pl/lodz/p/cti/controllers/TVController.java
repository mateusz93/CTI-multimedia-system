package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.exceptions.TvModelDoesNotExistsException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.services.ConfigurationService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.ScheduleService;
import pl.lodz.p.cti.services.TvService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.ActualScheduleFinder.findActualSchedule;
import static pl.lodz.p.cti.utils.SessionIdentifierGenerator.nextSessionId;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class TVController {

    private final ObjectService objectService;
    private final ConfigurationService configurationService;
    private final TvService tvService;
    private final ScheduleService scheduleService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String indexGET(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        TvModel tvModel = tvService.findByIp(ip);
        return tvModel == null ? "index" : "redirect:/tv/" + tvModel.getHash() + "/";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPOST(HttpServletRequest request, String name) {
        String ip = request.getRemoteAddr();
        String hash;
        do {
            hash = nextSessionId();
        } while (tvService.findByHash(hash) != null);
        if (name.length() < 1) name = ip;
        return "redirect:/tv/" + tvService.save(TvModel.builder()
                .ip(ip)
                .name(name)
                .hash(hash)
                .build())
                .getHash() + "/";
    }

    @RequestMapping(value = {"/tv/{hash}/"}, method = RequestMethod.GET)
    public String hashGET(Model model, @PathVariable String hash) throws ValidationException {
        TvModel tvModel = tvService.findByHash(hash);
        if (tvModel == null) {
            throw new TvModelDoesNotExistsException(TvModel.PROPERTY_HASH, hash);
        }
        ScheduleModel actualSchedule = findActualSchedule(scheduleService.findByTvId(tvModel.getId()));
        if (actualSchedule == null) {
            try {
                ConfigurationModel placeholderObj = configurationService.findByName("placeholder");
                if (placeholderObj != null) {
                    ObjectModel placeholder = objectService.findOne(Long.valueOf(placeholderObj.getValue()));
                    if (placeholder != null) {
                        List<ObjectModel> objects = new ArrayList<>();
                        objects.add(placeholder);
                        model.addAttribute("objects", objects);
                    } else {
                        model.addAttribute("objects", null);
                    }
                } else {
                    model.addAttribute("objects", null);
                }
            } catch (NumberFormatException nfe) {
                //Ignore
                model.addAttribute("objects", null);
            }
        } else {
            model.addAttribute("objects", actualSchedule.getCollection().getCollectionObjects()
                    .stream().map(CollectionObjectModel::getObjectModel).collect(Collectors.toList()));
        }
        model.addAttribute("tvId", tvModel.getId());
        ConfigurationModel displayTime = configurationService.findByName("displayTime");
        ConfigurationModel displayBubbles = configurationService.findByName("displayBubbles");
        ConfigurationModel displayHeader = configurationService.findByName("displayHeader");
        ConfigurationModel headerText = configurationService.findByName("headerText");
        model.addAttribute("displayTime", displayTime == null ? 5 : Integer.valueOf(displayTime.getValue()));
        model.addAttribute("displayBubbles", displayBubbles == null || displayBubbles.getValue().equals("true"));
        model.addAttribute("displayHeader", displayHeader == null || displayHeader.getValue().equals("true"));
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        return "presentation";
    }
}
