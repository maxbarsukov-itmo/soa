package ru.ifmo.soa.uddi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.soa.uddi.model.entity.EmailEntity;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {}
