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
@Table(name = "tmodel", indexes = {
  @Index(name = "idx_tmodel_name", columnList = "name"),
  @Index(name = "idx_tmodel_operator", columnList = "operator")
})
public class TModel {
  @Id
  @Column(name = "tmodel_key", nullable = false, unique = true)
  private String tModelKey;

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

  @OneToMany(mappedBy = "tModel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CategoryBag> categoryBags = new ArrayList<>();

  @OneToMany(mappedBy = "tModel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OverviewDoc> overviewDocs = new ArrayList<>();

  public TModel() {
    this.tModelKey = "uuid:" + UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
