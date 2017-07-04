package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.repository.CollectionObjectRepository;
import pl.lodz.p.cti.repository.CollectionRepository;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.repository.PresentationRepository;
import pl.lodz.p.cti.repository.ScheduleRepository;
import pl.lodz.p.cti.utils.CollectionWrapper;
import pl.lodz.p.cti.utils.Statements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionObjectRepository collectionObjectRepository;
    private final ScheduleRepository scheduleRepository;
    private final ObjectRepository objectRepository;
    private final PresentationRepository presentationRepository;

    private static final String COLLECTIONS = "collections";
    private static final String OBJECTS = "objects";
    private static final String MODIFY_COLLECTION_ENDPOINT = "modifyCollection";
    private static final String COLLECTION_WRAPPER = "collectionWrapper";
    private static final String GREEN = "green";

    public String getCollection(Model model) {
        log.info("Preparing collections view");
        model.addAttribute(COLLECTIONS, getCollectionsBySchedule());
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(COLLECTION_WRAPPER, new CollectionWrapper());
        return MODIFY_COLLECTION_ENDPOINT;
    }

    public String addCollection(Model model, String name, CollectionWrapper collectionWrapper) {
        collectionObjectRepository.save(getCollectionObjectModels(name, collectionWrapper));

        log.info("Preparing collections view");
        model.addAttribute(COLLECTIONS, getCollectionsByPresentation());
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(COLLECTION_WRAPPER, new CollectionWrapper());
        model.addAttribute(GREEN, generateStatement(Statements.SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS, name));
        return MODIFY_COLLECTION_ENDPOINT;

    }

    public String deleteCollection(Model model, Long collectionId) {
        log.info("Deleting collection with id: {}", collectionId);
        collectionRepository.delete(collectionId);

        log.info("Preparing collections view");
        model.addAttribute(COLLECTIONS, getCollectionsByPresentation());
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(COLLECTION_WRAPPER, new CollectionWrapper());
        model.addAttribute(GREEN, generateStatement(Statements.CHOSEN_COLLECTION_REMOVED));
        return MODIFY_COLLECTION_ENDPOINT;
    }

    private List<CollectionObjectModel> getCollectionObjectModels(String name, CollectionWrapper collectionWrapper) {
        log.info("Creating new collection with name: {}", name);
        log.info("Collection sequence: {}", collectionWrapper.toString());
        List<CollectionObjectModel> collectionObjectModelList = new ArrayList<>();
        CollectionModel collectionModel = collectionRepository.save(createCollectionModel(name));

        for (long i = 0; i < collectionWrapper.getValues().size(); ++i) {
            collectionObjectModelList.add(CollectionObjectModel.builder()
                    .collection(collectionModel)
                    .orderNumber(i)
                    .objectModel(objectRepository.findOne(collectionWrapper.getValues().get((int) i)))
                    .build());
        }
        log.info("Collection created correctly");
        return collectionObjectModelList;
    }

    private List<CollectionModel> getCollectionsBySchedule() {
        return collectionRepository.findByIdNotIn(scheduleRepository.findAll()
                .stream()
                .map(scheduleModel -> scheduleModel.getCollection().getId())
                .collect(Collectors.toList()));
    }

    private List<CollectionModel> getCollectionsByPresentation() {
        return collectionRepository.findByIdNotIn(presentationRepository.findAll()
                .stream()
                .map(presentationModel -> presentationModel.getCollection().getId())
                .collect(Collectors.toList()));
    }

    private CollectionModel createCollectionModel(String name) {
        return CollectionModel.builder()
                .name(name)
                .build();
    }
}
