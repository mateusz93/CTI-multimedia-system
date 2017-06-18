package pl.lodz.p.cti.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="collections")
public class CollectionModel{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "collection", cascade={CascadeType.ALL}, orphanRemoval = false)
    private Set<CollectionObjectModel> collectionObjects;

    public Set<CollectionObjectModel> getCollectionObjects() {
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
