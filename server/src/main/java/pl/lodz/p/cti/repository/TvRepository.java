package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.TvModel;

@Transactional
public interface TvRepository extends JpaRepository<TvModel, Long> {
    TvModel findByIp(String remoteAddr);
    TvModel findByHash(String hash);
}
