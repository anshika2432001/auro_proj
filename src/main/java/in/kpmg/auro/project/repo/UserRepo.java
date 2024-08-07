package in.kpmg.auro.project.repo;

import in.kpmg.auro.project.model.UserMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserMst, Integer> {


    @Query("SELECT u FROM UserMst u WHERE LOWER(u.email)=LOWER(:username) ")
    UserMst getUserDataByUserName(String username);

    Optional<UserMst> findByEmail(String email);
}
