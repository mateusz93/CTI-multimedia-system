package pl.lodz.p.cti.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.cti.exceptions.ValidationException;
import pl.lodz.p.cti.services.TvService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Controller
@RequiredArgsConstructor
public class TVController {

    private final TvService service;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String indexGET(HttpServletRequest request) {
        return service.index(request);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPOST(HttpServletRequest request, String name) {
        return service.initialize(request, name);
    }

    @RequestMapping(value = {"/tv/{hash}/"}, method = RequestMethod.GET)
    public String hashGET(Model model, @PathVariable String hash) throws ValidationException {
        return service.hash(model, hash);
    }
}
