package com.felixwhitesean.classcommapp;

public class UnitsModelClass {
    private String unit_code, unit_name, serial_no, lecturer;

    public UnitsModelClass(String serial_no, String unit_code, String unit_name, String lecturer) {
        this.unit_code = unit_code;
        this.unit_name = unit_name;
        this.serial_no = serial_no;
        this.lecturer = lecturer;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return "UnitsModelClass{" +
                "unit_code='" + unit_code + '\'' +
                ", unit_name='" + unit_name + '\'' +
                ", serial_no='" + serial_no + '\'' +
                ", lecturer='" + lecturer + '\'' +
                '}';
    }
}
