package pl.lodz.p.cti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.ConfigurationModel;

@Transactional
public interface ConfigurationDAO extends JpaRepository<ConfigurationModel, Long> {
	ConfigurationModel findByName(String name);
}
