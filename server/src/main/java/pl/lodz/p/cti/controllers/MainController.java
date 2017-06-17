package pl.lodz.p.cti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.exceptions.TvModelDoesntExistsException;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.services.CollectionService;
import pl.lodz.p.cti.services.ConfigurationService;
import pl.lodz.p.cti.services.ObjectService;
import pl.lodz.p.cti.services.PresentationService;
import pl.lodz.p.cti.services.TvService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static pl.lodz.p.cti.utils.SessionIdentifierGenerator.nextSessionId;

@Controller
public class MainController {

    private CollectionService collectionService;

    private ConfigurationService configurationService;

    private ObjectService objectService;

    private PresentationService presentationService;

    private TvService tvService;

    @Autowired
    public MainController(CollectionService collectionService, ConfigurationService configurationService, ObjectService objectService, PresentationService presentationService, TvService tvService){
        this.collectionService = collectionService;
        this.configurationService = configurationService;
        this.objectService = objectService;
        this.presentationService = presentationService;
        this.tvService = tvService;
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String indexGET(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        TvModel tvModel = tvService.findByIp(ip);
        return tvModel == null ? "index" : "redirect:/"+tvModel.getHash()+"/";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPOST(HttpServletRequest request, String name) throws Exception {
        String ip = request.getRemoteAddr();
        String hash;
        do {
            hash = nextSessionId();
        } while (tvService.findByHash(hash) != null);
        return "redirect:/"+tvService.save(new TvModel(ip, name, hash))+"/";
    }

    @RequestMapping(value={"/{hash}/"},method = RequestMethod.GET)
    public String hashGET(Model model, @PathVariable String hash) throws ValidationException {
        TvModel tvModel = tvService.findByHash(hash);
        if(tvModel == null){
            throw new TvModelDoesntExistsException(TvModel.PROPERTY_HASH, hash);
        }
        PresentationModel presentationModel = presentationService.findByTvId(tvModel.getId());
        if(presentationModel==null){
            model.addAttribute("presentation",null);
        } else {
            List<CollectionModel> collections = collectionService.findByCollectionId(presentationModel.getCollectionId());
            Collections.sort(collections);
            model.addAttribute("presentation",collections);
        }
        return "presentation";
    }
/*

    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public String removeUserPOST(Model model, long userId) {
        try{
            userService.delete(userId);
            model.addAttribute("success","Udało się usunąć użytkownika!");
        } catch (UnsupportedOperationException exc){
            model.addAttribute("error",exc.getMessage());
        }
        model.addAttribute("users",userService.listUser());
        return "removeUser";
    }

    @RequestMapping(value={"/addDeposit"},method = RequestMethod.GET)
    public String addDepositGET(Model model) {
        model.addAttribute("users",userService.listUser());
        return "addDeposit";
    }

    @RequestMapping(value={"/addDeposit"},method = RequestMethod.POST)
    public String addDepositPOST(Model model, int userId, Double balance) {
        UserModel user = userService.findOne(userId);
        if(balance==null){
            model.addAttribute("error", "Nie podano wysokości wpłaty!");
        } else {
            user.setBalance(user.getBalance() + balance);
            try {
                userService.update(user);
                model.addAttribute("success","Udało się dokonać wpłaty!");
            } catch (UnsupportedOperationException exc) {
                model.addAttribute("error", exc.getMessage());
            }
        }
        model.addAttribute("users",userService.listUser());
        return "addDeposit";
    }

    @RequestMapping(value = "/makeWithdraw", method = RequestMethod.GET)
    public String makeWithdrawGET(Model model) {
        model.addAttribute("users",userService.listUser());
        return "makeWithdraw";
    }

    @RequestMapping(value = "/makeWithdraw", method = RequestMethod.POST)
    public String makeWithdrawPOST(Model model, int userId, Double balance) {
        UserModel user = userService.findOne(userId);
        if(balance==null) {
            model.addAttribute("error", "Nie podano wysokości wypłaty!");
        } else if(user.getBalance()-balance<0) {
            model.addAttribute("error","Błąd! Kwota po wypłacnie byłaby mniejsza od 0!");
        } else{
            user.setBalance(user.getBalance() - balance);
            try {
                userService.update(user);
                model.addAttribute("success","Udało się dokonać wypłaty");
            }catch(UnsupportedOperationException exc){
                model.addAttribute("error",exc.getMessage());
            }
        }
        model.addAttribute("users",userService.listUser());
        return "makeWithdraw";
    }

    @RequestMapping(value = "/makePayment", method = RequestMethod.GET)
    public String makePaymentGET(Model model) {
        model.addAttribute("users",userService.listUser());
        return "makePayment";
    }

    @RequestMapping(value={"/makePayment"},method = RequestMethod.POST)
    public String makePaymentPOST(Model model, int userSourceId, int userDestinationId, double balance) {
        UserModel sourceUser = userService.findOne(userSourceId);
        UserModel destinationUser = userService.findOne(userDestinationId);
        try{
            userService.makePayment(sourceUser,destinationUser,balance);
            model.addAttribute("success","Przelew został zrealizowany!");
        } catch (UnsupportedOperationException exc) {
            model.addAttribute("error",exc.getMessage());
        }
        model.addAttribute("users",userService.listUser());
        return "makePayment";
    }*/

}
