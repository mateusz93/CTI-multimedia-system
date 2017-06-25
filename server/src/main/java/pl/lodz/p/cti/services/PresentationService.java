package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.PresentationRepository;
import pl.lodz.p.cti.models.PresentationModel;

import java.time.LocalTime;
import java.util.List;

@Service
public class PresentationService {
    private PresentationRepository presentationRepository;

    @Autowired
    PresentationService(PresentationRepository presentationRepository){
        this.presentationRepository = presentationRepository;
    }

    public List<PresentationModel> findByTvId(Long id) {
        return presentationRepository.findByTvId(id);
    }

    public List<PresentationModel> findAll() {
        return presentationRepository.findAll();
    }

    public List<PresentationModel> deleteByTvId(Long tvId) {
        return presentationRepository.deleteByTvId(tvId);
    }

    public PresentationModel save(PresentationModel presentationModel) {
        return presentationRepository.save(presentationModel);
    }

    public void delete(Long presentationId) {
        presentationRepository.delete(presentationId);
    }

    public PresentationModel findByTvIdAndStartTime(Long tvId, LocalTime startTime) {
        return presentationRepository.findByTvIdAndStartTime(tvId,startTime);
    }
}
