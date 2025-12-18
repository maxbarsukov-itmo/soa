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
@Table(name = "binding_template", indexes = {
  @Index(name = "idx_binding_service_key", columnList = "service_key"),
  @Index(name = "idx_binding_access_point", columnList = "access_point")
})
public class BindingTemplate {
  @Id
  @Column(name = "binding_key", nullable = false, unique = true)
  private String bindingKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_key", nullable = false)
  private BusinessService service;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "access_point", nullable = false, length = 500)
  private String accessPoint;

  @Column(name = "hosting_redirector", length = 500)
  private String hostingRedirector;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "binding", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TModelInstanceInfo> tModelInstances = new ArrayList<>();

  public BindingTemplate() {
    this.bindingKey = "uuid:" + UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
