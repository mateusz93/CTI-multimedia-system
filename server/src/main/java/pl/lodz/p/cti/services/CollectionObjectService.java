package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.CollectionObjectDAO;
import pl.lodz.p.cti.models.CollectionObjectModel;

import java.util.List;

@Service
public class CollectionObjectService {
    private CollectionObjectDAO collectionObjectDAO;

    @Autowired
    CollectionObjectService(CollectionObjectDAO collectionObjectDAO){
        this.collectionObjectDAO = collectionObjectDAO;
    }

    public List<CollectionObjectModel> save(List<CollectionObjectModel> collectionObjectModelList) {
        return collectionObjectDAO.save(collectionObjectModelList);
    }

    public List<CollectionObjectModel> findByCollectionId(Long collectionId) {
        return collectionObjectDAO.findByCollectionId(collectionId);
    }

    public void delete(List<CollectionObjectModel> collectionObjectModelList) {
        collectionObjectDAO.delete(collectionObjectModelList);
    }
}
