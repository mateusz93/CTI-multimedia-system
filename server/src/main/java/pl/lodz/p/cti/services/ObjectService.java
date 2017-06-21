package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.ObjectDAO;
import pl.lodz.p.cti.models.ObjectModel;

import java.util.List;

@Service
public class ObjectService {
    private ObjectDAO objectDAO;

    @Autowired
    ObjectService(ObjectDAO objectDAO){
        this.objectDAO = objectDAO;
    }

    public List<ObjectModel> findAll() {
        return objectDAO.findAll();
    }

    public ObjectModel save(ObjectModel objectModel) {
        return objectDAO.save(objectModel);
    }

    public ObjectModel findOne(Long objectId) {
        return objectDAO.findOne(objectId);
    }

    public ObjectModel findByName(String name) {
        return objectDAO.findByName(name);
    }

    public void delete(Long objectId) {
        objectDAO.delete(objectId);
    }

    public List<ObjectModel> findByIdNotIn(List<Long> objectIdUsedList) {
        return objectDAO.findByIdNotIn(objectIdUsedList);
    }
}
