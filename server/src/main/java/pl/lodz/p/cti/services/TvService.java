package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.TvModel;
import pl.lodz.p.cti.repository.TvRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TvService {

    private final TvRepository tvRepository;

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
