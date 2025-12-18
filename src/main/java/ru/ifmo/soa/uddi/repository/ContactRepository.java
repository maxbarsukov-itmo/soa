package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.ContactEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
  List<ContactEntity> findByBusinessEntity_BusinessKey(String businessKey);
  List<ContactEntity> findByBusinessEntity_BusinessKeyIn(Collection<String> businessKeys);
}
