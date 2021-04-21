package org.marco.calamai.todolist.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ToDo {
	@Id
	private BigInteger id;
	private String user;
	private String title;
	private String description;
	private boolean done;
	private LocalDate deadline;
	
	public ToDo() {
		this.done = false;
	}
	
	public ToDo(String user, String title, String description, LocalDate deadline) {
		this.user = user;
		this.title = title;
		this.description = description;
		this.done = false;
		this.deadline = deadline;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	
	@Override
	public String toString() {
		return "ToDo [id=" + id + ", user=" + user + ", title=" + title + ", description=" + description + ", done="
				+ done + ", deadline=" + deadline + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (done ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToDo other = (ToDo) obj;
		return Objects.equals(id, other.id) && 
				Objects.equals(user, other.user) &&
				Objects.equals(title, other.title) && 
				Objects.equals(description, other.description) &&
				done == other.done &&
				Objects.equals(deadline, other.deadline);
	}

}
