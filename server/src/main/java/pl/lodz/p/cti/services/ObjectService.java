package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.ObjectDAO;

@Service
public class ObjectService {
    private ObjectDAO objectDAO;

    @Autowired
    ObjectService(ObjectDAO objectDAO){
        this.objectDAO = objectDAO;
    }
}
