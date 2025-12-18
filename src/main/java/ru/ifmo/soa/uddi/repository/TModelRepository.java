package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.TModelEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TModelRepository extends JpaRepository<TModelEntity, String> {

  @Query("SELECT t FROM TModelEntity t WHERE t.tModelKey = :tModelKey")
  Optional<TModelEntity> findByTModelKey(@Param("tModelKey") String tModelKey);

  List<TModelEntity> findByOperator(String operator);

  @Query("SELECT t FROM TModelEntity t WHERE t.name LIKE %:name%")
  List<TModelEntity> findByNameLike(@Param("name") String name);

  @Query("SELECT t FROM TModelEntity t " +
    "WHERE t.tModelKey IN (" +
    "SELECT cb.entityKey FROM CategoryBagEntity cb " +
    "WHERE cb.entityType = ru.ifmo.soa.uddi.model.entity.EntityType.TMODEL " +
    "AND cb.tModelKey = :tModelKey " +
    "AND cb.keyValue = :keyValue" +
    ")"
  )
  List<TModelEntity> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
