package pl.lodz.p.cti.models;

import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class ScheduleJSModel {
	private Long id;
	private String text;
	private String rec_type;
	private String start_date;
	private String end_date;
	private Long event_length;
	private Long event_parent;
	private Long collection;
	public ScheduleJSModel() {
		super();
	}
	public ScheduleJSModel(ScheduleModel model) {
		super();
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		this.id = model.getId();
		this.text = model.getText();
		this.rec_type = model.getRecurrence();
		this.start_date = model.getStartTime().format(format);
		this.end_date = model.getEndTime().format(format);
		this.event_length = model.getEventLength();
		this.event_parent = model.getEventParent() != null ? model.getEventParent().getId() : 0;
		this.collection = model.getCollection().getId();
	}
	public ScheduleJSModel(Long id, String text, String rec_type, String start_date, String end_date, Long event_length,
			Long event_parent, Long collection) {
		super();
		this.id = id;
		this.text = text;
		this.rec_type = rec_type;
		this.start_date = start_date;
		this.end_date = end_date;
		this.event_length = event_length;
		this.event_parent = event_parent;
		this.collection = collection;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRec_type() {
		return rec_type;
	}
	public void setRec_type(String rec_type) {
		this.rec_type = rec_type;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public Long getEvent_length() {
		return event_length;
	}
	public void setEvent_length(Long event_length) {
		this.event_length = event_length;
	}
	public Long getEvent_parent() {
		return event_parent;
	}
	public void setEvent_parent(Long event_parent) {
		this.event_parent = event_parent;
	}
	public Long getCollection() {
		return collection;
	}
	public void setCollection(Long collection) {
		this.collection = collection;
	}
}
