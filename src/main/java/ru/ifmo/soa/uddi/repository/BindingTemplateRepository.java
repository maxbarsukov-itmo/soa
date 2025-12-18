package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BindingTemplateEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BindingTemplateRepository extends JpaRepository<BindingTemplateEntity, String> {

  Optional<BindingTemplateEntity> findByBindingKey(String bindingKey);

  List<BindingTemplateEntity> findByService_ServiceKey(String serviceKey);

  List<BindingTemplateEntity> findByService_ServiceKeyIn(Collection<String> serviceKeys);

  List<BindingTemplateEntity> findByService_BusinessEntity_Operator(String operator);

  @Query("SELECT b FROM BindingTemplateEntity b " +
    "WHERE b.bindingKey IN (" +
    "SELECT tmi.binding.bindingKey FROM TModelInstanceInfoEntity tmi " +
    "WHERE tmi.tModelKey = :tModelKey" +
    ")")
  List<BindingTemplateEntity> findByTModelKey(@Param("tModelKey") String tModelKey);}
