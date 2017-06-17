package pl.lodz.p.cti.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="collections")
public class CollectionModel implements Comparable<CollectionModel>{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long collectionId;

    @NotNull
    private Long orderNumber;

    @OneToOne
    @JoinColumn(name = "objectId")
    private ObjectModel objectModel;

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ObjectModel getObjectModel() {
        return objectModel;
    }

    public void setObjectModel(ObjectModel objectModel) {
        this.objectModel = objectModel;
    }

    @Override
    public int compareTo(CollectionModel o) {
        return (int)(orderNumber-o.getOrderNumber());
    }
}
