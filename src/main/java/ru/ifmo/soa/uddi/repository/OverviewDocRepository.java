package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.OverviewDoc;

import java.util.List;

@Repository
public interface OverviewDocRepository extends JpaRepository<OverviewDoc, Long> {
  List<OverviewDoc> findByTModel_TModelKey(String tModelKey);
}
