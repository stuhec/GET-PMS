package by.get.pms.dataaccess;

import by.get.pms.model.Project;
import by.get.pms.model.Task;
import by.get.pms.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Milos.Savic on 10/18/2016.
 */
public interface TaskRepository extends CrudRepository<Task, Long> {

	Task findTaskByName(@Param("name") String name);

	// @Query("select t from Task t where t.assignee = :assignee")
	List<Task> findTasksByAssignee(@Param("assignee") User assignee);

	// @Query("select t from Task t where t.project = :project")
	List<Task> findTasksByProject(@Param("project") Project project);

	@Query("select t from Task t where t.id in (:tids)")
	List<Task> findTasksByIds(@Param("tids") Set<Long> tids);

	@Query("select count(t) from Task t where t.name = :name")
	Integer taskExistsByName(@Param("name") String name);
}
