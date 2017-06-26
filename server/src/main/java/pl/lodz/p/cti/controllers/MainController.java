package pl.lodz.p.cti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.cti.exceptions.MissingNecessaryObjectException;
import pl.lodz.p.cti.exceptions.TvModelDoesntExistsException;
import pl.lodz.p.cti.exceptions.UnexpectedErrorException;
import pl.lodz.p.cti.exceptions.UnsupportedExtensionException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.services.CollectionObjectService;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.services.ConfigurationService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.PresentationService;
import pl.lodz.p.cti.services.TvService;
import pl.lodz.p.cti.utils.CollectionWrapper;
import pl.lodz.p.cti.utils.Statements;
import pl.lodz.p.cti.utils.StringToLocalTimeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.cti.utils.ActualPresentationFinder.findActualPresentation;
import static pl.lodz.p.cti.utils.SessionIdentifierGenerator.nextSessionId;
import static pl.lodz.p.cti.utils.Statements.generateStatement;

@Controller
public class MainController {

    private CollectionObjectService collectionObjectService;

    private CollectionService collectionService;

    private ConfigurationService configurationService;

    private ObjectService objectService;

    private PresentationService presentationService;

    private TvService tvService;

    @Autowired
    public MainController(CollectionObjectService collectionObjectService, CollectionService collectionService, ConfigurationService configurationService, ObjectService objectService, PresentationService presentationService, TvService tvService){
        this.collectionObjectService = collectionObjectService;
        this.collectionService = collectionService;
        this.configurationService = configurationService;
        this.objectService = objectService;
        this.presentationService = presentationService;
        this.tvService = tvService;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String indexGET(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        TvModel tvModel = tvService.findByIp(ip);
        return tvModel == null ? "index" : "redirect:/tv/"+tvModel.getHash()+"/";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPOST(HttpServletRequest request, String name) throws Exception {
        String ip = request.getRemoteAddr();
        String hash;
        do {
            hash = nextSessionId();
        } while (tvService.findByHash(hash) != null);
        return "redirect:/tv/"+tvService.save(new TvModel(ip, name, hash)).getHash()+"/";
    }

    @RequestMapping(value={"/tv/{hash}/"},method = RequestMethod.GET)
    public String hashGET(Model model, @PathVariable String hash) throws ValidationException {
        TvModel tvModel = tvService.findByHash(hash);
        if(tvModel == null){
            throw new TvModelDoesntExistsException(TvModel.PROPERTY_HASH, hash);
        }
        PresentationModel actualPresentation = findActualPresentation(presentationService.findByTvId(tvModel.getId()));
        if(actualPresentation==null) {
            model.addAttribute("objects", null);
        } else {
            model.addAttribute("objects", actualPresentation.getCollection().getCollectionObjects()
                    .stream().map(CollectionObjectModel::getObjectModel).collect(Collectors.toList()));
        }
        model.addAttribute("tvId",tvModel.getId());
    	ConfigurationModel displayTime =  configurationService.findByName("displayTime");
    	ConfigurationModel displayBubbles =  configurationService.findByName("displayBubbles");
    	ConfigurationModel displayHeader =  configurationService.findByName("displayHeader");
    	ConfigurationModel headerText =  configurationService.findByName("headerText");
        model.addAttribute("displayTime", displayTime == null ? 5 : Integer.valueOf(displayTime.getValue()));
        model.addAttribute("displayBubbles", displayBubbles == null ? "true" : displayBubbles.getValue());
        model.addAttribute("displayHeader", displayHeader == null ? true : displayHeader.getValue().equals("true"));
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        return "presentation";
    }

    @RequestMapping(value={"/objects"},method = RequestMethod.GET)
    public String objectsGET(Model model) {
        model.addAttribute("objects",objectService.findAll());
        model.addAttribute("presentations",presentationService.findAll());
        return "objects";
    }

    @RequestMapping(value={"/presentations"},method = RequestMethod.GET)
    public String presentationsGET(Model model) throws ValidationException {
        List<PresentationModel> presentations = presentationService.findAll();
        Collections.sort(presentations);
        model.addAttribute("presentations",presentations);
        return "presentations";
    }


    @RequestMapping(value={"/modifyObject"},method = RequestMethod.GET)
    public String modifyObjectGET(Model model) throws ValidationException {
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectService.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectService.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects",objectIdNotUsedList);
        return "modifyObject";
    }

    @RequestMapping(value={"/addObject"},method = RequestMethod.POST)
    public String addObjectPOST(Model model, @RequestParam("image") MultipartFile image, @RequestParam("name") String name) throws ValidationException {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                String contentType = image.getContentType();
                if(!"video/mp4".equals(contentType)&&!"image/jpeg".equals(contentType)&&!"image/jpg".equals(contentType)&&!"image/png".equals(contentType)&&!"image/gif".equals(contentType)){
                    throw new UnsupportedExtensionException(contentType);
                }
                if(objectService.findByName(name)!=null){
                    model.addAttribute("red",generateStatement(Statements.OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS,name));
                } else {
                    objectService.save(new ObjectModel(name, contentType, bytes));
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
        model.addAttribute("objects",objectIdNotUsedList);
        return "modifyObject";
    }

    @RequestMapping(value={"/removeObject"},method = RequestMethod.POST)
    public String removeObjectPOST(Model model, Long objectId) throws ValidationException {
        objectService.delete(objectId);
        List<CollectionObjectModel> collectionObjectModelList = collectionObjectService.findAll();
        List<Long> objectIdUsedList = collectionObjectModelList.stream().map(collectionObjectModel -> collectionObjectModel.getObjectModel().getId()).collect(Collectors.toList());
        List<ObjectModel> objectIdNotUsedList = objectService.findByIdNotIn(objectIdUsedList);
        model.addAttribute("objects",objectIdNotUsedList);
        model.addAttribute("green",generateStatement(Statements.CHOSEN_OBJECT_REMOVED));
        return "modifyObject";
    }

    @RequestMapping(value = {"/getObject"}, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws IOException {
        ObjectModel pict = objectService.findOne(id);
        response.setContentType("video/mp4, image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(pict.getImage());
        response.getOutputStream().close();
    }

    @RequestMapping(value={"/modifyPresentation"},method = RequestMethod.GET)
    public String modifyPresentationGET(Model model) throws ValidationException {
        model.addAttribute("presentations",presentationService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        model.addAttribute("tvs", tvService.findAll());
        return "modifyPresentation";
    }

    @RequestMapping(value={"/addPresentation"},method = RequestMethod.POST)
    public String addPresentationPOST(Model model, Long tvId, Long collectionId, String time) throws ValidationException {
        LocalTime startTime = new StringToLocalTimeConverter(time).getLocalTime();
        PresentationModel presentationModel = presentationService.findByTvIdAndStartTime(tvId,startTime);
        if(presentationModel!=null){
            model.addAttribute("red",generateStatement(Statements.PRESENTATION_WITH_GIVEN_DATA_ALREADY_EXISTS));
        } else {
            presentationService.save(new PresentationModel(tvService.findOne(tvId), collectionService.findOne(collectionId), startTime));
            model.addAttribute("green", generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        }
        model.addAttribute("presentations",presentationService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        model.addAttribute("tvs", tvService.findAll());
        return "modifyPresentation";
    }

    @RequestMapping(value={"/removePresentation"},method = RequestMethod.POST)
    public String deletePresentationPOST(Model model, Long presentationId) throws ValidationException {
        presentationService.delete(presentationId);
        model.addAttribute("presentations",presentationService.findAll());
        model.addAttribute("collections",collectionService.findAll());
        model.addAttribute("tvs",tvService.findAll());
        model.addAttribute("green",generateStatement(Statements.SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS));
        return "modifyPresentation";
    }

    @RequestMapping(value={"/modifyCollection"},method = RequestMethod.GET)
    public String modifyCollectionGET(Model model) throws ValidationException {
        List<PresentationModel> presentationModelList = presentationService.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream().map(presentationModel -> presentationModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections",collectionIdNotUsedList);
        model.addAttribute("objects",objectService.findAll());
        model.addAttribute("collectionWrapper",new CollectionWrapper());
        return "modifyCollection";
    }

    @RequestMapping(value={"/addCollection"},method = RequestMethod.POST)
    public String addCollectionPOST(Model model, @ModelAttribute(name="collectionWrapper") CollectionWrapper collectionWrapper, String name) throws ValidationException {
        List<CollectionObjectModel> collectionObjectModelList = new ArrayList<>();
        Long i=0l;
        CollectionModel collectionModel = new CollectionModel(name);
        collectionModel = collectionService.save(collectionModel);
        for(Long value : collectionWrapper.getValues()){
            collectionObjectModelList.add(new CollectionObjectModel(collectionModel,i,objectService.findOne(value)));
            i++;
        }
        collectionObjectService.save(collectionObjectModelList);
        List<PresentationModel> presentationModelList = presentationService.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream().map(presentationModel -> presentationModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections",collectionIdNotUsedList);
        model.addAttribute("objects",objectService.findAll());
        model.addAttribute("collectionWrapper",new CollectionWrapper());
        model.addAttribute("green",generateStatement(Statements.SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS,name));
        return "modifyCollection";
    }

    @RequestMapping(value={"/removeCollection"},method = RequestMethod.POST)
    public String removeCollectionPOST(Model model,Long collectionId) throws ValidationException {
        collectionService.delete(collectionId);
        List<PresentationModel> presentationModelList = presentationService.findAll();
        List<Long> collectionIdUsedList = presentationModelList.stream().map(presentationModel -> presentationModel.getCollection().getId()).collect(Collectors.toList());
        List<CollectionModel> collectionIdNotUsedList = collectionService.findByIdNotIn(collectionIdUsedList);
        model.addAttribute("collections",collectionIdNotUsedList);
        model.addAttribute("objects",objectService.findAll());
        model.addAttribute("collectionWrapper",new CollectionWrapper());
        model.addAttribute("green",generateStatement(Statements.CHOSEN_COLLECTION_REMOVED));
        return "modifyCollection";
    }
    
    @RequestMapping(value={"/configuration"},method = RequestMethod.GET)
    public String modifyConfigurationGET(Model model) throws ValidationException {
    	ConfigurationModel displayTime =  configurationService.findByName("displayTime");
    	ConfigurationModel displayBubbles =  configurationService.findByName("displayBubbles");
    	ConfigurationModel displayHeader =  configurationService.findByName("displayHeader");
    	ConfigurationModel headerText =  configurationService.findByName("headerText");
        model.addAttribute("displayTime", displayTime == null ? "5" : displayTime.getValue());
        model.addAttribute("displayBubbles", displayBubbles == null ? "true" : displayBubbles.getValue());
        model.addAttribute("displayHeader", displayHeader == null ? "true" : displayHeader.getValue());
        model.addAttribute("headerText", headerText == null ? "" : headerText.getValue());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }
    
    @RequestMapping(value={"/configuration"},method = RequestMethod.POST)
    public String modifyConfigurationPOST(Model model, String displayTime, String displayBubbles, String displayHeader, String headerText) throws ValidationException {
    	ConfigurationModel displayTimeObj =  configurationService.findByName("displayTime");
    	if (displayTimeObj == null)
    	{
    		displayTimeObj = new ConfigurationModel();
    		displayTimeObj.setName("displayTime");
    		displayTimeObj.setValue("5");
    	}
    	try
    	{
    		int time = Integer.valueOf(displayTime);
    		if (time > 0) displayTimeObj.setValue(displayTime);
    	} catch (NumberFormatException nfe)
    	{
    		//Ignore
    	}
    	configurationService.save(displayTimeObj);
    	ConfigurationModel displayBubblesObj =  configurationService.findByName("displayBubbles");
    	if (displayBubblesObj == null)
    	{
    		displayBubblesObj = new ConfigurationModel();
    		displayBubblesObj.setName("displayBubbles");
    		displayBubblesObj.setValue("true");
    	}
    	if (displayBubbles.equals("true") || displayBubbles.equals("false")) displayBubblesObj.setValue(displayBubbles);
    	configurationService.save(displayBubblesObj);
    	ConfigurationModel displayHeaderObj =  configurationService.findByName("displayHeader");
    	if (displayHeaderObj == null)
    	{
    		displayHeaderObj = new ConfigurationModel();
    		displayHeaderObj.setName("displayHeader");
    		displayHeaderObj.setValue("true");
    	}
    	if (displayHeader.equals("true") || displayHeader.equals("false")) displayHeaderObj.setValue(displayHeader);
    	configurationService.save(displayHeaderObj);
    	ConfigurationModel headerTextObj =  configurationService.findByName("headerText");
    	if (headerTextObj == null)
    	{
    		headerTextObj = new ConfigurationModel();
    		headerTextObj.setName("headerText");
    		headerTextObj.setValue("");
    	}
    	headerTextObj.setValue(headerText);
    	configurationService.save(headerTextObj);
        model.addAttribute("displayTime", displayTimeObj.getValue());
        model.addAttribute("displayBubbles", displayBubblesObj.getValue());
        model.addAttribute("displayHeader", displayHeaderObj.getValue());
        model.addAttribute("headerText", headerTextObj.getValue());
        model.addAttribute("trueValue", "true");
        return "configuration";
    }

/*    @MessageMapping("/register")
    @SendTo("/topic/schedule")
    public ForceRefreshMessage registerWebsocketEndpoint(RegisterMessage message) throws Exception {
        TvModel tvModel = tvService.findByHash(message.getHash());
        if(tvModel==null){
            throw new TvModelDoesntExistsException(TvModel.PROPERTY_HASH,message.getHash());
        }
        PresentationModel presentationModel = presentationService.findByTvId(tvModel.getId());
        if(presentationModel==null){
            return new ForceRefreshMessage(Boolean.FALSE);
        } else {
            return new ForceRefreshMessage(Boolean.TRUE);
        }
    }*/
}
