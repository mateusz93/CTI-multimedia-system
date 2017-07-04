package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final CollectionRepository collectionRepository;
    private final ScheduleRepository scheduleRepository;
    private final TvRepository tvRepository;
    private final CommonService commonService;

    private static final String COLLECTIONS = "collections";
    private static final String SCHEDULES = "schedules";
    private static final String TV = "tv";
    private static final String MODIFY_SCHEDULE_ENDPOINT = "modifySchedule";
    private static final String MODIFY_PRESENTATION_ENDPOINT = "redirect:/modifyPresentation";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.ENGLISH);

    public String getActualConfiguration(Model model, Long tvId) {
        log.info("Getting actual schedule configuration for tv with id {}", tvId);
        TvModel tvModel = tvRepository.findOne(tvId);
        if (tvModel == null) {
            return MODIFY_PRESENTATION_ENDPOINT;
        }
        model.addAttribute(COLLECTIONS, getCollectionConfiguration());
        model.addAttribute(SCHEDULES, getSchedulesForConfiguration(tvId));
        model.addAttribute(TV, tvModel);
        return MODIFY_SCHEDULE_ENDPOINT;
    }

    public String modify(Model model, Long tvId, List<ScheduleJSModel> schedules) {
        log.info("Updating schedule configuration for tv with id {}", tvId);
        TvModel tvModel = tvRepository.findOne(tvId);
        if (tvModel == null) {
            return MODIFY_PRESENTATION_ENDPOINT;
        }
        List<ScheduleModel> schedulesToSave = new ArrayList<>();
        List<ScheduleModel> schedulesToRemove = scheduleRepository.findByTvId(tvId);
        for (ScheduleJSModel jsModel : schedules) {
            ScheduleModel scheduleModel = createScheduleModel(tvModel, jsModel);
            scheduleRepository.save(scheduleModel);
            schedulesToSave.add(scheduleModel);
        }
        schedulesToRemove.removeAll(schedulesToSave);
        for (ScheduleModel scheduleModel : schedulesToRemove) {
            log.info("Deleting old schedules");
            scheduleRepository.delete(scheduleModel.getId());
        }
        log.info("Preparing schedule configuration view after updating");
        model.addAttribute(COLLECTIONS, getCollectionConfiguration());
        model.addAttribute(SCHEDULES, schedules);
        model.addAttribute(TV, tvModel);
        commonService.forceTvRefreshById(tvModel.getId());
        return MODIFY_SCHEDULE_ENDPOINT;
    }

    private ScheduleModel createScheduleModel(TvModel tvModel, ScheduleJSModel jsModel) {
        log.info("Creating new schedule");
        ScheduleModel scheduleModel = null;
        if (jsModel.getId() != null) {
            scheduleModel = scheduleRepository.findOne(jsModel.getId());
        }
        if (scheduleModel == null) {
            scheduleModel = new ScheduleModel();
        }
        scheduleModel.setCollection(collectionRepository.findOne(jsModel.getCollection()));
        scheduleModel.setEndTime(LocalDateTime.parse(jsModel.getEnd_date(), format));
        if (jsModel.getEvent_length() != null) {
            scheduleModel.setEventLength(jsModel.getEvent_length());
        } else {
            scheduleModel.setEventLength(0L);
        }
        if (jsModel.getEvent_parent() != null) {
            scheduleModel.setEventParent(scheduleRepository.findOne(jsModel.getEvent_parent()));
        }
        scheduleModel.setRecurrence(jsModel.getRec_type());
        scheduleModel.setStartTime(LocalDateTime.parse(jsModel.getStart_date(), format));
        scheduleModel.setText(jsModel.getText());
        scheduleModel.setTv(tvModel);
        log.info("Created schedule: {}", scheduleModel.toString());
        return scheduleModel;
    }

    private List<ScheduleJSModel> getSchedulesForConfiguration(Long tvId) {
        return scheduleRepository.findByTvId(tvId)
                .stream()
                .map(e -> (new ScheduleJSModel(e)))
                .collect(Collectors.toList());
    }

    private List<MapJSModel> getCollectionConfiguration() {
        return collectionRepository.findAll()
                .stream()
                .map(e -> (new MapJSModel(e)))
                .collect(Collectors.toList());
    }
}
