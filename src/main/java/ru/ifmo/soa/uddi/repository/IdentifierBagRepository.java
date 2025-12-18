package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.EntityType;
import ru.ifmo.soa.uddi.model.entity.IdentifierBagEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface IdentifierBagRepository extends JpaRepository<IdentifierBagEntity, Long> {
  List<IdentifierBagEntity> findByEntityTypeAndEntityKey(EntityType entityType, String entityKey);
  List<IdentifierBagEntity> findByEntityTypeAndEntityKeyIn(EntityType entityType, Collection<String> entityKeys);
}
