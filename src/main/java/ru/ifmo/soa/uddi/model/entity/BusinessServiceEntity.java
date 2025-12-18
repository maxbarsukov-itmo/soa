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
@Table(name = "business_service", indexes = {
  @Index(name = "idx_service_name", columnList = "name"),
  @Index(name = "idx_service_business_key", columnList = "business_key")
})
public class BusinessServiceEntity {
  @Id
  @Column(name = "service_key", nullable = false, unique = true)
  private String serviceKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "business_key", nullable = false)
  private BusinessEntityEntity businessEntity;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Transient
  private List<CategoryBagEntity> categoryBags = new ArrayList<>();

  @Transient
  private List<BindingTemplateEntity> bindings = new ArrayList<>();

  public BusinessServiceEntity() {
    this.serviceKey = "uuid:" + UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
