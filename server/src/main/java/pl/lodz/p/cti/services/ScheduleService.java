package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lodz.p.cti.models.MapJSModel;
import pl.lodz.p.cti.models.ScheduleJSModel;
import pl.lodz.p.cti.models.ScheduleModel;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.repository.CollectionRepository;
import pl.lodz.p.cti.repository.ScheduleRepository;
import pl.lodz.p.cti.repository.TvRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final CollectionRepository collectionRepository;
    private final ScheduleRepository scheduleRepository;
    private final TvRepository tvRepository;
    private final CommonService commonService;

    public String getActualConfiguration(Model model, Long tvId) {
        TvModel tvModel = tvRepository.findOne(tvId);
        if (tvModel == null) {
            return "redirect:/modifyPresentation";
        }
        model.addAttribute("collections", collectionRepository.findAll().stream()
                .map(e -> (new MapJSModel(e)))
                .collect(Collectors.toList()));
        model.addAttribute("schedules", scheduleRepository.findByTvId(tvId).stream()
                .map(e -> (new ScheduleJSModel(e)))
                .collect(Collectors.toList()));
        model.addAttribute("tv", tvModel);
        return "modifySchedule";

    }

    public String modify(Model model, Long tvId, List<ScheduleJSModel> schedules) {
        TvModel tvModel = tvRepository.findOne(tvId);
        if (tvModel == null) {
            return "redirect:/modifyPresentation";
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        List<ScheduleModel> schedulesToSave = new ArrayList<>();
        List<ScheduleModel> schedulesToRemove = scheduleRepository.findByTvId(tvId);
        for (ScheduleJSModel m : schedules) {
            ScheduleModel scheduleModel = null;
            if (m.getId() != null) scheduleModel = scheduleRepository.findOne(m.getId());
            if (scheduleModel == null) scheduleModel = new ScheduleModel();
            scheduleModel.setCollection(collectionRepository.findOne(m.getCollection()));
            scheduleModel.setEndTime(LocalDateTime.parse(m.getEnd_date(), format));
            if (m.getEvent_length() != null) scheduleModel.setEventLength(m.getEvent_length());
            else scheduleModel.setEventLength(0L);
            if (m.getEvent_parent() != null) scheduleModel.setEventParent(scheduleRepository.findOne(m.getEvent_parent()));
            scheduleModel.setRecurrence(m.getRec_type());
            scheduleModel.setStartTime(LocalDateTime.parse(m.getStart_date(), format));
            scheduleModel.setText(m.getText());
            scheduleModel.setTv(tvModel);
            scheduleRepository.save(scheduleModel);
            schedulesToSave.add(scheduleModel);
        }
        schedulesToRemove.removeAll(schedulesToSave);
        for (ScheduleModel m : schedulesToRemove) {
            scheduleRepository.delete(m.getId());
        }
        model.addAttribute("collections", collectionRepository.findAll().stream().map(e -> (new MapJSModel(e))).collect(Collectors.toList()));
        model.addAttribute("schedules", schedules);
        model.addAttribute("tv", tvModel);
        commonService.forceRefresh(tvModel.getId());
        return "modifySchedule";

    }
}
