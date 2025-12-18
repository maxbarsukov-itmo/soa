package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.TModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface TModelRepository extends JpaRepository<TModel, String> {

  Optional<TModel> findByTModelKey(String tModelKey);

  @Query("SELECT t FROM TModel t WHERE t.name LIKE %:name%")
  List<TModel> findByNameLike(@Param("name") String name);

  @Query("SELECT t FROM TModel t " +
    "JOIN t.categoryBags cb " +
    "WHERE cb.tModelKey = :tModelKey AND cb.keyValue = :keyValue")
  List<TModel> findByCategory(@Param("tModelKey") String tModelKey, @Param("keyValue") String keyValue);
}
