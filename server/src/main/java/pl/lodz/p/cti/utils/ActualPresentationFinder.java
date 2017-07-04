package pl.lodz.p.cti.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.lodz.p.cti.models.PresentationModel;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActualPresentationFinder {

    public static PresentationModel findActualPresentation(List<PresentationModel> presentationModelList) {
        log.info("Looking for actual presentations");
        PresentationModel lastBeforeCurrentTime = null;
        PresentationModel last = null;
        LocalTime currentTime = LocalTime.now();
        LocalTime currentTimeRoundedIntoMinutes = LocalTime.MIN.withMinute(currentTime.getMinute()).withHour(currentTime.getHour());
        for (PresentationModel presentationModel : presentationModelList) {
            if (presentationModel.getStartTime().compareTo(currentTimeRoundedIntoMinutes) == 0) {
                return presentationModel;
            }
            if ((lastBeforeCurrentTime == null || lastBeforeCurrentTime.getStartTime().isBefore(presentationModel.getStartTime()))
                    && presentationModel.getStartTime().isBefore(currentTimeRoundedIntoMinutes)) {
                lastBeforeCurrentTime = presentationModel;
            }
            if (last == null || presentationModel.getStartTime().isAfter(last.getStartTime())) {
                last = presentationModel;
            }
        }
        if (lastBeforeCurrentTime != null) {
            log.info("Found presentation: {}", lastBeforeCurrentTime);
            return lastBeforeCurrentTime;
        }
        if (last != null) {
            log.info("Found presentation: {}", last);
            return last;
        }
        log.warn("Don't found any presentation!");
        return null;
    }
}
