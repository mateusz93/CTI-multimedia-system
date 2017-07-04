package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.CollectionObjectModel;

import java.util.List;

public interface CollectionObjectRepository extends JpaRepository<CollectionObjectModel, Long> {
    List<CollectionObjectModel> findByCollectionId(Long collectionId);
    List<CollectionObjectModel> findByObjectModelId(Long objectId);
}
