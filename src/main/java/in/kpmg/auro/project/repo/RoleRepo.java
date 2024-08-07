package in.kpmg.auro.project.repo;

import in.kpmg.auro.project.model.RoleMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<RoleMst, Integer> {



}
