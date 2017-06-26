package pl.lodz.p.cti.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForceRefreshMessage {

    private Long tvId;

}
