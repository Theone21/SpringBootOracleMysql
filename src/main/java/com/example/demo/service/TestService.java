package com.example.demo.service;

import com.example.demo.model.RevisitInfo;

public interface TestService {

    Long getLength();

    Long getReVisitLen();

    RevisitInfo get();

    String two();

    Integer convertRevisitInfoData();

    /**
     * 转换任务下面的人
     * @return
     */
    Integer convertRevisitPatient();

    /**
     * 转换任务分配
     */
    Integer convertRevisitEntry();


    /**
     * 转换随访结果临时表
     * @return
     */
    Integer convertRevisitResultTemp();

    /**
     * 转换随访结果
     * @return
     */
    Integer convertRevisitResult();
}
