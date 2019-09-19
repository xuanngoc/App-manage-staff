package user;

public class Subject {
    private String subjectCode;
    private String subjectName;
    private String subjectCredits;
    private String subjectHours;
    private String subjectCoefficient;

    public Subject(){

    }

    public Subject(String subjectCode, String subjectName, String subjectCredits, String subjectHours, String subjectCoefficient) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectCredits = subjectCredits;
        this.subjectHours = subjectHours;
        this.subjectCoefficient = subjectCoefficient;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCredits() {
        return subjectCredits;
    }

    public void setSubjectCredits(String subjectCredits) {
        this.subjectCredits = subjectCredits;
    }

    public String getSubjectHours() {
        return subjectHours;
    }

    public void setSubjectHours(String subjectHours) {
        this.subjectHours = subjectHours;
    }

    public String getSubjectCoefficient() {
        return subjectCoefficient;
    }

    public void setSubjectCoefficient(String subjectCoefficient) {
        this.subjectCoefficient = subjectCoefficient;
    }
}
