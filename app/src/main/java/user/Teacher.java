package user;


import com.example.managestaff.AddTeacherActivity;
import com.example.managestaff.MainActivity;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class Teacher {


    private String teachCode ;

    private String fullName;
    private String birthday;
    private String gender;
    private String joinDate;
    private float salaryCoefficient;

    public Teacher() {
        // Default constructor
    }

    public Teacher(String departmentCode , String fullName, String birthday, String gender, String joinDate, float salaryCoefficient) {
        if(departmentCode.length() == 3){ // format: CTI + currrentTeacherCode

            //AddTeacherActivity.getCurrentTeacherCode(departmentCode);

            //System.out.println("current1 = " + AddTeacherActivity.currentTeacherCode);

            this.teachCode = departmentCode + AddTeacherActivity.currentTeacherCode ;

            System.out.println(this.teachCode);

            //AddTeacherActivity.updateCurrentTeacherCode(departmentCode);
            //System.out.println("update xong roi");
        }else {
            this.teachCode = departmentCode; // do nothing
        }


        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.joinDate = joinDate;
        this.salaryCoefficient = salaryCoefficient;
    }

    /*public Teacher(  String teacherCode, String fullName, String birthday, String gender, String joinDate, float salaryCoefficient) {

        this.teachCode = teacherCode;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.joinDate = joinDate;
        this.salaryCoefficient = salaryCoefficient;
    }*/




   /* public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
*/
    public void setTeachCode(String teachCode) {
        this.teachCode = teachCode;
    }

    public String getTeachCode() {
        return teachCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
    public float getSalaryCoefficient() {
        return salaryCoefficient;
    }

    public void setSalaryCoefficient(float salaryCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
    }

    public int getAge(){
        int yearOfBirth;
        if(birthday.length() == 8){ // format: d/mm/yyyy
            yearOfBirth = Integer.parseInt( birthday.substring(4));
        }else{ //format: dd/mm/yyyy
            yearOfBirth = Integer.parseInt( birthday.substring(5));
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear - yearOfBirth;
    }
}
