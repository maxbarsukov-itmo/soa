package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "business_entity", indexes = {
  @Index(name = "idx_business_name", columnList = "name"),
  @Index(name = "idx_business_operator", columnList = "operator")
})
public class BusinessEntityEntity {
  @Id
  @Column(name = "business_key", nullable = false, unique = true)
  private String businessKey;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "operator", nullable = false, length = 255)
  private String operator = "localhost";

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Transient
  private List<DiscoveryUrlEntity> discoveryUrls = new ArrayList<>();

  @Transient
  private List<ContactEntity> contacts = new ArrayList<>();

  @Transient
  private List<CategoryBagEntity> categoryBags = new ArrayList<>();

  @Transient
  private List<IdentifierBagEntity> identifierBags = new ArrayList<>();

  @Transient
  private List<BusinessServiceEntity> services = new ArrayList<>();

  public BusinessEntityEntity() {
    this.businessKey = "uuid:" + UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
