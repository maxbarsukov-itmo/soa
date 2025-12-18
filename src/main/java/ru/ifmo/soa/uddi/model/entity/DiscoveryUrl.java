package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "discovery_url", indexes = {
  @Index(name = "idx_discovery_business_key", columnList = "business_key"),
  @Index(name = "idx_discovery_url", columnList = "url")
})
public class DiscoveryUrl {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "business_key", nullable = false)
  private BusinessEntity businessEntity;

  @Column(name = "use_type", length = 255)
  private String useType;

  @Column(name = "url", nullable = false, length = 500)
  private String url;
}
