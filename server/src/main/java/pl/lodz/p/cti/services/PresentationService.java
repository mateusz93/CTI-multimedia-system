package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.PresentationDAO;
import pl.lodz.p.cti.models.PresentationModel;

import java.util.List;

@Service
public class PresentationService {
    private PresentationDAO presentationDAO;

    @Autowired
    PresentationService(PresentationDAO presentationDAO){
        this.presentationDAO = presentationDAO;
    }

    public PresentationModel findByTvId(Long id) {
        return presentationDAO.findByTvId(id);
    }

    public List<PresentationModel> findAll() {
        return presentationDAO.findAll();
    }
}
