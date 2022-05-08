package ca.dal.cs.csci3130.groupproject;

public class PreferJob {
    public String employee, title, lowerExpectedDuration, upperExpectedDuration, lowerSalary, upperSalary;

    public PreferJob(){

    }

    public PreferJob(String employee, String title, String lowerExpectedDuration, String upperExpectedDuration, String lowerSalary, String upperSalary){
        this.employee = employee;
        this.lowerExpectedDuration = lowerExpectedDuration;
        this.upperExpectedDuration = upperExpectedDuration;
        this.lowerSalary = lowerSalary;
        this.upperSalary = upperSalary;
        this.title = title;
    }


    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployee() {
        return employee;
    }

    public String getTitle() {
        return title;
    }

    public String getLowerExpectedDuration() {
        return lowerExpectedDuration;
    }

    public String getLowerSalary() {
        return lowerSalary;
    }

    public String getUpperExpectedDuration() {
        return upperExpectedDuration;
    }

    public String getUpperSalary() {
        return upperSalary;
    }

    public void setLowerExpectedDuration(String lowerExpectedDuration) {
        this.lowerExpectedDuration = lowerExpectedDuration;
    }

    public void setLowerSalary(String lowerSalary) {
        this.lowerSalary = lowerSalary;
    }

    public void setUpperExpectedDuration(String upperExpectedDuration) {
        this.upperExpectedDuration = upperExpectedDuration;
    }

    public void setUpperSalary(String upperSalary) {
        this.upperSalary = upperSalary;
    }
}
