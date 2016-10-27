package by.get.pms.dataaccess;

import by.get.pms.model.Project;
import by.get.pms.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Milos.Savic on 10/18/2016.
 */
public interface ProjectRepository extends CrudRepository<Project, Long>{

	//@Query("select p from Project p where p.code = :projectCode")
	public Project findProjectByCode(@Param("projectCode") String projectCode);

	//@Query("select p from Project p where p.projectManager = :projectManager")
	public List<Project> findProjectsByProjectManager(@Param("projectManager") User projectManager);

	@Query("select p from Project p where p.id in (:projectCode)")
	List<Project> findProjectsByIds(Set<Long> projectIds);
}
