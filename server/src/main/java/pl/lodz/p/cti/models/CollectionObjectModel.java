package pl.lodz.p.cti.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="collection_objects")
public class CollectionObjectModel{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionModel collection;

    @NotNull
    private Long orderNumber;

    @ManyToOne////////////////////////*(cascade = CascadeType.REMOVE)*/
    @JoinColumn(name = "object_id")
    private ObjectModel objectModel;

    public CollectionObjectModel(){}

    public CollectionObjectModel(CollectionModel collection, Long orderNumber, ObjectModel objectModel) {
        this.collection = collection;
        this.orderNumber = orderNumber;
        this.objectModel = objectModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionModel getCollection() {
        return collection;
    }

    public void setCollection(CollectionModel collection) {
        this.collection = collection;
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
}