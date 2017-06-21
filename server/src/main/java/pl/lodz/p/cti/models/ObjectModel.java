package pl.lodz.p.cti.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="objects")
public class ObjectModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique=true)
    private String name;

    @NotNull
    private String contentType;

    @Lob
    @Column(name = "object", length = Integer.MAX_VALUE)
    private byte[] image;

    public ObjectModel(){}

    public ObjectModel(String name, String contentType, byte[] image) {
        this.name = name;
        this.contentType = contentType;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
