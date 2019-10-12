package com.example.demo.model;

public class RevisitPatient {

    private String revisitCode;

    private String town;

    private Integer villageId;

    private String patientCode;

    private Integer revisitId;

    private Integer patientId;

    private Integer isImport;

    public String getRevisitCode() {
        return revisitCode;
    }

    public void setRevisitCode(String revisitCode) {
        this.revisitCode = revisitCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public Integer getRevisitId() {
        return revisitId;
    }

    public void setRevisitId(Integer revisitId) {
        this.revisitId = revisitId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }
}
