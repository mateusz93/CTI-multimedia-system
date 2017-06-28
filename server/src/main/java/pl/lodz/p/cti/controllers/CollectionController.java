package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.utils.CollectionWrapper;

@Controller
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService service;

    @RequestMapping(value = {"/modifyCollection"}, method = RequestMethod.GET)
    public String modifyGET(Model model) {
        return service.getCollection(model);
    }

    @RequestMapping(value = {"/addCollection"}, method = RequestMethod.POST)
    public String addPOST(Model model, @ModelAttribute(name = "collectionWrapper") CollectionWrapper collectionWrapper, String name) {
        return service.addCollection(model, name, collectionWrapper);
    }

    @RequestMapping(value = {"/removeCollection"}, method = RequestMethod.POST)
    public String removePOST(Model model, Long collectionId) {
        return service.deleteCollection(model, collectionId);
    }

}
