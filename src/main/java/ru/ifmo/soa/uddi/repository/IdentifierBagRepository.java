package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.EntityType;
import ru.ifmo.soa.uddi.model.entity.IdentifierBag;

import java.util.List;

@Repository
public interface IdentifierBagRepository extends JpaRepository<IdentifierBag, Long> {
  List<IdentifierBag> findByEntityTypeAndEntityKey(EntityType entityType, String entityKey);
}
