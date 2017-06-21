package pl.lodz.p.cti.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="presentations")
public class PresentationModel {

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

    public PresentationModel(){}

    public PresentationModel(TvModel tv, CollectionModel collection) {
        this.tv = tv;
        this.collection = collection;
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
}
