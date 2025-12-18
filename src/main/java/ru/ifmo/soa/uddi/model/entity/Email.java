package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "email")
public class Email {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne private Contact contact;
  @Column(name = "use_type") private String useType;
  @Column(name = "email_address", nullable = false) private String emailAddress;
}
