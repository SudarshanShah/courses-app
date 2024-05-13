package dev.ssh.courses.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ssh.courses.dtos.ResponseDto;
import dev.ssh.courses.models.Course;
import dev.ssh.courses.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/courses")
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@Operation(
			summary = "Get All Courses",
			description = "We will get the List of all courses",
			tags = {"courses", "get"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = List.class), 
				mediaType = "application/json")})
	})
	@GetMapping("/all")
	public ResponseEntity<List<Course>> getAllCourses() {
		return ResponseEntity.ok(courseService.getAll());
	}

	@Operation(
			summary = "Add a Course",
			description = "We add the course to DB by providing Course object in Request Body. We get the new created Course as response",
			tags = {"courses", "post"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "201", 
				content = {@Content(schema = @Schema(implementation = Course.class), 
				mediaType = "application/json")})
	})
	@PostMapping("/add")
	public ResponseEntity<Course> addCourse(@RequestBody Course course) {
		return new ResponseEntity<Course>(courseService.addCourse(course), HttpStatus.CREATED);
	}
	
	@Operation(
			summary = "Get a single Course by CourseId",
			description = "We fetch single course by Course Id. We get the Course object as response",
			tags = {"courses", "get"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = Course.class), 
				mediaType = "application/json")})
	})
	@GetMapping("/{courseId}")
	public ResponseEntity<Course> getCourse(@PathVariable String courseId) {
		return new ResponseEntity<Course>(courseService.getCourseById(courseId), HttpStatus.OK);
	}
	
	@Operation(
			summary = "Update existing Course data using CourseId and updated Course information",
			description = "We fetch course that we want to update by Course Id, and then provide the updated course data. "
					+ "We get updated Course object as response",
			tags = {"courses", "put"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = Course.class), 
				mediaType = "application/json")})
	})
	@PutMapping("/update/{courseId}")
	public ResponseEntity<Course> updateCourse(@PathVariable String courseId, @RequestBody Course course) {
		System.out.println("Course in controller -> " + course);
		return new ResponseEntity<Course>(courseService.updateCourse(courseId, course), HttpStatus.OK);
	}
	
	@Operation(
			summary = "Delete existing Course data using CourseId",
			description = "We fetch single course by Course Id, and delete the Course object. We get the String message as response",
			tags = {"courses", "delete"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = Course.class), 
				mediaType = "application/json")})
	})
	@DeleteMapping("/delete/{courseId}")
	public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable String courseId) {
		return ResponseEntity.ok(courseService.deleteCourse(courseId));
	}
	
	@Operation(
			summary = "Get List of Courses by instructor and Course Fee",
			description = "We fetch all courses with the given instructor. We get List of courses as response",
			tags = {"courses", "get"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = List.class), 
				mediaType = "application/json")})
	})
	@GetMapping("/instructor/{instructor}/courseFee/{courseFee}")
	public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable String instructor, @PathVariable Double courseFee) {
		return ResponseEntity.ok(courseService.findCoursesByInstructorAndCourseFee(instructor, courseFee));
	}
	
	// Aggregation method using API
	@Operation(
			summary = "Get List of Courses by instructor and Course Fee using Aggregation API",
			description = "We fetch all courses with the given instructor. We get List of courses as response",
			tags = {"courses", "get"}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				content = {@Content(schema = @Schema(implementation = List.class), 
				mediaType = "application/json")})
	})
	@GetMapping("/instructor/courseFee/")
	public ResponseEntity<List<Course>> getCoursesByInstructor() {
		return ResponseEntity.ok(courseService.findCoursesByInstructorAndCourseFee());
	}
	
	// Aggregation method using API -> MATCH and GROUP
		@Operation(
				summary = "Get Custom result using Match and Group",
				description = "We get instructor, totalCourses, averageCourseFee",
				tags = {"courses", "get"}
				)
		@ApiResponses({
			@ApiResponse(responseCode = "200", 
					content = {@Content(schema = @Schema(implementation = ResponseDto.class), 
					mediaType = "application/json")})
		})
		@GetMapping("/match/{instructor}")
		public ResponseEntity<ResponseDto> getAggregateEntity(@PathVariable String instructor) {
			return ResponseEntity.ok(courseService.matchAndGroupAggregation(instructor));
		}
	
	
}
