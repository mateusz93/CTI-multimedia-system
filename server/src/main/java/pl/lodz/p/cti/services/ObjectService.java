package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.cti.exceptions.MissingNecessaryObjectException;
import pl.lodz.p.cti.exceptions.UnexpectedErrorException;
import pl.lodz.p.cti.exceptions.UnsupportedExtensionException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.repository.CollectionObjectRepository;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.repository.PresentationRepository;
import pl.lodz.p.cti.utils.Statements;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final CollectionObjectRepository collectionObjectRepository;
    private final ObjectRepository objectRepository;
    private final PresentationRepository presentationRepository;

    public String getObjectsAndPresentations(Model model) {
        model.addAttribute("objects", objectRepository.findAll());
        model.addAttribute("presentations", presentationRepository.findAll());
        return "objects";
    }

    public String getObjects(Model model) {
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectRepository.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream()
                .map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId())
                .collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectRepository.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        return "modifyObject";
    }

    public String addObject(Model model, String name, MultipartFile image) throws ValidationException {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                String contentType = image.getContentType();
                if (!"video/mp4".equals(contentType) && !"image/jpeg".equals(contentType) && !"image/jpg".equals(contentType) && !"image/png".equals(contentType) && !"image/gif".equals(contentType)) {
                    throw new UnsupportedExtensionException(contentType);
                }
                if (objectRepository.findByName(name) != null) {
                    model.addAttribute("red", generateStatement(Statements.OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS, name));
                } else {
                    objectRepository.save(ObjectModel.builder()
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
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectRepository.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectRepository.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        return "modifyObject";
    }

    public String deleteObject(Model model, Long objectId) {
        objectRepository.delete(objectId);
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectRepository.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream()
                .map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId())
                .collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectRepository.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects", objectIdNotUsedList);
        model.addAttribute("green", generateStatement(Statements.CHOSEN_OBJECT_REMOVED));
        return "modifyObject";

    }

    public void getObject(Long id, HttpServletResponse response) throws IOException {
        ObjectModel pict = objectRepository.findOne(id);
        response.setContentType("video/mp4, image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(pict.getImage());
        response.getOutputStream().close();
    }
}
