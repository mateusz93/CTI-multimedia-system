package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.models.ScheduleModel;
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

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionObjectRepository collectionObjectRepository;
    private final ScheduleRepository scheduleRepository;
    private final ObjectRepository objectRepository;
    private final PresentationRepository presentationRepository;

    public String getCollection(Model model) {
        List<ScheduleModel> scheduleModelList = scheduleRepository.findAll();
        List<Long> collectionIdUsedList = scheduleModelList.stream()
                .map(scheduleModel -> scheduleModel.getCollection().getId())
                .collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionRepository.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectRepository.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        return "modifyCollection";
    }

    public String addCollection(Model model, String name, CollectionWrapper collectionWrapper) {
        List<CollectionObjectModel> collectionObjectModelList = new ArrayList<>();
        Long i = 0L;
        CollectionModel collectionModel = CollectionModel.builder()
                .name(name)
                .build();
        collectionModel = collectionRepository.save(collectionModel);
        for (Long value : collectionWrapper.getValues()) {
            collectionObjectModelList.add(CollectionObjectModel.builder()
                    .collection(collectionModel)
                    .orderNumber(i)
                    .objectModel(objectRepository.findOne(value))
                    .build());
            i++;
        }
        collectionObjectRepository.save(collectionObjectModelList);
        List<PresentationModel> presentationModelList = presentationRepository.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream()
                .map(presentationModel -> presentationModel.getCollection().getId())
                .collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionRepository.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectRepository.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        model.addAttribute("green", generateStatement(Statements.SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS, name));
        return "modifyCollection";

    }

    public String deleteCollection(Model model, Long collectionId) {
        collectionRepository.delete(collectionId);
        List<PresentationModel> presentationModelList = presentationRepository.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream()
                .map(presentationModel -> presentationModel.getCollection().getId())
                .collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionRepository.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectRepository.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        model.addAttribute("green", generateStatement(Statements.CHOSEN_COLLECTION_REMOVED));
        return "modifyCollection";
    }
}
