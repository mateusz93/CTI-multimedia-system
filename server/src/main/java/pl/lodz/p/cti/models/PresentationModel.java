package pl.lodz.p.cti.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="presentations")
public class PresentationModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private Long tvId;

    @NotNull
    private Long collectionId;

    public Long getTvId() {
        return tvId;
    }

    public void setTvId(Long tvId) {
        this.tvId = tvId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }
}
