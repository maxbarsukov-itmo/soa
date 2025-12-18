package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "email", indexes = {
  @Index(name = "idx_email_contact", columnList = "contact_id")
})
public class EmailEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne private ContactEntity contact;
  @Column(name = "use_type") private String useType;
  @Column(name = "email_address", nullable = false) private String emailAddress;
}
