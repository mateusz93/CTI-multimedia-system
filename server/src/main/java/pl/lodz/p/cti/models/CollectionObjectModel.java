package pl.lodz.p.cti.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"collection", "objectModel"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collection_objects")
public class CollectionObjectModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionModel collection;

    @NotNull
    private Long orderNumber;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectModel objectModel;

}