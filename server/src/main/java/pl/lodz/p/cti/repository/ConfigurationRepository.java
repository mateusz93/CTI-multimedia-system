package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.ConfigurationModel;

public interface ConfigurationRepository extends JpaRepository<ConfigurationModel, Long> {
	ConfigurationModel findByName(String name);
}
