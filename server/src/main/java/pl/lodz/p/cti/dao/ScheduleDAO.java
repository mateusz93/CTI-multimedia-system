package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.ScheduleModel;

import java.util.List;

@Transactional
public interface ScheduleDAO extends JpaRepository<ScheduleModel, Long> {
    List<ScheduleModel> deleteByTvId(Long tvId);
    List<ScheduleModel> findByTvId(Long id);
}
