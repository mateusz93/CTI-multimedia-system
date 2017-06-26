package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.repository.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleModel> findByTvId(Long id) {
        return scheduleRepository.findByTvId(id);
    }

    public List<ScheduleModel> findAll() {
        return scheduleRepository.findAll();
    }

    public List<ScheduleModel> deleteByTvId(Long tvId) {
        return scheduleRepository.deleteByTvId(tvId);
    }

    public ScheduleModel save(ScheduleModel scheduleModel) {
        return scheduleRepository.save(scheduleModel);
    }

    public void delete(Long scheduleId) {
        scheduleRepository.delete(scheduleId);
    }

    public ScheduleModel findOne(Long scheduleId) {
        return scheduleRepository.findOne(scheduleId);
    }
}
