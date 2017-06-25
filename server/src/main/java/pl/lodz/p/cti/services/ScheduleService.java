package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.ScheduleDAO;
import pl.lodz.p.cti.models.ScheduleModel;

import java.util.List;

@Service
public class ScheduleService {
    private ScheduleDAO scheduleDAO;

    @Autowired
    ScheduleService(ScheduleDAO scheduleDAO){
        this.scheduleDAO = scheduleDAO;
    }

    public List<ScheduleModel> findByTvId(Long id) {
        return scheduleDAO.findByTvId(id);
    }

    public List<ScheduleModel> findAll() {
        return scheduleDAO.findAll();
    }

    public List<ScheduleModel> deleteByTvId(Long tvId) {
        return scheduleDAO.deleteByTvId(tvId);
    }

    public ScheduleModel save(ScheduleModel scheduleModel) {
        return scheduleDAO.save(scheduleModel);
    }

    public void delete(Long scheduleId) {
    	scheduleDAO.delete(scheduleId);
    }

    public ScheduleModel findOne(Long scheduleId) {
        return scheduleDAO.findOne(scheduleId);
    }
}
