# Academic Management System

## Description
This project is a **Academic-Management-System** designed for universities. It provides a backend API that manages student enrollment, courses, departments, professors, and class schedules. The system supports authentication and authorization, allowing students to register for courses and instructors to assign grades.

## Features
- **User Authentication & Security**
  - JWT-based authentication
  - Role-based access control (Students & Instructors)
- **Student Management**
  - Register and manage student data
  - Enroll students in classes
- **Instructor Management**
  - Add and manage instructor information
  - Assign grades to students
- **Course & Class Management**
  - Add and update courses and class schedules
  - Associate courses with instructors and students
- **Department Management**
  - Organize courses and instructors into departments
- **Exception Handling**
  - Handles resource duplication and missing data
  
## Project Structure
```
*src
│
└── main
    └── java
        └── ju
            ├── configuration
            │   ├── JwtFilter.java
            │   ├── SecurityConfiguration.java
            │
            ├── controller
            │   ├── ClassController.java
            │   ├── ClassStudentController.java
            │   ├── CourseController.java
            │   ├── DepartmentController.java
            │   ├── InstructorController.java
            │   ├── StudentController.java
            │   ├── UserController.java
            │
            ├── dto
            │   ├── ClassDto.java
            │   ├── ClassStudentDto.java
            │   ├── CourseDto.java
            │   ├── DepartmentDto.java
            │   ├── InstructorDto.java
            │   ├── StudentDto.java
            │
            ├── exception
            │   ├── DuplicateResourceException.java
            │   ├── GlobalExceptionHandler.java
            │   ├── ResourceNotFoundException.java
            │
            ├── model
            │   ├── Course.java
            │   ├── Department.java
            │   ├── Instructor.java
            │   ├── Role.java (Enum)
            │   ├── Student.java
            │   ├── User.java
            │   ├── Class.java
            │   ├── ClassStudent
            │       ├── ClassStudent.java
            │       ├── ClassStudentId.java
            │       ├── Enum
            │           ├── Passed.java
            │
            ├── repository
            │   ├── ClassRepository.java
            │   ├── ClassStudentRepository.java
            │   ├── CourseRepository.java
            │   ├── DepartmentRepository.java
            │   ├── InstructorRepository.java
            │   ├── StudentRepository.java
            │   ├── UserRepository.java
            │
            ├── service
                ├── ClassService.java
                ├── ClassStudentService.java
                ├── CourseService.java
                ├── DepartmentService.java
                ├── InstructorService.java
                ├── JwtService.java
                ├── MyUserService.java
                ├── StudentService.java
                ├── UserService.java
```

## Technologies Used
- **Java 17**
- **Spring Boot** (Spring Security, Spring Data JPA)
- **JWT Authentication**
- **MySQL** (Database)
- **Maven** (Dependency Management)

## Installation & Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/0bada02/Academic-Management-System.git
   ```
2. Navigate to the project directory:
   ```sh
   cd Academic-Management-System
   ```
3. Configure the **application.properties** file with your database credentials.
4. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```

## API Endpoints
### Authentication
- `POST /api/auth/login` - Login for students and instructors
- `POST /api/auth/register` - Register a new user

### Students
- `GET /api/students` - Retrieve all students
- `POST /api/students` - Add a new student
- `GET /api/students/{id}` - Retrieve student details

### Instructors
- `GET /api/instructors` - Retrieve all instructors
- `POST /api/instructors` - Add a new instructor

### Courses & Classes
- `GET /api/courses` - Retrieve all courses
- `POST /api/courses` - Add a new course
- `GET /api/classes` - Retrieve all classes
- `POST /api/classes` - Add a new class

### Departments
- `GET /api/departments` - Retrieve all departments
- `POST /api/departments` - Add a new department

## License
This project is licensed under the MIT License.
