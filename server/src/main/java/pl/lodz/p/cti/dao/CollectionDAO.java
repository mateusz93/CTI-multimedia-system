package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.CollectionModel;

@Transactional
public interface CollectionDAO extends JpaRepository<CollectionModel, Long> {
}
