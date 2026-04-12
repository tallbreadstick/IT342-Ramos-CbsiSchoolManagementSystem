package edu.cit.ramos.repository;

import edu.cit.ramos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import edu.cit.ramos.entity.AccountStatusType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySchoolId(String schoolId);
    boolean existsByEmail(String email);
    boolean existsBySchoolId(String schoolId);
    long countBySchoolIdStartingWith(String prefix);
    List<User> findByAccountStatus(AccountStatusType status);
}
