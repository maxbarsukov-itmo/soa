package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.BusinessEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, String> {

  Optional<BusinessEntity> findByBusinessKey(String businessKey);

  @Query("SELECT b FROM BusinessEntity b WHERE b.name LIKE %:name%")
  List<BusinessEntity> findByNameLike(@Param("name") String name);

  @Query("SELECT b FROM BusinessEntity b " +
    "JOIN b.categoryBags cb " +
    "WHERE cb.tModelKey = :tModelKey AND cb.keyValue = :keyValue")
  List<BusinessEntity> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);

  @Query("SELECT b FROM BusinessEntity b " +
    "JOIN b.identifierBags ib " +
    "WHERE ib.tModelKey = :tModelKey AND ib.keyValue = :keyValue")
  List<BusinessEntity> findByIdentifier(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
