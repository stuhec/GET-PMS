package by.get.pms.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * Created by milos on 14-Oct-16.
 */

@Entity
@Table(name = "task", uniqueConstraints = {
        @UniqueConstraint(name = "AK_PROJECT_NAME", columnNames = {"PROJECT", "NAME"})})
public class Task extends PersistentEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "task_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.NEW;

    @Min(0)
    @Max(100)
    @Column(name = "progress", nullable = false)
    private int progress;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project")
    private Project project;

    public Task() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Task{" + "id= " + getId() + '\'' + "name= '" + name + '\'' + ", taskStatus= " + taskStatus
                + ", progress= " + progress + ", deadline= " + deadline + ", description= '" + description + '\''
                + ", assignee= " + assignee + ", project= " + project + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Task))
            return false;
        if (!super.equals(o))
            return false;
        Task task = (Task) o;
        return Objects.equal(getName(), task.getName()) && Objects.equal(getProject(), task.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getProject());
    }
}
