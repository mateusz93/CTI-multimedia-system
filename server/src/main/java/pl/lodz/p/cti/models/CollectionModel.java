package pl.lodz.p.cti.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="collections")
public class CollectionModel{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "collection", orphanRemoval = true)
    private List<CollectionObjectModel> collectionObjects;

    public List<CollectionObjectModel> getCollectionObjects() {
        return collectionObjects;
    }

    public CollectionModel(){}

    public CollectionModel(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
