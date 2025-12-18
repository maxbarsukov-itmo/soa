package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "identifier_bag")
public class IdentifierBag {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "entity_type", nullable = false)
  private String entityType;

  @Column(name = "entity_key", nullable = false)
  private String entityKey;

  @Column(name = "tmodel_key", nullable = false, length = 255)
  private String tModelKey;

  @Column(name = "key_name", length = 255)
  private String keyName;

  @Column(name = "key_value", length = 255)
  private String keyValue;
}
