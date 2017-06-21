package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.PresentationModel;

import java.util.List;

@Transactional
public interface PresentationDAO extends JpaRepository<PresentationModel, Long> {
    PresentationModel findByTvId(Long id);
    List<PresentationModel> deleteByTvId(Long tvId);
}
