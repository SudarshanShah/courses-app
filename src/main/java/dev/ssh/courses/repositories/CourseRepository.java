package dev.ssh.courses.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.ssh.courses.models.Course;

public interface CourseRepository extends MongoRepository<Course, String> {

	List<Course> findByInstructor(String instructor);

	@Aggregation(pipeline = {" {$match :  {instructor :  ?0}}", "{$match: {courseFee: {$gt: ?1}}} "})
	List<Course> findByInstructorAndCourseFee(String instructor, Double courseFee);
}
