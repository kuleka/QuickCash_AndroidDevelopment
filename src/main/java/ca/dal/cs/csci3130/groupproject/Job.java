package ca.dal.cs.csci3130.groupproject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Job {

    public String employer, employee, title, date, expectedDuration, place, salary, latLong, status, evaluationToEmployer, evaluationToEmployee;
    public float distance = -1;
    public Boolean urgency;

    public Job(){

    }

    public Job(String employer, String employee, String title, String date, String expectedDuration, String place, String latLong, String salary, Boolean urgency, String status, String evaluationToEmployer, String evaluationToEmployee){
        this.title = title;
        this.date = date;
        this.expectedDuration = expectedDuration;
        this.place = place;
        this.salary = salary;
        this.urgency = urgency;
        this.employer = employer;
        this.employee = employee;
        this.latLong = latLong;
        this.status = status;
        this.evaluationToEmployer = evaluationToEmployer;
        this.evaluationToEmployee = evaluationToEmployee;
    }

    public String getLatLong() {
        return latLong;
    }

    public Boolean getUrgency() {
        return urgency;
    }

    public String getDate() {
        return date;
    }

    public String getExpectedDuration() {
        return expectedDuration;
    }

    public String getPlace() {
        return place;
    }

    public String getSalary() {
        return salary;
    }

    public String getTitle() {
        return title;
    }

    public String getEmployee() {
        return employee;
    }

    public String getEmployer() {
        return employer;
    }

    public float getDistance(){return distance;}

    public String getStatus() {
        return status;
    }

    public String getEvaluationToEmployer(){
        return evaluationToEmployer;
    }

    public String getEvaluationToEmployee(){
        return evaluationToEmployee;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExpectedDuration(String expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrgency(Boolean urgency) {
        this.urgency = urgency;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEvaluationToEmployer(String evaluationToEmployer) {
        this.evaluationToEmployer = evaluationToEmployer;
    }

    public void setEvaluationToEmployee(String evaluationToEmployee) {
        this.evaluationToEmployee = evaluationToEmployee;
    }

    public boolean isValidDate(String dateString) {
        Date currentDate = new Date();

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(dateString);
            System.out.println(date);
            if (date.before(currentDate)){
                return false;
            }

        } catch (ParseException e) {
            return false;
        }

        return true;
    }


}
