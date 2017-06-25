package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.TvRepository;
import pl.lodz.p.cti.models.TvModel;

import java.util.List;

@Service
public class TvService {
    private TvRepository tvRepository;

    @Autowired
    TvService(TvRepository tvRepository){
        this.tvRepository = tvRepository;
    }

    public TvModel findByIp(String remoteAddr) {
        return tvRepository.findByIp(remoteAddr);
    }

    public TvModel save(TvModel tvModel) {
        return tvRepository.save(tvModel);
    }

    public TvModel findByHash(String hash) {
        return tvRepository.findByHash(hash);
    }

    public List<TvModel> findAll() {
        return tvRepository.findAll();
    }

    public TvModel findOne(Long tvId) {
        return tvRepository.findOne(tvId);
    }
}
