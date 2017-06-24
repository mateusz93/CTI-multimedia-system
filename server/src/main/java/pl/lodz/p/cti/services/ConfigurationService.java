package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.ConfigurationDAO;
import pl.lodz.p.cti.models.ConfigurationModel;

@Service
public class ConfigurationService {

    private ConfigurationDAO configurationDAO;

    @Autowired
    ConfigurationService(ConfigurationDAO configurationDAO){
        this.configurationDAO = configurationDAO;
    }

    public ConfigurationModel findByName(String name) {
        return configurationDAO.findByName(name);
    }
    public ConfigurationModel save(ConfigurationModel configurationModel) {
        return configurationDAO.save(configurationModel);
    }
}
