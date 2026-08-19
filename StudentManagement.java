public class Main {
    public static void main(String[] args) {
        StudentManager manager = new StudentManager();
        Validation validation = new Validation();

        while (true) {
            System.out.println("\nWELCOME TO STUDENT MANAGEMENT");
            System.out.println(" 1. Create");
            System.out.println(" 2. Find and Sort");
            System.out.println(" 3. Update/Delete");
            System.out.println(" 4. Report");
            System.out.println(" 5. Exit");
            System.out.print(" Please choose (1-5): ");

            int choice = validation.checkInputIntLimit(1, 5);

            switch (choice) {
                case 1:
                    manager.createStudent();
                    break;
                case 2:
                    manager.findAndSort();
                    break;
                case 3:
                    manager.updateOrDelete();
                    break;
                case 4:
                    manager.report();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    return;
            }
        }
    }
}
-----------------------------------------------------------------------------------
public class Student implements Comparable<Student> {
    private String id;
    private String studentName;
    private String semester;
    private String courseName;

    public Student() {
    }

    public Student(String id, String studentName, String semester, String courseName) {
        this.id = id;
        this.studentName = studentName;
        this.semester = semester;
        this.courseName = courseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public int compareTo(Student o) {
        return this.studentName.compareToIgnoreCase(o.getStudentName());
    }

    public void print() {
        System.out.printf("%-15s | %-15s | %-10s\n", studentName, semester, courseName);
    }
}
------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.Scanner;

public class Validation {
    private final Scanner in = new Scanner(System.in);

    public int checkInputIntLimit(int min, int max) {
        while (true) {
            try {
                int result = Integer.parseInt(in.nextLine().trim());
                if (result < min || result > max) {
                    throw new NumberFormatException();
                }
                return result;
            } catch (NumberFormatException e) {
                System.err.println("Please input number in range [" + min + ", " + max + "]");
                System.out.print("Enter again: ");
            }
        }
    }

    public String checkInputString() {
        while (true) {
            String result = in.nextLine().trim();
            if (result.isEmpty()) {
                System.err.println("Not empty");
                System.out.print("Enter again: ");
            } else {
                return result;
            }
        }
    }

    public boolean checkInputYN() {
        while (true) {
            String result = checkInputString();
            if (result.equalsIgnoreCase("Y")) {
                return true;
            }
            if (result.equalsIgnoreCase("N")) {
                return false;
            }
            System.err.println("Please input y/Y or n/N.");
            System.out.print("Enter again: ");
        }
    }

    public String checkInputCourse() {
        while (true) {
            String result = checkInputString();
            if (result.equalsIgnoreCase("java")
                    || result.equalsIgnoreCase(".net")
                    || result.equalsIgnoreCase("c/c++")) {
                return result;
            }
            System.err.println("There are only three courses: Java, .Net, C/C++");
            System.out.print("Enter again: ");
        }
    }

    public boolean checkStudentExist(ArrayList<Student> list, String id, String name, String semester, String course) {
        for (Student student : list) {
            if (id.equalsIgnoreCase(student.getId())
                    && name.equalsIgnoreCase(student.getStudentName())
                    && semester.equalsIgnoreCase(student.getSemester())
                    && course.equalsIgnoreCase(student.getCourseName())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkReportExist(ArrayList<Report> list, String name, String course, int total) {
        for (Report report : list) {
            if (name.equalsIgnoreCase(report.getStudentName())
                    && course.equalsIgnoreCase(report.getCourseName())
                    && total == report.getTotalCourse()) {
                return true;
            }
        }
        return false;
    }
}
-----------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.Collections;

// Sub-class phục vụ Báo cáo (Report)
class Report {
    private String studentName;
    private String courseName;
    private int totalCourse;

    public Report(String studentName, String courseName, int totalCourse) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.totalCourse = totalCourse;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getTotalCourse() {
        return totalCourse;
    }

    public void setTotalCourse(int totalCourse) {
        this.totalCourse = totalCourse;
    }
}

public class StudentManager {
    private final ArrayList<Student> studentList;
    private final Validation validation;

    public StudentManager() {
        this.studentList = new ArrayList<>();
        this.validation = new Validation();
    }

    // 1. Create Student
    public void createStudent() {
        if (studentList.size() >= 10) {
            System.out.print("Do you want to continue (Y/N): ");
            if (!validation.checkInputYN()) {
                return;
            }
        }
        while (true) {
            System.out.print("Enter id: ");
            String id = validation.checkInputString();
            System.out.print("Enter name student: ");
            String name = validation.checkInputString();
            
            // Kiểm tra trùng ID nhưng khác tên
            for (Student s : studentList) {
                if (s.getId().equalsIgnoreCase(id) && !s.getStudentName().equalsIgnoreCase(name)) {
                    System.err.println("ID already exists with name: " + s.getStudentName() + ". Name updated to match ID!");
                    name = s.getStudentName();
                    break;
                }
            }

            System.out.print("Enter semester: ");
            String semester = validation.checkInputString();
            System.out.print("Enter course name (Java, .Net, C/C++): ");
            String course = validation.checkInputCourse();

            if (!validation.checkStudentExist(studentList, id, name, semester, course)) {
                studentList.add(new Student(id, name, semester, course));
                System.out.println("Add student success.");
            } else {
                System.err.println("Duplicate record!");
            }

            if (studentList.size() >= 10) {
                System.out.print("Do you want to continue (Y/N): ");
                if (!validation.checkInputYN()) {
                    return;
                }
            }
        }
    }

    // 2. Find and Sort
    public void findAndSort() {
        if (studentList.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        System.out.print("Enter name to search: ");
        String name = validation.checkInputString();
        ArrayList<Student> listByName = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getStudentName().toLowerCase().contains(name.toLowerCase())) {
                listByName.add(student);
            }
        }
        if (listByName.isEmpty()) {
            System.err.println("Not found!");
        } else {
            Collections.sort(listByName);
            System.out.printf("%-15s | %-15s | %-10s\n", "Student Name", "Semester", "Course Name");
            for (Student student : listByName) {
                student.print();
            }
        }
    }

    // 3. Update or Delete
    public void updateOrDelete() {
        if (studentList.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        System.out.print("Enter id: ");
        String id = validation.checkInputString();
        ArrayList<Student> listById = getListStudentById(id);

        if (listById.isEmpty()) {
            System.err.println("Not found student.");
            return;
        }

        Student student = getStudentByList(listById);
        System.out.print("Do you want to update (U) or delete (D) student: ");
        if (validation.checkInputYN()) { // Y tương đương U/D tùy chọn người dùng
            System.out.print("Enter id: ");
            String idUpdate = validation.checkInputString();
            System.out.print("Enter name student: ");
            String nameUpdate = validation.checkInputString();
            System.out.print("Enter semester: ");
            String semesterUpdate = validation.checkInputString();
            System.out.print("Enter course name: ");
            String courseUpdate = validation.checkInputCourse();

            if (!validation.checkStudentExist(studentList, idUpdate, nameUpdate, semesterUpdate, courseUpdate)) {
                student.setId(idUpdate);
                student.setStudentName(nameUpdate);
                student.setSemester(semesterUpdate);
                student.setCourseName(courseUpdate);
                System.out.println("Update success.");
            } else {
                System.err.println("Duplicate record!");
            }
        } else {
            studentList.remove(student);
            System.out.println("Delete success.");
        }
    }

    private ArrayList<Student> getListStudentById(String id) {
        ArrayList<Student> getListById = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(id)) {
                getListById.add(student);
            }
        }
        return getListById;
    }

    private Student getStudentByList(ArrayList<Student> listById) {
        System.out.println("Found records:");
        int count = 1;
        System.out.printf("%-10s | %-15s | %-15s | %-10s\n", "Number", "Student Name", "Semester", "Course Name");
        for (Student student : listById) {
            System.out.printf("%-10d | %-15s | %-15s | %-10s\n", count, student.getStudentName(), student.getSemester(), student.getCourseName());
            count++;
        }
        System.out.print("Enter choice number: ");
        int choice = validation.checkInputIntLimit(1, listById.size());
        return listById.get(choice - 1);
    }

    // 4. Report
    public void report() {
        if (studentList.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        ArrayList<Report> reportList = new ArrayList<>();
        for (Student student : studentList) {
            int total = 0;
            for (Student countStudent : studentList) {
                if (student.getId().equalsIgnoreCase(countStudent.getId())
                        && student.getCourseName().equalsIgnoreCase(countStudent.getCourseName())) {
                    total++;
                }
            }
            if (!validation.checkReportExist(reportList, student.getStudentName(), student.getCourseName(), total)) {
                reportList.add(new Report(student.getStudentName(), student.getCourseName(), total));
            }
        }
        System.out.printf("%-15s | %-10s | %-5s\n", "Student Name", "Course", "Total");
        for (Report report : reportList) {
            System.out.printf("%-15s | %-10s | %-5d\n", report.getStudentName(), report.getCourseName(), report.getTotalCourse());
        }
    }
}