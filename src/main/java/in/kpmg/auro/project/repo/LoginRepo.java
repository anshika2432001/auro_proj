package in.kpmg.auro.project.repo;

import in.kpmg.auro.project.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends JpaRepository<UserLogin, Long> {

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM user_login_audit WHERE user_name= :username and password_hash = :password and status = 1")
    Integer checkPasswordHash(String username, String password);
}
