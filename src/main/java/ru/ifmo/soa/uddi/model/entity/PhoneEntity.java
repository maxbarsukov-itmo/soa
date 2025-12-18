package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "phone", indexes = {
  @Index(name = "idx_phone_contact", columnList = "contact_id")
})
public class PhoneEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne private ContactEntity contact;
  @Column(name = "use_type") private String useType;
  @Column(name = "phone_number", nullable = false) private String phoneNumber;
}
