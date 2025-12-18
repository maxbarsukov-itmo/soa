package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contact")
public class Contact {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "business_key", nullable = false)
  private BusinessEntity businessEntity;

  @Column(name = "use_type", length = 255)
  private String useType;

  @Column(name = "person_name", length = 255)
  private String personName;

  @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Phone> phones = new ArrayList<>();

  @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Email> emails = new ArrayList<>();
}
