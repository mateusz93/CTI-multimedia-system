package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.CollectionDAO;
import pl.lodz.p.cti.models.CollectionModel;

import java.util.List;

@Service
public class CollectionService {
    private CollectionDAO collectionDAO;

    @Autowired
    CollectionService(CollectionDAO collectionDAO){
        this.collectionDAO = collectionDAO;
    }

    public CollectionModel findOne(Long collectionId) {
        return collectionDAO.findOne(collectionId);
    }

    public List<CollectionModel> findAll() {
        return collectionDAO.findAll();
    }

    public CollectionModel save(CollectionModel collectionModel) {
        return collectionDAO.save(collectionModel);
    }

    public void delete(Long collectionId) {
        collectionDAO.delete(collectionId);
    }
}
