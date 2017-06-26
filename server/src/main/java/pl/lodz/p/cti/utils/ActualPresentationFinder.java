package pl.lodz.p.cti.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.cti.models.PresentationModel;

import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActualPresentationFinder {

    public static PresentationModel findActualPresentation(List<PresentationModel> presentationModelList) {
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
        return lastBeforeCurrentTime != null ? lastBeforeCurrentTime : last;
    }
}
