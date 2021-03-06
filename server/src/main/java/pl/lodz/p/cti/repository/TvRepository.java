package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.TvModel;

public interface TvRepository extends JpaRepository<TvModel, Long> {
    TvModel findByIp(String remoteAddr);
    TvModel findByHash(String hash);
}
