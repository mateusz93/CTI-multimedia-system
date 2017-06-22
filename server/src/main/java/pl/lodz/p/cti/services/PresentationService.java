package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.PresentationDAO;
import pl.lodz.p.cti.models.PresentationModel;

import java.time.LocalTime;
import java.util.List;

@Service
public class PresentationService {
    private PresentationDAO presentationDAO;

    @Autowired
    PresentationService(PresentationDAO presentationDAO){
        this.presentationDAO = presentationDAO;
    }

    public List<PresentationModel> findByTvId(Long id) {
        return presentationDAO.findByTvId(id);
    }

    public List<PresentationModel> findAll() {
        return presentationDAO.findAll();
    }

    public List<PresentationModel> deleteByTvId(Long tvId) {
        return presentationDAO.deleteByTvId(tvId);
    }

    public PresentationModel save(PresentationModel presentationModel) {
        return presentationDAO.save(presentationModel);
    }

    public void delete(Long presentationId) {
        presentationDAO.delete(presentationId);
    }

    public PresentationModel findByTvIdAndStartTime(Long tvId, LocalTime startTime) {
        return presentationDAO.findByTvIdAndStartTime(tvId,startTime);
    }
}
