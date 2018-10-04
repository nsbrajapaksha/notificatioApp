package com.example.android.notificationexample;

public class Users extends UserId{

    String fromuserid, message, name, faculty, department, indexNo, vehicle,
            date, passengers, stime, etime, sdate, edate, vehicleNo;

    public Users() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public Users(String fromuserid, String message, String date, String passengers, String stime,
                 String etime, String sdate, String edate, String name, String faculty, String department,
                 String indexNo, String vehicle, String vehicleNo) {
        this.fromuserid = fromuserid;
        this.message = message;
        this.date  =date;
        this.passengers = passengers;
        this.stime = stime;
        this.etime = etime;
        this.sdate = sdate;
        this.edate = edate;
        this.name = name;
        this.faculty = faculty;
        this.department = department;
        this.indexNo = indexNo;
        this.vehicle = vehicle;
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getPassengers() {
        return passengers;
    }

    public String getStime() {
        return stime;
    }

    public String getEtime() {
        return etime;
    }

    public String getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }
}
