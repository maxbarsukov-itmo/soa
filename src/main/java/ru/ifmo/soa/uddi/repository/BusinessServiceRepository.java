package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BusinessServiceEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessServiceEntity, String> {

  Optional<BusinessServiceEntity> findByServiceKey(String serviceKey);

  List<BusinessServiceEntity> findByBusinessEntity_BusinessKey(String businessKey);

  List<BusinessServiceEntity> findByBusinessEntity_Operator(String operator);

  List<BusinessServiceEntity> findByBusinessEntity_BusinessKeyIn(Collection<String> businessKeys);

  @Query("SELECT s FROM BusinessServiceEntity s WHERE s.name LIKE %:name%")
  List<BusinessServiceEntity> findByNameLike(@Param("name") String name);

  @Query("SELECT s FROM BusinessServiceEntity s " +
    "WHERE s.serviceKey IN (" +
    "SELECT cb.entityKey FROM CategoryBagEntity cb " +
    "WHERE cb.entityType = ru.ifmo.soa.uddi.model.entity.EntityType.SERVICE " +
    "AND cb.tModelKey = :tModelKey " +
    "AND cb.keyValue = :keyValue" +
    ")"
  )
  List<BusinessServiceEntity> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
