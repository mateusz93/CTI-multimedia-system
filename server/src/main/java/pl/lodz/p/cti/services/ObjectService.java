package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.cti.exceptions.MissingNecessaryObjectException;
import pl.lodz.p.cti.exceptions.UnexpectedErrorException;
import pl.lodz.p.cti.exceptions.UnsupportedExtensionException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.repository.CollectionObjectRepository;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.repository.PresentationRepository;
import pl.lodz.p.cti.utils.ContentTypeUtils;
import pl.lodz.p.cti.utils.Statements;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Service
@Transactional
@RequiredArgsConstructor
public class ObjectService {

    private final CollectionObjectRepository collectionObjectRepository;
    private final ObjectRepository objectRepository;
    private final PresentationRepository presentationRepository;
    private final ContentTypeUtils contentTypeUtils;

    private static final String OBJECTS = "objects";
    private static final String PRESENTATIONS = "presentations";
    private static final String OBJECTS_ENDPOINT = "objects";
    private static final String MODIFY_OBJECT_ENDPOINT = "modifyObject";
    private static final String GREEN = "green";
    private static final String RED = "red";

    public String getObjectsAndPresentations(Model model) {
        model.addAttribute(OBJECTS, objectRepository.findAll());
        model.addAttribute(PRESENTATIONS, presentationRepository.findAll());
        return OBJECTS_ENDPOINT;
    }

    public String getObjects(Model model) {
        model.addAttribute(OBJECTS, getObjectsForModify());
        return MODIFY_OBJECT_ENDPOINT;
    }

    public String addObject(Model model, String name, MultipartFile image) throws ValidationException {
        if (image.isEmpty()) {
            throw new MissingNecessaryObjectException();
        }
        try {
            String contentType = image.getContentType();
            if (!contentTypeUtils.isSupported(contentType)) {
                throw new UnsupportedExtensionException(contentType);
            }
            if (objectRepository.findByName(name) != null) {
                model.addAttribute(RED, generateStatement(Statements.OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS, name));
            } else {
                ObjectModel objectModel = ObjectModel.builder()
                        .name(name)
                        .contentType(contentType)
                        .image(image.getBytes())
                        .build();
                objectRepository.save(objectModel);
                model.addAttribute(GREEN, generateStatement(Statements.SAVE_OBJECT_WITH_GIVEN_NAME_SUCCESS, name));
            }
        } catch (Exception e) {
            throw new UnexpectedErrorException(e);
        }
        model.addAttribute(OBJECTS, getObjectsForModify());
        return MODIFY_OBJECT_ENDPOINT;
    }

    public String deleteObject(Model model, Long objectId) {
        objectRepository.delete(objectId);

        model.addAttribute(OBJECTS, getObjectsForModify());
        model.addAttribute(GREEN, generateStatement(Statements.CHOSEN_OBJECT_REMOVED));
        return MODIFY_OBJECT_ENDPOINT;
    }

    public void getObject(Long id, HttpServletResponse response) throws IOException {
        ObjectModel pict = objectRepository.findOne(id);

        response.setContentType(contentTypeUtils.getExtension());
        response.getOutputStream().write(pict.getImage());
        response.getOutputStream().close();
    }

    private List<ObjectModel> getObjectsForModify() {
        return objectRepository.findByIdNotIn(collectionObjectRepository.findAll()
                .stream()
                .map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId())
                .collect(Collectors.toList()));
    }
}
