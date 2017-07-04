package pl.lodz.p.cti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.cti.models.ObjectModel;

import java.util.List;

public interface ObjectRepository extends JpaRepository<ObjectModel, Long> {
    ObjectModel findByName(String name);
    List<ObjectModel> findByIdNotIn(List<Long> objectIdUsedList);
}
