package in.kpmg.auro.project.repo;

import in.kpmg.auro.project.model.OtpDetailsMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDetailsRepo extends JpaRepository<OtpDetailsMst, String> {

}
