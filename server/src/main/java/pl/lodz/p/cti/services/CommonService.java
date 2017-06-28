package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.messages.ForceRefreshMessage;

/**
 * @author Mateusz Wieczorek on 28.06.2017.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class CommonService {

    private final SimpMessagingTemplate template;

    void forceRefresh(Long tvId) {
        log.info("ForceRefresh!");
        template.convertAndSend("/topic/forceRefresh", new ForceRefreshMessage(tvId));
    }
}
