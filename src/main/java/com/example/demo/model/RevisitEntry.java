package com.example.demo.model;

import java.util.Date;

public class RevisitEntry {

    private String revisitCode;
    private String userName;
    private String town;
    private String villageName;
    private String manager;
    private Date insertDate;
    private Integer state;
    private Integer submitState;
    private Integer isImport;
    private Integer userId;
    private Integer revisitId;
    private Integer villageId;


    public String getRevisitCode() {
        return revisitCode;
    }

    public void setRevisitCode(String revisitCode) {
        this.revisitCode = revisitCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSubmitState() {
        return submitState;
    }

    public void setSubmitState(Integer submitState) {
        this.submitState = submitState;
    }

    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRevisitId() {
        return revisitId;
    }

    public void setRevisitId(Integer revisitId) {
        this.revisitId = revisitId;
    }

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }
}
