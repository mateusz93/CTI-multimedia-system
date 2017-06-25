package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.ObjectRepository;
import pl.lodz.p.cti.models.ObjectModel;

import java.util.List;

@Service
public class ObjectService {
    private ObjectRepository objectRepository;

    @Autowired
    ObjectService(ObjectRepository objectRepository){
        this.objectRepository = objectRepository;
    }

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
