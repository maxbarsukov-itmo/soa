package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.DiscoveryUrlEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface DiscoveryUrlRepository extends JpaRepository<DiscoveryUrlEntity, Long> {
  List<DiscoveryUrlEntity> findByBusinessEntity_BusinessKey(String businessKey);
  List<DiscoveryUrlEntity> findByBusinessEntity_BusinessKeyIn(Collection<String> businessKeys);
}
