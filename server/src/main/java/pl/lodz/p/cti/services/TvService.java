package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.TvDAO;
import pl.lodz.p.cti.models.TvModel;

@Service
public class TvService {
    private TvDAO tvDAO;

    @Autowired
    TvService(TvDAO tvDAO){
        this.tvDAO = tvDAO;
    }

    public TvModel findByIp(String remoteAddr) {
        return tvDAO.findByIp(remoteAddr);
    }

    public TvModel save(TvModel tvModel) {
        return tvDAO.save(tvModel);
    }

    public TvModel findByHash(String hash) {
        return tvDAO.findByHash(hash);
    }
}
