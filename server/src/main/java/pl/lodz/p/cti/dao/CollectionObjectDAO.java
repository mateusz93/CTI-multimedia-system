package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.CollectionObjectModel;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CollectionObjectDAO extends JpaRepository<CollectionObjectModel, Long> {
    List<CollectionObjectModel> findByCollectionId(Long collectionId);
}
