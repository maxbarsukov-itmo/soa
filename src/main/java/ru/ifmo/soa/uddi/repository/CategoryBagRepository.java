package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.CategoryBag;
import ru.ifmo.soa.uddi.model.entity.EntityType;

import java.util.List;

@Repository
public interface CategoryBagRepository extends JpaRepository<CategoryBag, Long> {
  List<CategoryBag> findByEntityTypeAndEntityKey(EntityType entityType, String entityKey);
}
