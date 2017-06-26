package pl.lodz.p.cti.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.models.ConfigurationModel;
import pl.lodz.p.cti.repository.ConfigurationRepository;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationModel findByName(String name) {
        return configurationRepository.findByName(name);
    }

    public ConfigurationModel save(ConfigurationModel configurationModel) {
        return configurationRepository.save(configurationModel);
    }
}
