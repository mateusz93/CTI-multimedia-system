package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.ScheduleModel;

import java.util.List;

@Transactional
public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long> {
    List<ScheduleModel> deleteByTvId(Long tvId);
    List<ScheduleModel> findByTvId(Long id);
}
