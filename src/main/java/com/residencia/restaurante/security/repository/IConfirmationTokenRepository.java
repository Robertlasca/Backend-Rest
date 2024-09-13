package com.residencia.restaurante.security.repository;

import com.residencia.restaurante.security.model.ConfirmationToken;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
}
