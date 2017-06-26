package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.ScheduleRepository;
import pl.lodz.p.cti.models.ScheduleModel;

import java.util.List;

@Service
public class ScheduleService {
    private ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

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
