package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contact", indexes = {
  @Index(name = "idx_contact_business_key", columnList = "business_key")
})
public class ContactEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "business_key", nullable = false)
  private BusinessEntityEntity businessEntity;

  @Column(name = "use_type", length = 255)
  private String useType;

  @Column(name = "person_name", length = 255)
  private String personName;

  @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PhoneEntity> phones = new ArrayList<>();

  @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmailEntity> emails = new ArrayList<>();
}
