package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tmodel_instance_info", indexes = {
  @Index(name = "idx_tmodelinst_binding_key", columnList = "binding_key"),
  @Index(name = "idx_tmodelinst_tmodel_key", columnList = "tmodel_key")
})
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
