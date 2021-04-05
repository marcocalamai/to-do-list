package org.marco.calamai.todolist.model;

import java.time.LocalDate;

public class ToDo {
	private String user;
	private String title;
	private String description;
	private boolean done;
	private LocalDate deadline;
	
	public ToDo(String user, String title, String description, LocalDate deadline) {
		this.user = user;
		this.title = title;
		this.description = description;
		this.done = false;
		this.deadline = deadline;
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
		return "ToDo [title=" + title + ", description=" + description + ", done=" + done + ", deadline=" + deadline
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (done ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (done != other.done)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
