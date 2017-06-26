package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.PresentationModel;
import pl.lodz.p.cti.repository.PresentationRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepository;

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
        return presentationRepository.findByTvIdAndStartTime(tvId, startTime);
    }
}
