package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.ObjectModel;
import pl.lodz.p.cti.repository.ObjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final ObjectRepository objectRepository;

    public List<ObjectModel> findAll() {
        return objectRepository.findAll();
    }

    public ObjectModel save(ObjectModel objectModel) {
        return objectRepository.save(objectModel);
    }

    public ObjectModel findOne(Long objectId) {
        return objectRepository.findOne(objectId);
    }

    public ObjectModel findByName(String name) {
        return objectRepository.findByName(name);
    }

    public void delete(Long objectId) {
        objectRepository.delete(objectId);
    }

    public List<ObjectModel> findByIdNotIn(List<Long> objectIdUsedList) {
        return objectRepository.findByIdNotIn(objectIdUsedList);
    }
}
