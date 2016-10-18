package by.get.pms.dto;

import by.get.pms.model.Task;
import by.get.pms.validation.Username;
import by.get.pms.validation.ValidRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Milos.Savic on 10/17/2016.
 */
public class UserDTO extends DTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String firstName;

	@NotNull
	@Size(min = 1, max = 255)
	private String lastName;

	@NotNull
	@Size(min = 1, max = 255)
	private String email;

	private List<Task> assignedTasks;

	@NotNull
	@Size(min = 1, max = 30)
	@Username
	private String username;

	@NotNull
	private LocalDateTime creationDate;

	private Boolean active;

	@ValidRole
	private String roleCode;

	public UserDTO(long id, String firstName, String lastName, String email, List<Task> assignedTasks, String username,
			LocalDateTime creationDate, Boolean active, String roleCode) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.assignedTasks = (assignedTasks != null) ? assignedTasks : new LinkedList<>();
		this.username = username;
		this.creationDate = creationDate;
		this.active = active;
		this.roleCode = roleCode;
	}

	@Override
	public String getBusinessIdentifier() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}