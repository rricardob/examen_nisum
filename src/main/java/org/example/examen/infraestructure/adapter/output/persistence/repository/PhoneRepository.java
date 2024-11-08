package org.example.examen.infraestructure.adapter.output.persistence.repository;

import org.example.examen.infraestructure.adapter.output.persistence.entity.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, UUID> {
}
