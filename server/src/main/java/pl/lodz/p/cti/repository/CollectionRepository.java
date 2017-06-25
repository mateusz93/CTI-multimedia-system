package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.CollectionModel;

import java.util.List;

@Transactional
public interface CollectionRepository extends JpaRepository<CollectionModel, Long> {
    List<CollectionModel> findByIdNotIn(List<Long> collectionIdUsedList);
}
