package user;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Department {


    private String departmentCode;
    private String departmentName;
    private List<Teacher> teachers;

    public Department(){

    }

    public Department(String departmentCode, String departmentName){
        this.departmentCode = departmentCode;
        this.departmentName  = departmentName;
        teachers = new LinkedList<>();
    }

    public void addTeacher(Teacher teacher){
        teachers.add(teacher);
    }

    public Teacher getTeacher(String teacherCode){
        for(Iterator i = teachers.iterator(); i.hasNext(); ){
            Teacher teacher = (Teacher) i.next();
            if(teacher.getTeachCode().equals(teacherCode)){
                return teacher;
            }
        }
        return null;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

}
