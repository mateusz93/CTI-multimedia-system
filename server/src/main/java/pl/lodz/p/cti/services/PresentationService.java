package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lodz.p.cti.exceptions.IncorrectTimeException;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.repository.CollectionRepository;
import pl.lodz.p.cti.repository.PresentationRepository;
import pl.lodz.p.cti.repository.TvRepository;
import pl.lodz.p.cti.utils.Statements;
import pl.lodz.p.cti.utils.StringToLocalTimeConverter;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Service
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final CollectionRepository collectionRepository;
    private final TvRepository tvRepository;

    public String getPresentations(Model model) {
        List<PresentationModel> presentations = presentationRepository.findAll();
        Collections.sort(presentations);
        model.addAttribute("presentations", presentations);
        return "presentations";
    }

    public String getPresentationsAndCollectionsAndTvs(Model model) {
        model.addAttribute("presentations", presentationRepository.findAll());
        model.addAttribute("collections", collectionRepository.findAll());
        model.addAttribute("tvs", tvRepository.findAll());
        return "modifyPresentation";
    }

    public String addPresentation(Model model, Long tvId, Long collectionId, String time) throws IncorrectTimeException {
        LocalTime startTime = new StringToLocalTimeConverter(time).getLocalTime();
        PresentationModel presentationModel = presentationRepository.findByTvIdAndStartTime(tvId, startTime);
        if (presentationModel != null) {
            model.addAttribute("red", generateStatement(Statements.PRESENTATION_WITH_GIVEN_DATA_ALREADY_EXISTS));
        } else {
            presentationRepository.save(PresentationModel.builder()
                    .tv(tvRepository.findOne(tvId))
                    .collection(collectionRepository.findOne(collectionId))
                    .startTime(startTime)
                    .build());
            model.addAttribute("green", generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        }
        model.addAttribute("presentations", presentationRepository.findAll());
        model.addAttribute("collections", collectionRepository.findAll());
        model.addAttribute("tvs", tvRepository.findAll());
        return "modifyPresentation";
    }

    public String delete(Model model, Long presentationId) {
        presentationRepository.delete(presentationId);
        model.addAttribute("presentations", presentationRepository.findAll());
        model.addAttribute("collections", collectionRepository.findAll());
        model.addAttribute("tvs", tvRepository.findAll());
        model.addAttribute("green", generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        return "modifyPresentation";
    }
}
