package by.get.pms.web.controller.task;

import by.get.pms.acl.TaskACL;
import by.get.pms.dtos.TaskDTO;
import by.get.pms.dtos.UserRole;
import by.get.pms.exception.ApplicationException;
import by.get.pms.facade.task.TaskFacade;
import by.get.pms.security.Application;
import by.get.pms.web.controller.WebConstants;
import by.get.pms.web.response.Response;
import by.get.pms.web.response.ResponseBuilder;
import by.get.pms.web.response.ResponseBuilderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Milos.Savic on 10/24/2016.
 */
@RestController
@RequestMapping(value = WebConstants.REST_API_URL)
public class TaskRestController {

    @Autowired
    private ResponseBuilderFactoryBean responseBuilder;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskACL taskACL;

    @RequestMapping(value = WebConstants.CREATE_TASK_URL, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_PROJECT_MANAGER') and #taskParams.getProject().getProjectManager().equals(T(by.get.pms.security.Application).getInstance().getUser()))")
    public Response createTask(@Validated TaskDTO taskParams, BindingResult errors) {
        ResponseBuilder builder = responseBuilder.instance();
        if (errors.hasErrors()) {
            return builder.addErrors(errors).build();
        }

        try {
            TaskDTO taskDtoNew = taskFacade.createTask(taskParams);

            taskACL.createACL(taskDtoNew);

            return builder.indicateSuccess()
                    .addSuccessMessage("tasks.createTask.successfully.added", taskDtoNew.getId())
                    .addObject("task", taskDtoNew).build();
        } catch (ApplicationException ae) {
            return builder.addErrorMessage(ae.getMessage(), ae.getParams()).build();
        }
    }

    @RequestMapping(value = WebConstants.UPDATE_TASK_URL, method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#newTaskParams, 'write')")
    public Response updateTask(@Validated TaskDTO newTaskParams, BindingResult errors) {
        ResponseBuilder builder = responseBuilder.instance();
        if (errors.hasErrors()) {
            return builder.addErrors(errors).build();
        }

        TaskDTO oldTask = taskFacade.getTask(newTaskParams.getId());

        UserRole userRole = Application.getInstance().getCurrentRole();
        switch (userRole) {
            case ROLE_ADMIN:
                return updateTaskByAdmin(oldTask, newTaskParams, builder);
            case ROLE_PROJECT_MANAGER:
                return updateTaskByProjectManager(oldTask, newTaskParams, builder);
            case ROLE_DEV:
                return updateTaskByDev(oldTask, newTaskParams, builder);
            default:
                throw new RuntimeException(String.format("Not supported role: %s for task update!", userRole.name()));
        }
    }

    private Response updateTaskByAdmin(TaskDTO oldTask, TaskDTO newTaskParams, ResponseBuilder builder) {
        try {

            taskFacade.updateTask(newTaskParams);

            taskACL.updateACL(oldTask, newTaskParams);

            return builder.indicateSuccess()
                    .addSuccessMessage("tasks.updateTask.successfully.updated", newTaskParams.getName())
                    .addObject("task", newTaskParams).build();
        } catch (ApplicationException ae) {
            return builder.addErrorMessage(ae.getMessage(), ae.getParams()).build();
        }
    }

    private Response updateTaskByProjectManager(TaskDTO oldTask, TaskDTO newTaskParams, ResponseBuilder builder) {
        try {

            taskFacade.updateTaskByProjectManager(newTaskParams);

            taskACL.updateACL(oldTask, newTaskParams);

            return builder.indicateSuccess()
                    .addSuccessMessage("tasks.updateTask.successfully.updated", newTaskParams.getName())
                    .addObject("task", newTaskParams).build();
        } catch (ApplicationException ae) {
            return builder.addErrorMessage(ae.getMessage(), ae.getParams()).build();
        }
    }

    private Response updateTaskByDev(TaskDTO oldTask, TaskDTO newTaskParams, ResponseBuilder builder) {
        try {
            taskFacade.updateTaskByDeveloper(newTaskParams);

            taskACL.updateACL(oldTask, newTaskParams);

            return builder.indicateSuccess()
                    .addSuccessMessage("tasks.updateTask.successfully.updated", newTaskParams.getName())
                    .addObject("task", newTaskParams).build();
        } catch (ApplicationException ae) {
            return builder.addErrorMessage(ae.getMessage(), ae.getParams()).build();
        }
    }

    @RequestMapping(value = WebConstants.DELETE_TASK_URL + "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response removeTask(@PathVariable("id") Long id) {
        final ResponseBuilder builder = responseBuilder.instance();

        try {
            taskFacade.removeTask(id);

            taskACL.deleteACL(id);

            return builder.indicateSuccess().addSuccessMessage("tasks.removeTask.successfully.removed", id).build();
        } catch (ApplicationException e) {
            return builder.addErrorMessage(e.getMessage(), e.getParams()).build();
        }
    }
}
