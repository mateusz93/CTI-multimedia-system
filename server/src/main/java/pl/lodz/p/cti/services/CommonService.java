package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.messages.ForceRefreshMessage;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.repository.TvRepository;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class CommonService {

    private final SimpMessagingTemplate template;
    private final TvRepository tvRepository;

    void forceTvRefreshById(Long tvId) {
        log.info("Refreshing tv: {}", tvId);
        template.convertAndSend("/topic/forceTvRefreshById", new ForceRefreshMessage(tvId));
    }

    void forceTvRefreshAll() {
        log.info("Refreshing all tvs");
        for (TvModel tv : tvRepository.findAll()) {
            forceTvRefreshById(tv.getId());
        }
    }
}
