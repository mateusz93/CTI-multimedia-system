package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.PresentationModel;

import java.time.LocalTime;
import java.util.List;

public interface PresentationRepository extends JpaRepository<PresentationModel, Long> {
    PresentationModel findByTvIdAndStartTime(Long tvId, LocalTime startTime);
    List<PresentationModel> deleteByTvId(Long tvId);
    List<PresentationModel> findByTvId(Long id);
}
