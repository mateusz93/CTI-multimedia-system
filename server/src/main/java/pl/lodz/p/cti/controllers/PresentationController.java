package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.services.PresentationService;
import pl.lodz.p.cti.services.TvService;
import pl.lodz.p.cti.utils.Statements;
import pl.lodz.p.cti.utils.StringToLocalTimeConverter;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class PresentationController {

    private final CollectionService collectionService;
    private final PresentationService presentationService;
    private final TvService tvService;

    @RequestMapping(value = {"/presentations"}, method = RequestMethod.GET)
    public String presentationsGET(Model model) {
        List<PresentationModel> presentations = presentationService.findAll();
        Collections.sort(presentations);
        model.addAttribute("presentations", presentations);
        return "presentations";
    }


    @RequestMapping(value = {"/modifyPresentation"}, method = RequestMethod.GET)
    public String modifyPresentationGET(Model model) {
        model.addAttribute("presentations", presentationService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        model.addAttribute("tvs", tvService.findAll());
        return "modifyPresentation";
    }

    @RequestMapping(value = {"/addPresentation"}, method = RequestMethod.POST)
    public String addPresentationPOST(Model model, Long tvId, Long collectionId, String time) throws ValidationException {
        LocalTime startTime = new StringToLocalTimeConverter(time).getLocalTime();
        PresentationModel presentationModel = presentationService.findByTvIdAndStartTime(tvId, startTime);
        if (presentationModel != null) {
            model.addAttribute("red", generateStatement(Statements.PRESENTATION_WITH_GIVEN_DATA_ALREADY_EXISTS));
        } else {
            presentationService.save(PresentationModel.builder()
                    .tv(tvService.findOne(tvId))
                    .collection(collectionService.findOne(collectionId))
                    .startTime(startTime)
                    .build());
            model.addAttribute("green", generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        }
        model.addAttribute("presentations", presentationService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        model.addAttribute("tvs", tvService.findAll());
        return "modifyPresentation";
    }

    @RequestMapping(value = {"/removePresentation"}, method = RequestMethod.POST)
    public String deletePresentationPOST(Model model, Long presentationId) {
        presentationService.delete(presentationId);
        model.addAttribute("presentations", presentationService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        model.addAttribute("tvs", tvService.findAll());
        model.addAttribute("green", generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        return "modifyPresentation";
    }
}
