package pl.lodz.p.cti.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tvs")
public class TvModel implements Comparable<TvModel> {

    public static final String PROPERTY_HASH = "hash";
    public static final String PROPERTY_IP = "ip";

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique=true)
    private String ip;

    @NotNull
    @Column(unique=true)
    private String name;

    @NotNull
    @Column(unique=true)
    private String hash;

    public TvModel(){}

    public TvModel(String ip, String name, String hash){
        this.ip = ip;
        this.name = name;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public int compareTo(TvModel o) {
        return (int)(id-o.getId());
    }
}
