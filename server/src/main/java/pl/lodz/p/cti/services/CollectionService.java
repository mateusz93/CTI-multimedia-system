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

    public List<CollectionModel> findByCollectionId(Long collectionId) {
        return collectionDAO.findByCollectionId(collectionId);
    }
}
