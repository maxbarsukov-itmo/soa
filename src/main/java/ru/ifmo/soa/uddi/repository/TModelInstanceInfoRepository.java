package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.TModelInstanceInfo;

import java.util.List;

@Repository
public interface TModelInstanceInfoRepository extends JpaRepository<TModelInstanceInfo, Long> {
  List<TModelInstanceInfo> findByBinding_BindingKey(String bindingKey);
}
