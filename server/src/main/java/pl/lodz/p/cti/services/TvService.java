package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lodz.p.cti.exceptions.TvModelDoesNotExistsException;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.repository.ConfigurationRepository;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.repository.ScheduleRepository;
import pl.lodz.p.cti.repository.TvRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.ActualScheduleFinder.findActualSchedule;
import static pl.lodz.p.cti.utils.SessionIdentifierGenerator.nextSessionId;

@Service
@RequiredArgsConstructor
public class TvService {

    private final TvRepository tvRepository;
    private final ConfigurationRepository configurationRepository;
    private final ScheduleRepository scheduleRepository;
    private final ObjectRepository objectRepository;

    public String index(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        TvModel tvModel = tvRepository.findByIp(ip);
        return tvModel == null ? "index" : "redirect:/tv/" + tvModel.getHash() + "/";
    }

    public String initialize(HttpServletRequest request, String name) {
        String ip = request.getRemoteAddr();
        String hash;
        do {
            hash = nextSessionId();
        } while (tvRepository.findByHash(hash) != null);
        if (name.length() < 1) {
            name = ip;
        }
        return "redirect:/tv/" + tvRepository.save(TvModel.builder()
                .ip(ip)
                .name(name)
                .hash(hash)
                .build())
                .getHash() + "/";
    }

    public String hash(Model model, String hash) throws TvModelDoesNotExistsException {
        TvModel tvModel = tvRepository.findByHash(hash);
        if (tvModel == null) {
            throw new TvModelDoesNotExistsException(TvModel.PROPERTY_HASH, hash);
        }
        ScheduleModel actualSchedule = findActualSchedule(scheduleRepository.findByTvId(tvModel.getId()));
        if (actualSchedule == null) {
            try {
                ConfigurationModel placeholderObj = configurationRepository.findByName("placeholder");
                if (placeholderObj != null) {
                    ObjectModel placeholder = objectRepository.findOne(Long.valueOf(placeholderObj.getValue()));
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
        ConfigurationModel displayTime = configurationRepository.findByName("displayTime");
        ConfigurationModel displayBubbles = configurationRepository.findByName("displayBubbles");
        ConfigurationModel displayHeader = configurationRepository.findByName("displayHeader");
        ConfigurationModel headerText = configurationRepository.findByName("headerText");
        model.addAttribute("displayTime", displayTime == null ? 5 : Integer.valueOf(displayTime.getValue()));
        model.addAttribute("displayBubbles", displayBubbles == null || displayBubbles.getValue().equals("true"));
        model.addAttribute("displayHeader", displayHeader == null || displayHeader.getValue().equals("true"));
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        return "presentation";

    }
}
