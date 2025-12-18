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
public class BusinessEntity {
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

  @OneToMany(mappedBy = "businessEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DiscoveryUrl> discoveryUrls = new ArrayList<>();

  @OneToMany(mappedBy = "businessEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Contact> contacts = new ArrayList<>();

  @OneToMany(mappedBy = "businessEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CategoryBag> categoryBags = new ArrayList<>();

  @OneToMany(mappedBy = "businessEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<IdentifierBag> identifierBags = new ArrayList<>();

  @OneToMany(mappedBy = "businessEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BusinessService> services = new ArrayList<>();

  public BusinessEntity() {
    this.businessKey = "uuid:" + UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
