package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "overview_doc", indexes = {
  @Index(name = "idx_overview_tmodel_key", columnList = "tmodel_key")
})
public class OverviewDocEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tmodel_key", nullable = false)
  private TModelEntity tModel;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "overview_url", length = 500)
  private String overviewUrl;
}
