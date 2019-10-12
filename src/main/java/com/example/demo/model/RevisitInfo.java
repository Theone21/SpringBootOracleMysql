package com.example.demo.model;

import java.util.Date;

public class RevisitInfo {
    private Integer revisitId;

    private String revisitCode;

    private String revisitName;

    private Integer insertUser;

    private Date insertDate;

    private Integer subjectId;

    private Integer siteId;

    private String revisitRemark;

    private Byte lockFlag;

    private Date createDate;

    private Integer isImport;

    public Integer getRevisitId() {
        return revisitId;
    }

    public void setRevisitId(Integer revisitId) {
        this.revisitId = revisitId;
    }

    public String getRevisitCode() {
        return revisitCode;
    }

    public void setRevisitCode(String revisitCode) {
        this.revisitCode = revisitCode == null ? null : revisitCode.trim();
    }

    public String getRevisitName() {
        return revisitName;
    }

    public void setRevisitName(String revisitName) {
        this.revisitName = revisitName == null ? null : revisitName.trim();
    }

    public Integer getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(Integer insertUser) {
        this.insertUser = insertUser;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getRevisitRemark() {
        return revisitRemark;
    }

    public void setRevisitRemark(String revisitRemark) {
        this.revisitRemark = revisitRemark == null ? null : revisitRemark.trim();
    }

    public Byte getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Byte lockFlag) {
        this.lockFlag = lockFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }

    @Override
    public String toString() {
        return "RevisitInfo{" +
                "revisitId=" + revisitId +
                ", revisitCode='" + revisitCode + '\'' +
                ", revisitName='" + revisitName + '\'' +
                ", insertUser=" + insertUser +
                ", insertDate=" + insertDate +
                ", subjectId=" + subjectId +
                ", siteId=" + siteId +
                ", revisitRemark='" + revisitRemark + '\'' +
                ", lockFlag=" + lockFlag +
                ", createDate=" + createDate +
                '}';
    }
}