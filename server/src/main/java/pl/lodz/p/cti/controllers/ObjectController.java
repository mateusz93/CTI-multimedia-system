package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.cti.exceptions.MissingNecessaryObjectException;
import pl.lodz.p.cti.exceptions.UnexpectedErrorException;
import pl.lodz.p.cti.exceptions.UnsupportedExtensionException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.services.CollectionObjectService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.PresentationService;
import pl.lodz.p.cti.utils.Statements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class ObjectController {

    private final CollectionObjectService collectionObjectService;
    private final ObjectService objectService;
    private final PresentationService presentationService;

    @RequestMapping(value = {"/objects"}, method = RequestMethod.GET)
    public String objectsGET(Model model) {
        model.addAttribute("objects", objectService.findAll());
        model.addAttribute("presentations", presentationService.findAll());
        return "objects";
    }

    @RequestMapping(value = {"/modifyObject"}, method = RequestMethod.GET)
    public String modifyObjectGET(Model model) {
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectService.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectService.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        return "modifyObject";
    }

    @RequestMapping(value = {"/addObject"}, method = RequestMethod.POST)
    public String addObjectPOST(Model model, @RequestParam("image") MultipartFile image, @RequestParam("name") String name) throws ValidationException {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                String contentType = image.getContentType();
                if (!"video/mp4".equals(contentType) && !"image/jpeg".equals(contentType) && !"image/jpg".equals(contentType) && !"image/png".equals(contentType) && !"image/gif".equals(contentType)) {
                    throw new UnsupportedExtensionException(contentType);
                }
                if (objectService.findByName(name) != null) {
                    model.addAttribute("red", generateStatement(Statements.OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS, name));
                } else {
                    objectService.save(ObjectModel.builder()
                            .name(name)
                            .contentType(contentType)
                            .image(bytes)
                            .build());
                    model.addAttribute("green", generateStatement(Statements.SAVE_OBJECT_WITH_GIVEN_NAME_SUCCESS, name));
                }
            } catch (Exception e) {
                throw new UnexpectedErrorException(e);
            }
        } else {
            throw new MissingNecessaryObjectException();
        }
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectService.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectService.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        return "modifyObject";
    }

    @RequestMapping(value = {"/removeObject"}, method = RequestMethod.POST)
    public String removeObjectPOST(Model model, Long objectId) {
        objectService.delete(objectId);
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectService.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectService.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        model.addAttribute("green", generateStatement(Statements.CHOSEN_OBJECT_REMOVED));
        return "modifyObject";
    }

    @RequestMapping(value = {"/getObject"}, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws IOException {
        ObjectModel pict = objectService.findOne(id);
        response.setContentType("video/mp4, image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(pict.getImage());
        response.getOutputStream().close();
    }
}
