package com.example.demo.mapper;

import com.example.demo.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisitInfoMapper {
    int insertSelective(RevisitInfo record);

    RevisitInfo selectByPrimaryKey(Integer revisitId);

    Integer getPatientIdByCode(@Param("patientCode") String patientCode);

    Integer getReVisitIdByCode(@Param("revisitCode") String revisitCode);

    Integer insertRevisitPatient(RevisitPatient revisitPatient);

    Integer getUserIdByName(String userName);

    Integer getVillageIdByName(@Param("villageName") String villageName, @Param("town") String town);

    Integer insertRevisitEntryArea(RevisitEntry revisitEntry);

    Integer insertRevisitResultTemp(RevisitResultTemp rrt);

    Integer getVillageIdByPatientCode(String patientCode);

    Integer insertResult(RevisitResultTemp rrt);

    Integer insertCrfContent(RevisitCrfContentTemp rcct);

    Integer insertCrfContentTemp(RevisitCrfContentTemp rcct);

    String getElementTypeByText(String elementText);

    String getElemValueByCnValue(@Param("elemValue") String elemValue, @Param("elementText") String elementText);
}