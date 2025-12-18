package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tmodel_instance_info")
public class TModelInstanceInfo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "binding_key", nullable = false)
  private BindingTemplate binding;

  @Column(name = "tmodel_key", nullable = false, length = 255)
  private String tModelKey;

  @Column(name = "instance_details", columnDefinition = "TEXT")
  private String instanceDetails;
}
