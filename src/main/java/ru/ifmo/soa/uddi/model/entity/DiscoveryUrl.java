package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "discovery_url")
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
