package com.sarangsvkm.portfolio_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    Optional<ContactRequest> findByEmail(String email);
}
