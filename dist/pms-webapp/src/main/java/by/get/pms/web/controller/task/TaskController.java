package by.get.pms.web.controller.task;

import by.get.pms.dto.*;
import by.get.pms.security.Application;
import by.get.pms.service.entitlement.EntitlementService;
import by.get.pms.service.project.ProjectFacade;
import by.get.pms.service.task.TaskFacade;
import by.get.pms.web.controller.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by milos on 23-Oct-16.
 */
@Controller
public class TaskController {

	@Autowired
	private ProjectFacade projectFacade;

	@Autowired
	private TaskFacade taskFacade;

	@Autowired
	private EntitlementService entitlementService;

	@RequestMapping(value = WebConstants.TASKS_URL + "/{project}", method = RequestMethod.GET)
	public ModelAndView getProjectTasks(@PathVariable("project") String project) {
		Long projectId = Long.valueOf(project);
		ProjectDTO projectDTO = projectFacade.getProject(projectId);

		ModelAndView modelAndView = new ModelAndView(WebConstants.TASKS_HTML_PATH);

		List<TaskDTO> projectTasks = retrieveProjectTasks(projectDTO, Application.getInstance().getUser());
		modelAndView.getModel().put("projectTasks", projectTasks);

		return modelAndView;
	}

	private List<TaskDTO> retrieveProjectTasks(ProjectDTO project, UserDTO user) {
		List<EntitlementDTO> entitlementsForTasksPermittedToUser = entitlementService
				.getEntitlementsForObjectTypePermittedToUser(user.getUsername(), ObjectType.TASK);
		Set<Long> taskIds = entitlementsForTasksPermittedToUser.parallelStream().map(EntitlementDTO::getObjectid)
				.collect(Collectors.toSet());
		List<TaskDTO> tasks = taskFacade.getTasksByIds(taskIds);

		return tasks.parallelStream().filter(taskDTO -> project.equals(taskDTO.getProjectDTO()))
				.collect(Collectors.toList());
	}
}
