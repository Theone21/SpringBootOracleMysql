package com.example.demo.oraclemapper;

import com.example.demo.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReVisitMapper {

    Long getVisitLength();

    List<RevisitInfo> getRevisitInfo();

    List<RevisitPatient> getRevisitPatient();

    List<RevisitEntry> getRevisitEntry();

    List<RevisitResultTemp> getResultTemp();

    List<RevisitResultTemp> getResult();

    List<RegisterCard> getRegisterCard(@Param("patientCode") String patientCode, @Param("revisitCode") String revisitCode,
                                       @Param("inUser") String inUser, @Param("cardId") String cardId);

    List<RegisterCard> getRegisterCardFinal(@Param("patientCode") String patientCode, @Param("revisitCode") String revisitCode,
                                            @Param("cardId") String cardId);
}
