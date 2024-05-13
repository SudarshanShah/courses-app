package dev.ssh.courses.exceptions;

public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4573209702199558079L;

	public CourseNotFoundException(String message) {
		super(message);
	}
}
