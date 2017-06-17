package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.TvModel;

@Transactional
public interface TvDAO extends JpaRepository<TvModel, Long> {
    TvModel findByIp(String remoteAddr);
    TvModel findByHash(String hash);
}
