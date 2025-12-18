package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.CategoryBagEntity;
import ru.ifmo.soa.uddi.model.entity.EntityType;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryBagRepository extends JpaRepository<CategoryBagEntity, Long> {
  List<CategoryBagEntity> findByEntityTypeAndEntityKey(EntityType entityType, String entityKey);
  List<CategoryBagEntity> findByEntityTypeAndEntityKeyIn(EntityType entityType, Collection<String> entityKeys);
}
