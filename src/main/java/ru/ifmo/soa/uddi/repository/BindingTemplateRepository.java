package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BindingTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public interface BindingTemplateRepository extends JpaRepository<BindingTemplate, String> {

  Optional<BindingTemplate> findByBindingKey(String bindingKey);

  List<BindingTemplate> findByService_ServiceKey(String serviceKey);

  @Query("SELECT b FROM BindingTemplate b " +
    "JOIN b.tModelInstances tmi " +
    "WHERE tmi.tModelKey = :tModelKey")
  List<BindingTemplate> findByTModelKey(@Param("tModelKey") String tModelKey);
}
