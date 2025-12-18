package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category_bag", indexes = {
  @Index(name = "idx_category_entity", columnList = "entity_type, entity_key"),
  @Index(name = "idx_category_tmodel_key_value", columnList = "tmodel_key, key_value")
})
public class CategoryBagEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "entity_type", nullable = false)
  private EntityType entityType;

  @Column(name = "entity_key", nullable = false)
  private String entityKey;

  @Column(name = "tmodel_key", nullable = false, length = 255)
  private String tModelKey;

  @Column(name = "key_name", length = 255)
  private String keyName;

  @Column(name = "key_value", length = 255)
  private String keyValue;
}
