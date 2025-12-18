package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.TModelInstanceInfoEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface TModelInstanceInfoRepository extends JpaRepository<TModelInstanceInfoEntity, Long> {
  List<TModelInstanceInfoEntity> findByBinding_BindingKey(String bindingKey);
  List<TModelInstanceInfoEntity> findByBinding_BindingKeyIn(Collection<String> bindingKeys);
}
