package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BusinessEntityEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessEntityRepository extends JpaRepository<BusinessEntityEntity, String> {

  Optional<BusinessEntityEntity> findByBusinessKey(String businessKey);

  List<BusinessEntityEntity> findByOperator(String operator);

  @Query("SELECT b FROM BusinessEntityEntity b WHERE b.name LIKE %:name%")
  List<BusinessEntityEntity> findByNameLike(@Param("name") String name);

  @Query("SELECT b FROM BusinessEntityEntity b " +
    "WHERE b.businessKey IN (" +
    "SELECT cb.entityKey FROM CategoryBagEntity cb " +
    "WHERE cb.entityType = ru.ifmo.soa.uddi.model.entity.EntityType.BUSINESS " +
    "AND cb.tModelKey = :tModelKey " +
    "AND cb.keyValue = :keyValue" +
    ")"
  )
  List<BusinessEntityEntity> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);

  @Query("SELECT b FROM BusinessEntityEntity b " +
    "WHERE b.businessKey IN (" +
    "SELECT cb.entityKey FROM IdentifierBagEntity cb " +
    "WHERE cb.entityType = ru.ifmo.soa.uddi.model.entity.EntityType.BUSINESS " +
    "AND cb.tModelKey = :tModelKey " +
    "AND cb.keyValue = :keyValue" +
    ")"
  )
  List<BusinessEntityEntity> findByIdentifier(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
