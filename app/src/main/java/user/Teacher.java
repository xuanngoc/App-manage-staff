package user;


import com.example.managestaff.MainActivity;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class Teacher {

    public String teachCode = "CTI";
    public String fullName;
    public String birthday;
    public String gender;
    public String joinDate;
    public float salaryCoefficient;







    public Teacher() {
        // Default constructor
    }

    public Teacher( String fullName, String birthday, String gender, String joinDate, float salaryCoefficient) {
        this.teachCode = teachCode + MainActivity.currentTeacherCode ++;
        MainActivity.updateCurrentTeacherCode(MainActivity.currentTeacherCode);
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.joinDate = joinDate;
        this.salaryCoefficient = salaryCoefficient;
    }

    public Teacher( String teacherCode, String fullName, String birthday, String gender, String joinDate, float salaryCoefficient) {
        this.teachCode = teacherCode;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.joinDate = joinDate;
        this.salaryCoefficient = salaryCoefficient;
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
