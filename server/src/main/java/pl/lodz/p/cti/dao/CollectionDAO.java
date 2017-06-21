package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.CollectionModel;

import java.util.List;

@Transactional
public interface CollectionDAO extends JpaRepository<CollectionModel, Long> {
    List<CollectionModel> findByIdNotIn(List<Long> collectionIdUsedList);
}
