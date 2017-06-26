package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.CollectionModel;
import pl.lodz.p.cti.repository.CollectionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

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
