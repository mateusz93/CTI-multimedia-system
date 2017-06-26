package pl.lodz.p.cti.models;

import lombok.Data;

@Data
public class MapJSModel {

    private Long key;
    private String label;

    public MapJSModel(CollectionModel model) {
        super();
        this.key = model.getId();
        this.label = model.getName();
    }

}
