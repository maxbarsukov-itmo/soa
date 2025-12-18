package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.OverviewDocEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface OverviewDocRepository extends JpaRepository<OverviewDocEntity, Long> {
  @Query("SELECT o FROM OverviewDocEntity o WHERE o.tModel.tModelKey = :tModelKey")
  List<OverviewDocEntity> findByTModel_TModelKey(@Param("tModelKey") String tModelKey);

  @Query("SELECT o FROM OverviewDocEntity o WHERE o.tModel.tModelKey IN :tModelKeys")
  List<OverviewDocEntity> findByTModel_TModelKeyIn(@Param("tModelKeys") Collection<String> tModelKeys);
}
