package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.CollectionRepository;
import pl.lodz.p.cti.models.CollectionModel;

import java.util.List;

@Service
public class CollectionService {
    private CollectionRepository collectionRepository;

    @Autowired
    CollectionService(CollectionRepository collectionRepository){
        this.collectionRepository = collectionRepository;
    }

    public CollectionModel findOne(Long collectionId) {
        return collectionRepository.findOne(collectionId);
    }

    public List<CollectionModel> findAll() {
        return collectionRepository.findAll();
    }

    public CollectionModel save(CollectionModel collectionModel) {
        return collectionRepository.save(collectionModel);
    }

    public void delete(Long collectionId) {
        collectionRepository.delete(collectionId);
    }

    public List<CollectionModel> findByIdNotIn(List<Long> collectionIdUsedList) {
        return collectionRepository.findByIdNotIn(collectionIdUsedList);
    }
}
