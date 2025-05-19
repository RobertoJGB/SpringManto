package br.com.orlands.manto.repositories;

import br.com.orlands.manto.domain.UserDomain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, Long> {
    Optional<UserDomain> findByEmail(String email);

}
