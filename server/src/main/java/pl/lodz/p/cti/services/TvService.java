package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
@RequiredArgsConstructor
public class TvService {

    private final TvRepository tvRepository;
    private final ConfigurationRepository configurationRepository;
    private final ScheduleRepository scheduleRepository;
    private final ObjectRepository objectRepository;

    private static final String OBJECTS = "objects";
    private static final String PLACE_HOLDER = "placeholder";
    private static final String TV_ID = "tvId";
    private static final String DISPLAY_TIME = "displayTime";
    private static final String DISPLAY_BUBBLES = "displayBubbles";
    private static final String DISPLAY_HEADER = "displayHeader";
    private static final String HEADER_TEXT = "headerText";
    private static final String TRUE_VALUE = "true";
    private static final String INDEX_ENDPOINT = "index";
    private static final String TV_ENDPOINT = "redirect:/tv/";
    private static final String PRESENTATION_ENDPOINT = "presentation";

    public String index(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        TvModel tvModel = tvRepository.findByIp(ip);
        return tvModel == null ? INDEX_ENDPOINT : TV_ENDPOINT + tvModel.getHash() + "/";
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
        return TV_ENDPOINT + tvRepository.save(TvModel.builder()
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
        model.addAttribute(OBJECTS, getObjects(tvModel));
        model.addAttribute(TV_ID, tvModel.getId());
        model.addAttribute(DISPLAY_TIME, getDisplayTime());
        model.addAttribute(DISPLAY_BUBBLES, getDisplayBubbles());
        model.addAttribute(DISPLAY_HEADER, getDisplayHeader());
        model.addAttribute(HEADER_TEXT, getHeaderText());
        return PRESENTATION_ENDPOINT;
    }

    private List<ObjectModel> getObjects(TvModel tvModel) {
        ScheduleModel actualSchedule = findActualSchedule(scheduleRepository.findByTvId(tvModel.getId()));
        if (actualSchedule != null) {
            return actualSchedule.getCollection()
                    .getCollectionObjects()
                    .stream()
                    .map(CollectionObjectModel::getObjectModel)
                    .collect(Collectors.toList());
        }
        try {
            ConfigurationModel placeholderObj = configurationRepository.findByName(PLACE_HOLDER);
            if (placeholderObj == null) {
                //TODO maybe better option will be return CollectionUtils.emptyCollection();
                return null;
            }
            ObjectModel placeholder = objectRepository.findOne(Long.valueOf(placeholderObj.getValue()));
            if (placeholder == null) {
                //TODO maybe better option will be return CollectionUtils.emptyCollection();
                return null;
            }
            List<ObjectModel> objects = new ArrayList<>();
            objects.add(placeholder);
            return objects;
        } catch (NumberFormatException nfe) {
            //TODO maybe better option will be return CollectionUtils.emptyCollection();
            return null;
        }
    }

    private int getDisplayTime() {
        ConfigurationModel displayTime = configurationRepository.findByName(DISPLAY_TIME);
        return displayTime == null ? 5 : Integer.valueOf(displayTime.getValue());
    }

    private boolean getDisplayBubbles() {
        ConfigurationModel displayBubbles = configurationRepository.findByName(DISPLAY_BUBBLES);
        return displayBubbles == null || displayBubbles.getValue().equalsIgnoreCase(TRUE_VALUE);
    }

    private boolean getDisplayHeader() {
        ConfigurationModel displayHeader = configurationRepository.findByName(DISPLAY_HEADER);
        return displayHeader == null || displayHeader.getValue().equalsIgnoreCase(TRUE_VALUE);
    }

    private String getHeaderText() {
        ConfigurationModel headerText = configurationRepository.findByName(HEADER_TEXT);
        return headerText == null ? StringUtils.EMPTY : headerText.getValue();
    }

}
