package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.repository.ConfigurationRepository;
import pl.lodz.p.cti.models.ConfigurationModel;

@Service
public class ConfigurationService {

    private ConfigurationRepository configurationRepository;

    @Autowired
    ConfigurationService(ConfigurationRepository configurationRepository){
        this.configurationRepository = configurationRepository;
    }

    public ConfigurationModel findByName(String name) {
        return configurationRepository.findByName(name);
    }
    public ConfigurationModel save(ConfigurationModel configurationModel) {
        return configurationRepository.save(configurationModel);
    }
}
