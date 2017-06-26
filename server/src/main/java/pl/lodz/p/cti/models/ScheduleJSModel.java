package pl.lodz.p.cti.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"format"})
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJSModel {

    private Long id;
    private String text;
    private String rec_type;
    private String start_date;
    private String end_date;
    private Long event_length;
    private Long event_parent;
    private Long collection;
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);

    public ScheduleJSModel(ScheduleModel model) {
        super();
        this.id = model.getId();
        this.text = model.getText();
        this.rec_type = model.getRecurrence();
        this.start_date = model.getStartTime().format(format);
        this.end_date = model.getEndTime().format(format);
        this.event_length = model.getEventLength();
        this.event_parent = model.getEventParent() != null ? model.getEventParent().getId() : 0;
        this.collection = model.getCollection().getId();
    }
}
