package dev.ssh.courses.services;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import dev.ssh.courses.dtos.ResponseDto;
import dev.ssh.courses.exceptions.CourseNotFoundException;
import dev.ssh.courses.models.Course;
import dev.ssh.courses.repositories.CourseRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;
	
	private final MongoTemplate mongoTemplate;

	public CourseService(CourseRepository courseRepository, MongoTemplate mongoTemplate) {
		this.courseRepository = courseRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public List<Course> getAll() {
		return courseRepository.findAll();
	}

	public Course addCourse(Course course) {
		return courseRepository.save(course);
	}
	
	public Course getCourseById(String courseId) {
		return courseRepository.findById(courseId)
				               .orElseThrow(() -> new CourseNotFoundException("The course with id " + courseId  + " not found!"));
	}
	
	public Course updateCourse(String courseId, Course course) {
		System.out.println("Course in service -> " + course);
		Course _course = courseRepository.findById(courseId)
										 .orElseThrow(() -> new CourseNotFoundException("The course with id " + courseId  + " not found!"));
		
		Course c = new Course(_course.courseId(), 
							   course.courseName(), 
							   course.instructor(), 
							   course.courseFee(),	
							   course.duration(),
							   course.isCourseLive());
		return courseRepository.save(c);
	}
	
	public Map<String, String> deleteCourse(String courseId) {
		Course course = courseRepository.findById(courseId)
				 .orElseThrow(() -> new CourseNotFoundException("The course with id " + courseId  + " not found!"));
		courseRepository.delete(course);
		return Map.of("message", "Course with ID:" + courseId + " deleted successfully");
	}
	
	public List<Course> findCoursesByInstructorAndCourseFee(String instructor, Double courseFee) {
		return courseRepository.findByInstructorAndCourseFee(instructor, courseFee);
	}
	
	public List<Course> findCoursesByInstructorAndCourseFee() {
		MatchOperation matchOperation1 = match(new Criteria("instructor").is("Sudarshan Shah"));
		MatchOperation matchOperation2 = match(new Criteria("courseFee").gt(3000.0));
		
		Aggregation aggregation = newAggregation(matchOperation1, matchOperation2);
		AggregationResults<Course> results = mongoTemplate.aggregate(aggregation, "courses", Course.class);
		
		return results.getMappedResults();
	}
	
	public ResponseDto matchAndGroupAggregation(String instructor) {
		MatchOperation matchOperation = match(new Criteria("instructor").is(instructor));
		GroupOperation groupOperation = group("instructor")
											.count().as("totalCourses")
											.avg("courseFee").as("averageCourseFee");
		
		Aggregation aggregation = newAggregation(matchOperation, groupOperation);
		AggregationResults<ResponseDto> results = mongoTemplate.aggregate(aggregation,
														  "courses", 
														      ResponseDto.class);
		
		return results.getUniqueMappedResult();
	}
	
	
}


