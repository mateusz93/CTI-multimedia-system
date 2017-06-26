package pl.lodz.p.cti.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "presentations")
public class PresentationModel implements Comparable<PresentationModel> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    private TvModel tv;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionModel collection;

    @NotNull
    private LocalTime startTime;

    @Override
    public int compareTo(PresentationModel o) {
        return tv.getId().equals(o.getTv().getId()) ?
                (startTime.isBefore(o.getStartTime()) ? -1 : (startTime.isAfter(o.getStartTime()) ? 1 : 0)) :
                (int) (tv.getId() - o.getTv().getId());
    }
}
