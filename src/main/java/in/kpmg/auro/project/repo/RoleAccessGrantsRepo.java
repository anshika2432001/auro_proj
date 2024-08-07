package in.kpmg.auro.project.repo;

import in.kpmg.auro.project.model.RoleAccessGrantsMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAccessGrantsRepo extends JpaRepository<RoleAccessGrantsMst, String> {

//    List<RoleAccessGrantsMst> findByRoleId(Integer roleId);

}
