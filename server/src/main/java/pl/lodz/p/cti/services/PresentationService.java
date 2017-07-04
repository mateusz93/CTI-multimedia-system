package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final CollectionRepository collectionRepository;
    private final TvRepository tvRepository;
    
    private static final String PRESENTATIONS = "presentations";
    private static final String PRESENTATIONS_ENDPOINT = "presentations";
    private static final String COLLECTIONS = "collections";
    private static final String TVS = "tvs";
    private static final String MODIFY_PRESENTATION_ENDPOINT = "modifyPresentation";
    private static final String RED = "red";
    private static final String GREEN = "green";

    public String getPresentations(Model model) {
        log.info("Preparing presentation view");
        List<PresentationModel> presentations = presentationRepository.findAll();
        Collections.sort(presentations);
        model.addAttribute(PRESENTATIONS, presentations);
        return PRESENTATIONS_ENDPOINT;
    }

    public String getPresentationsAndCollectionsAndTvs(Model model) {
        log.info("Preparing modify presentations view");
        model.addAttribute(PRESENTATIONS, presentationRepository.findAll());
        model.addAttribute(COLLECTIONS, collectionRepository.findAll());
        model.addAttribute(TVS, tvRepository.findAll());
        return MODIFY_PRESENTATION_ENDPOINT;
    }

    public String addPresentation(Model model, Long tvId, Long collectionId, String time) throws IncorrectTimeException {
        log.info("Adding new presentation. Tv id: {}. Collection id: {}. Time: {}", tvId, collectionId, time);
        LocalTime startTime = new StringToLocalTimeConverter(time).getLocalTime();
        PresentationModel presentationModel = presentationRepository.findByTvIdAndStartTime(tvId, startTime);
        if (presentationModel != null) {
            model.addAttribute(RED, generateStatement(Statements.PRESENTATION_WITH_GIVEN_DATA_ALREADY_EXISTS));
        } else {
            presentationRepository.save(PresentationModel.builder()
                    .tv(tvRepository.findOne(tvId))
                    .collection(collectionRepository.findOne(collectionId))
                    .startTime(startTime)
                    .build());
            model.addAttribute(GREEN, generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        }
        model.addAttribute(PRESENTATIONS, presentationRepository.findAll());
        model.addAttribute(COLLECTIONS, collectionRepository.findAll());
        model.addAttribute(TVS, tvRepository.findAll());
        return MODIFY_PRESENTATION_ENDPOINT;
    }

    public String delete(Model model, Long presentationId) {
        log.info("Deleting presentation by id {}", presentationId);
        presentationRepository.delete(presentationId);

        model.addAttribute(PRESENTATIONS, presentationRepository.findAll());
        model.addAttribute(COLLECTIONS, collectionRepository.findAll());
        model.addAttribute(TVS, tvRepository.findAll());
        model.addAttribute(GREEN, generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        return MODIFY_PRESENTATION_ENDPOINT;
    }
}
