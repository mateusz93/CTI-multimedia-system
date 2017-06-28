package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.services.CollectionObjectService;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.PresentationService;
import pl.lodz.p.cti.services.ScheduleService;
import pl.lodz.p.cti.utils.CollectionWrapper;
import pl.lodz.p.cti.utils.Statements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Controller
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionObjectService collectionObjectService;
    private final CollectionService collectionService;
    private final ObjectService objectService;
    private final PresentationService presentationService;
    private final ScheduleService scheduleService;

    @RequestMapping(value = {"/modifyCollection"}, method = RequestMethod.GET)
    public String modifyCollectionGET(Model model) {
        List<ScheduleModel> scheduleModelList = scheduleService.findAll();
        List<Long> collectionIdUsedList = scheduleModelList.stream().map(scheduleModel -> scheduleModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        return "modifyCollection";
    }

    @RequestMapping(value = {"/addCollection"}, method = RequestMethod.POST)
    public String addCollectionPOST(Model model, @ModelAttribute(name = "collectionWrapper") CollectionWrapper collectionWrapper, String name) {
        List<CollectionObjectModel> collectionObjectModelList = new ArrayList<>();
        Long i = 0L;
        CollectionModel collectionModel = CollectionModel.builder().name(name).build();
        collectionModel = collectionService.save(collectionModel);
        for (Long value : collectionWrapper.getValues()) {
            collectionObjectModelList.add(CollectionObjectModel.builder()
                    .collection(collectionModel)
                    .orderNumber(i)
                    .objectModel(objectService.findOne(value))
                    .build());
            i++;
        }
        collectionObjectService.save(collectionObjectModelList);
        List<PresentationModel> presentationModelList = presentationService.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream().map(presentationModel -> presentationModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        model.addAttribute("green", generateStatement(Statements.SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS, name));
        return "modifyCollection";
    }

    @RequestMapping(value = {"/removeCollection"}, method = RequestMethod.POST)
    public String removeCollectionPOST(Model model, Long collectionId) {
        collectionService.delete(collectionId);
        List<PresentationModel> presentationModelList = presentationService.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream().map(presentationModel -> presentationModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections", collectionIdNotUsedList);
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("collectionWrapper", new CollectionWrapper());
        model.addAttribute("green", generateStatement(Statements.CHOSEN_COLLECTION_REMOVED));
        return "modifyCollection";
    }

}
