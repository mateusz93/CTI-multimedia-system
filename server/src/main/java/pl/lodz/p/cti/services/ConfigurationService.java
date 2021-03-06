package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.repository.ConfigurationRepository;
import pl.lodz.p.cti.repository.ObjectRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConfigurationService {

    private final CommonService commonService;
    private final ConfigurationRepository configurationRepository;
    private final ObjectRepository objectRepository;

    private static final String DISPLAY_TIME = "displayTime";
    private static final String DISPLAY_BUBBLES = "displayBubbles";
    private static final String DISPLAY_HEADER = "displayHeader";
    private static final String HEADER_TEXT = "headerText";
    private static final String PLACE_HOLDER = "placeholder";
    private static final String OBJECTS = "objects";
    private static final String BOOLEAN_TRUE = "true";
    private static final String TRUE_VALUE = "trueValue";
    private static final String BOOLEAN_FALSE = "false";
    private static final String CONFIGURATION_ENDPOINT = "configuration";

    public String getActualConfiguration(Model model) {
        log.info("Preparing configuration view");
        model.addAttribute(DISPLAY_TIME, getOriginalDisplayTime());
        model.addAttribute(DISPLAY_BUBBLES, getOriginalDisplayBubbles());
        model.addAttribute(DISPLAY_HEADER, getOriginalDisplayHeader());
        model.addAttribute(HEADER_TEXT, getOriginalHeaderText());
        model.addAttribute(PLACE_HOLDER, getOriginalPlaceHolder());
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(TRUE_VALUE, BOOLEAN_TRUE);
        return CONFIGURATION_ENDPOINT;
    }

    public String modify(Model model, String displayTime, String displayBubbles, String displayHeader, String headerText, String placeholder) {
        commonService.forceTvRefreshAll();
        log.info("Updating configuration. New values:");
        log.info("Display Time: {} Display Bubbles: {} Display Header: {} Header Text: {}, Place Holder: {}",
                displayTime, displayBubbles, displayHeader, headerText, placeholder);

        model.addAttribute(DISPLAY_TIME, getModifiedDisplayTime(displayTime));
        model.addAttribute(DISPLAY_BUBBLES, getModifiedDisplayBubbles(displayBubbles));
        model.addAttribute(DISPLAY_HEADER, getModifiedDisplayHeader(displayHeader));
        model.addAttribute(HEADER_TEXT, getModifiedHeaderText(headerText));
        model.addAttribute(PLACE_HOLDER, getModifiedPlaceHolder(placeholder));
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(TRUE_VALUE, BOOLEAN_TRUE);
        return CONFIGURATION_ENDPOINT;
    }

    public Long getOriginalPlaceHolder() {
        ConfigurationModel placeholder = configurationRepository.findByName(PLACE_HOLDER);
        return placeholder == null ? 0 : Long.valueOf(placeholder.getValue());
    }

    private Long getModifiedPlaceHolder(String placeholder) {
        ConfigurationModel placeholderObj = configurationRepository.findByName(PLACE_HOLDER);
        if (placeholderObj == null) {
            placeholderObj = new ConfigurationModel();
            placeholderObj.setName(PLACE_HOLDER);
            placeholderObj.setValue("0");
        }
        try {
            Long id = Long.parseLong(placeholder);
            if (objectRepository.findOne(id) != null) {
                placeholderObj.setValue(placeholder);
            }
        } catch (NumberFormatException nfe) {
            //Ignore
        }
        return Long.parseLong(configurationRepository.save(placeholderObj).getValue());
    }

    private String getOriginalHeaderText() {
        ConfigurationModel headerText = configurationRepository.findByName(HEADER_TEXT);
        return headerText == null ? "" : headerText.getValue();
    }

    private String getModifiedHeaderText(String headerText) {
        ConfigurationModel headerTextObj = configurationRepository.findByName(HEADER_TEXT);
        if (headerTextObj == null) {
            headerTextObj = new ConfigurationModel();
            headerTextObj.setName(HEADER_TEXT);
            headerTextObj.setValue("");
        }
        headerTextObj.setValue(headerText);
        return configurationRepository.save(headerTextObj).getValue();
    }

    private String getOriginalDisplayHeader() {
        ConfigurationModel displayHeader = configurationRepository.findByName(DISPLAY_HEADER);
        return displayHeader == null ? BOOLEAN_TRUE : displayHeader.getValue();
    }

    private String getModifiedDisplayHeader(String displayHeader) {
        ConfigurationModel displayHeaderObj = configurationRepository.findByName(DISPLAY_HEADER);
        if (displayHeaderObj == null) {
            displayHeaderObj = new ConfigurationModel();
            displayHeaderObj.setName(displayHeader);
            displayHeaderObj.setValue(BOOLEAN_TRUE);
        }
        if (displayHeader.equals(BOOLEAN_TRUE) || displayHeader.equals(BOOLEAN_FALSE)) {
            displayHeaderObj.setValue(displayHeader);
        }
        return configurationRepository.save(displayHeaderObj).getValue();
    }

    private String getOriginalDisplayBubbles() {
        ConfigurationModel displayBubbles = configurationRepository.findByName(DISPLAY_BUBBLES);
        return displayBubbles == null ? BOOLEAN_TRUE : displayBubbles.getValue();
    }

    private String getModifiedDisplayBubbles(String displayBubbles) {
        ConfigurationModel displayBubblesObj = configurationRepository.findByName(DISPLAY_BUBBLES);
        if (displayBubblesObj == null) {
            displayBubblesObj = new ConfigurationModel();
            displayBubblesObj.setName(DISPLAY_BUBBLES);
            displayBubblesObj.setValue(BOOLEAN_TRUE);
        }
        if (BOOLEAN_TRUE.equals(displayBubbles) || BOOLEAN_FALSE.equals(displayBubbles)) {
            displayBubblesObj.setValue(displayBubbles);
        }
        return configurationRepository.save(displayBubblesObj).getValue();
    }

    private String getOriginalDisplayTime() {
        ConfigurationModel displayTime = configurationRepository.findByName(DISPLAY_TIME);
        return displayTime == null ? "5" : displayTime.getValue();
    }

    private String getModifiedDisplayTime(String displayTime) {
        ConfigurationModel displayTimeObj = configurationRepository.findByName(DISPLAY_TIME);
        if (displayTimeObj == null) {
            displayTimeObj = new ConfigurationModel();
            displayTimeObj.setName(DISPLAY_TIME);
            displayTimeObj.setValue("5");
        }
        try {
            int time = Integer.parseInt(displayTime);
            if (time > 0) {
                displayTimeObj.setValue(displayTime);
            }
        } catch (NumberFormatException nfe) {
            //Ignore
        }
        return configurationRepository.save(displayTimeObj).getValue();
    }

}
