package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.CollectionObjectRepository;
import pl.lodz.p.cti.models.CollectionObjectModel;

import java.util.List;

@Service
public class CollectionObjectService {
    private CollectionObjectRepository collectionObjectRepository;

    @Autowired
    CollectionObjectService(CollectionObjectRepository collectionObjectRepository){
        this.collectionObjectRepository = collectionObjectRepository;
    }

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
