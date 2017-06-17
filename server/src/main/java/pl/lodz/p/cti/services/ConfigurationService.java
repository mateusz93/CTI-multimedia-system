package pl.lodz.p.cti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.cti.dao.ConfigurationDAO;

@Service
public class ConfigurationService {

    private ConfigurationDAO configurationDAO;

    @Autowired
    ConfigurationService(ConfigurationDAO configurationDAO){
        this.configurationDAO = configurationDAO;
    }
}
