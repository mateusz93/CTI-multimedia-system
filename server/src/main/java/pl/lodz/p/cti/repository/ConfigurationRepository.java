package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.cti.models.ConfigurationModel;

@Transactional
public interface ConfigurationRepository extends JpaRepository<ConfigurationModel, Long> {
	ConfigurationModel findByName(String name);
}
