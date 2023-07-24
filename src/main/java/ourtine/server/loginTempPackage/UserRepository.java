package ourtine.server.loginTempPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import ourtine.server.jwt.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
