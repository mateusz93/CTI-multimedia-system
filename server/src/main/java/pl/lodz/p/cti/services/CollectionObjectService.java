package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.CollectionObjectModel;
import pl.lodz.p.cti.repository.CollectionObjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionObjectService {

    private final CollectionObjectRepository collectionObjectRepository;

    public List<CollectionObjectModel> save(List<CollectionObjectModel> collectionObjectModelList) {
        return collectionObjectRepository.save(collectionObjectModelList);
    }

    public List<CollectionObjectModel> findByCollectionId(Long collectionId) {
        return collectionObjectRepository.findByCollectionId(collectionId);
    }

    public void delete(List<CollectionObjectModel> collectionObjectModelList) {
        collectionObjectRepository.delete(collectionObjectModelList);
    }

    public List<CollectionObjectModel> findAll() {
        return collectionObjectRepository.findAll();
    }
}
