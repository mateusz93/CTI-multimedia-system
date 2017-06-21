package pl.lodz.p.cti.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name="presentations")
public class PresentationModel implements Comparable<PresentationModel> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    private TvModel tv;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionModel collection;

    @NotNull
    private LocalTime startTime;

    public PresentationModel(){}

    public PresentationModel(TvModel tv, CollectionModel collection, LocalTime startTime) {
        this.tv = tv;
        this.collection = collection;
        this.startTime = startTime;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(PresentationModel o) {
        return tv.getId().equals(o.getTv().getId())?(startTime.isBefore(o.getStartTime())?-1:(startTime.isAfter(o.getStartTime())?1:0)):(int)(tv.getId()-o.getTv().getId());
    }
}
