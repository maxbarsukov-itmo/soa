package ru.ifmo.soa.uddi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "phone")
public class Phone {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne private Contact contact;
  @Column(name = "use_type") private String useType;
  @Column(name = "phone_number", nullable = false) private String phoneNumber;
}
