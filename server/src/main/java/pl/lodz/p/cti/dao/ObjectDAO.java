package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.ObjectModel;

@Transactional
public interface ObjectDAO extends JpaRepository<ObjectModel, Long> {
    ObjectModel findByName(String name);
}
