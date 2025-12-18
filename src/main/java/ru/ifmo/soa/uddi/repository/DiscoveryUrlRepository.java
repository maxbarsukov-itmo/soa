package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.DiscoveryUrl;

import java.util.List;

@Repository
public interface DiscoveryUrlRepository extends JpaRepository<DiscoveryUrl, Long> {
  List<DiscoveryUrl> findByBusinessEntity_BusinessKey(String businessKey);
}
