package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.repository.ConfigurationRepository;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.repository.TvRepository;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final CommonService commonService;
    private final ConfigurationRepository configurationRepository;
    private final ObjectRepository objectRepository;
    private final TvRepository tvRepository;

    public String getActualConfiguration(Model model) {
        ConfigurationModel displayTime = configurationRepository.findByName("displayTime");
        ConfigurationModel displayBubbles = configurationRepository.findByName("displayBubbles");
        ConfigurationModel displayHeader = configurationRepository.findByName("displayHeader");
        ConfigurationModel headerText = configurationRepository.findByName("headerText");
        ConfigurationModel placeholder = configurationRepository.findByName("placeholder");
        model.addAttribute("displayTime", displayTime == null ? "5" : displayTime.getValue());
        model.addAttribute("displayBubbles", displayBubbles == null ? "true" : displayBubbles.getValue());
        model.addAttribute("displayHeader", displayHeader == null ? "true" : displayHeader.getValue());
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        model.addAttribute("placeholder", placeholder == null ? 0 : Long.valueOf(placeholder.getValue()));
        model.addAttribute("objects", objectRepository.findAll());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }

    public String modify(Model model, String displayTime, String displayBubbles, String displayHeader, String headerText, String placeholder) {
        ConfigurationModel displayTimeObj = configurationRepository.findByName("displayTime");
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
        configurationRepository.save(displayTimeObj);
        ConfigurationModel displayBubblesObj = configurationRepository.findByName("displayBubbles");
        if (displayBubblesObj == null) {
            displayBubblesObj = new ConfigurationModel();
            displayBubblesObj.setName("displayBubbles");
            displayBubblesObj.setValue("true");
        }
        if (displayBubbles.equals("true") || displayBubbles.equals("false")) displayBubblesObj.setValue(displayBubbles);
        configurationRepository.save(displayBubblesObj);
        ConfigurationModel displayHeaderObj = configurationRepository.findByName("displayHeader");
        if (displayHeaderObj == null) {
            displayHeaderObj = new ConfigurationModel();
            displayHeaderObj.setName("displayHeader");
            displayHeaderObj.setValue("true");
        }
        if (displayHeader.equals("true") || displayHeader.equals("false")) displayHeaderObj.setValue(displayHeader);
        configurationRepository.save(displayHeaderObj);
        ConfigurationModel headerTextObj = configurationRepository.findByName("headerText");
        if (headerTextObj == null) {
            headerTextObj = new ConfigurationModel();
            headerTextObj.setName("headerText");
            headerTextObj.setValue("");
        }
        headerTextObj.setValue(headerText);
        configurationRepository.save(headerTextObj);
        ConfigurationModel placeholderObj = configurationRepository.findByName("placeholder");
        if (placeholderObj == null) {
            placeholderObj = new ConfigurationModel();
            placeholderObj.setName("placeholder");
            placeholderObj.setValue("0");
        }
        try {
            Long id = Long.valueOf(placeholder);
            if (objectRepository.findOne(id) != null) placeholderObj.setValue(placeholder);
        } catch (NumberFormatException nfe) {
            //Ignore
        }
        configurationRepository.save(placeholderObj);
        for (TvModel tv : tvRepository.findAll()) {
            commonService.forceRefresh(tv.getId());
        }
        model.addAttribute("displayTime", displayTimeObj.getValue());
        model.addAttribute("displayBubbles", displayBubblesObj.getValue());
        model.addAttribute("displayHeader", displayHeaderObj.getValue());
        model.addAttribute("headerText", headerTextObj.getValue());
        model.addAttribute("placeholder", Long.valueOf(placeholderObj.getValue()));
        model.addAttribute("objects", configurationRepository.findAll());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }


}
