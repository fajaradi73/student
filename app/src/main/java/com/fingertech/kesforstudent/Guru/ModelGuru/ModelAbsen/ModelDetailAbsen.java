package com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen;

import java.util.List;

public class ModelDetailAbsen {
    private  String nama;
    private String nis;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String attitudeid;
    private String attitude_name;
    private String attidude_desc;
    private String colour_code;
    private String attitude_grade_code;
    private String attitude_grade_name;
    private String attitude_grade_desc;
    private String attitude_grade_from;
    private String attitude_grade_to;

    private List<ModelDataAttidude> modelDataAttidudeList;

    public List<ModelDataAttidude> getModelDataAttidudeList() {
        return modelDataAttidudeList;
    }

    public void setModelDataAttidudeList(List<ModelDataAttidude> modelDataAttidudeList) {
        this.modelDataAttidudeList = modelDataAttidudeList;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getAttitudeid() {
        return attitudeid;
    }

    public void setAttitudeid(String attitudeid) {
        this.attitudeid = attitudeid;
    }

    public String getAttitude_name() {
        return attitude_name;
    }

    public void setAttitude_name(String attitude_name) {
        this.attitude_name = attitude_name;
    }

    public String getAttidude_desc() {
        return attidude_desc;
    }

    public void setAttidude_desc(String attidude_desc) {
        this.attidude_desc = attidude_desc;
    }

    public String getColour_code() {
        return colour_code;
    }

    public void setColour_code(String colour_code) {
        this.colour_code = colour_code;
    }

    public String getAttitude_grade_code() {
        return attitude_grade_code;
    }

    public void setAttitude_grade_code(String attitude_grade_code) {
        this.attitude_grade_code = attitude_grade_code;
    }

    public String getAttitude_grade_name() {
        return attitude_grade_name;
    }

    public void setAttitude_grade_name(String attitude_grade_name) {
        this.attitude_grade_name = attitude_grade_name;
    }

    public String getAttitude_grade_desc() {
        return attitude_grade_desc;
    }

    public void setAttitude_grade_desc(String attitude_grade_desc) {
        this.attitude_grade_desc = attitude_grade_desc;
    }

    public String getAttitude_grade_from() {
        return attitude_grade_from;
    }

    public void setAttitude_grade_from(String attitude_grade_from) {
        this.attitude_grade_from = attitude_grade_from;
    }

    public String getAttitude_grade_to() {
        return attitude_grade_to;
    }

    public void setAttitude_grade_to(String attitude_grade_to) {
        this.attitude_grade_to = attitude_grade_to;
    }
}
