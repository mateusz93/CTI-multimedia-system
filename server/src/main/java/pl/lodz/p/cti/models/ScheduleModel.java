package pl.lodz.p.cti.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name="schedules")
public class ScheduleModel {

    @Id
    @GeneratedValue
    private Long id;

    private String text;
    
    private String recurrence;
    
    @NotNull
    @OneToOne
    private TvModel tv;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionModel collection;

    @NotNull
    private LocalDateTime startTime;
    
    @NotNull
    private LocalDateTime endTime;
    
    @NotNull
    private Long eventLength;
    
    @ManyToOne(optional = true)
    private ScheduleModel eventParent;
    
    

    public ScheduleModel(){}

	public ScheduleModel(Long id, String text, String recurrence, TvModel tv, CollectionModel collection,
			LocalDateTime startTime, LocalDateTime endTime, Long eventLength, ScheduleModel eventParent) {
		super();
		this.id = id;
		this.text = text;
		this.recurrence = recurrence;
		this.tv = tv;
		this.collection = collection;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventLength = eventLength;
		this.eventParent = eventParent;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TvModel getTv() {
        return tv;
    }

    public void setTv(TvModel tv) {
        this.tv = tv;
    }

    public CollectionModel getCollection() {
        return collection;
    }

    public void setCollection(CollectionModel collection) {
        this.collection = collection;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getEventLength() {
		return eventLength;
	}

	public void setEventLength(Long eventLength) {
		this.eventLength = eventLength;
	}

	public ScheduleModel getEventParent() {
		return eventParent;
	}

	public void setEventParent(ScheduleModel eventParent) {
		this.eventParent = eventParent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleModel other = (ScheduleModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
