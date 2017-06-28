package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.services.PresentationService;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class PresentationController {

    private final PresentationService service;

    @RequestMapping(value = {"/presentations"}, method = RequestMethod.GET)
    public String presentationsGET(Model model) {
        return service.getPresentations(model);
    }

    @RequestMapping(value = {"/modifyPresentation"}, method = RequestMethod.GET)
    public String modifyPresentationGET(Model model) {
        return service.getPresentationsAndCollectionsAndTvs(model);
    }

    @RequestMapping(value = {"/addPresentation"}, method = RequestMethod.POST)
    public String addPresentationPOST(Model model, Long tvId, Long collectionId, String time) throws ValidationException {
        return service.addPresentation(model, tvId, collectionId, time);
    }

    @RequestMapping(value = {"/removePresentation"}, method = RequestMethod.POST)
    public String deletePresentationPOST(Model model, Long presentationId) {
        return service.delete(model, presentationId);
    }
}
