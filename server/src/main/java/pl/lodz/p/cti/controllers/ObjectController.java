package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.services.ObjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class ObjectController {

    private final ObjectService service;

    @RequestMapping(value = {"/objects"}, method = RequestMethod.GET)
    public String objectsAndPresentationsGET(Model model) {
        return service.getObjectsAndPresentations(model);
    }

    @RequestMapping(value = {"/modifyObject"}, method = RequestMethod.GET)
    public String modifyObjectGET(Model model) {
        return service.getObjects(model);
    }

    @RequestMapping(value = {"/addObject"}, method = RequestMethod.POST)
    public String addObjectPOST(Model model, @RequestParam("image") MultipartFile image, @RequestParam("name") String name) throws ValidationException {
        return service.addObject(model, name, image);
    }

    @RequestMapping(value = {"/removeObject"}, method = RequestMethod.POST)
    public String removeObjectPOST(Model model, Long objectId) {
        return service.deleteObject(model, objectId);
    }

    @RequestMapping(value = {"/getObject"}, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws IOException {
        service.getObject(id, response);
    }
}
