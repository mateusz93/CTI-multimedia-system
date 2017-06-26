package pl.lodz.p.cti.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tvs")
public class TvModel implements Comparable<TvModel> {

    public static final String PROPERTY_HASH = "hash";

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String ip;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String hash;

    @Override
    public int compareTo(TvModel o) {
        return (int) (id - o.getId());
    }
}
