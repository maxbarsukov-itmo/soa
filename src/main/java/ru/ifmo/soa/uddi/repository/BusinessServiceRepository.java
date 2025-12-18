package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BusinessService;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, String> {

  Optional<BusinessService> findByServiceKey(String serviceKey);

  List<BusinessService> findByBusinessEntity_BusinessKey(String businessKey);

  @Query("SELECT s FROM BusinessService s WHERE s.name LIKE %:name%")
  List<BusinessService> findByNameLike(@Param("name") String name);

  @Query("SELECT s FROM BusinessService s " +
    "JOIN s.categoryBags cb " +
    "WHERE cb.tModelKey = :tModelKey AND cb.keyValue = :keyValue")
  List<BusinessService> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
