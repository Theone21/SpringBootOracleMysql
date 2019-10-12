package com.example.demo.service;

import com.example.demo.mapper.PatientCodeMapper;
import com.example.demo.mapper.RevisitInfoMapper;
import com.example.demo.model.*;
import com.example.demo.oraclemapper.ReVisitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private PatientCodeMapper pcm;

    @Autowired
    private ReVisitMapper oracleRvm;

    @Autowired
    private RevisitInfoMapper mysqlRim;

    @Override
    public Long getLength() {
        return pcm.getLength();
    }

    @Override
    public Long getReVisitLen() {
        return oracleRvm.getVisitLength();
    }

    @Override
    public RevisitInfo get(){
        RevisitInfo ri = new RevisitInfo();
        ri.setRevisitCode("code");
        ri.setRevisitName("name");
        mysqlRim.insertSelective(ri);
        return mysqlRim.selectByPrimaryKey(4);
    }

    @Override
    public String two() {
        long len = oracleRvm.getVisitLength();
        RevisitInfo ri = mysqlRim.selectByPrimaryKey(4);
        return len + ri.toString();
    }

    /**
     * 转换任务表
     * @return
     */
    @Override
    public Integer convertRevisitInfoData(){
        List<RevisitInfo> pvs = oracleRvm.getRevisitInfo();
        for(int i = 0; i < pvs.size(); i++){
            pvs.get(i).setIsImport(1);
            mysqlRim.insertSelective(pvs.get(i));
        }
        return 1;
    }

    /**
     * 转换任务下面的人
     * @return
     */
    @Override
    public Integer convertRevisitPatient() {
        List<RevisitPatient> rps = oracleRvm.getRevisitPatient();
        for(int i = 0; i < rps.size(); i++){
            RevisitPatient rp = rps.get(i);
            Integer revisitId = mysqlRim.getReVisitIdByCode(rp.getRevisitCode());
            Integer patientId = mysqlRim.getPatientIdByCode(rp.getPatientCode());
            rp.setRevisitId(revisitId);
            rp.setPatientId(patientId);
            rp.setIsImport(1);
            mysqlRim.insertRevisitPatient(rp);
        }
        return 1;
    }

    /**
     * 转换任务分配
     */
    @Override
    public Integer convertRevisitEntry() {

        List<RevisitEntry> lists = oracleRvm.getRevisitEntry();
        for(int i = 0; i < lists.size(); i++){
            RevisitEntry re = lists.get(i);
            Integer userId =  mysqlRim.getUserIdByName(re.getUserName());
            Assert.notNull(userId, "userId不能为空-"+re.getUserName());
            re.setUserId(userId);
            Integer revisitId =  mysqlRim.getReVisitIdByCode(re.getRevisitCode());
            re.setRevisitId(revisitId);
            Integer villageId =  mysqlRim.getVillageIdByName(re.getVillageName(), re.getTown());
            re.setVillageId(villageId);
            re.setIsImport(1);
            mysqlRim.insertRevisitEntryArea(re);
        }
        return 1;
    }

    /**
     * 转换随访结果临时表
     */
    @Override
    @Transactional
    public Integer convertRevisitResultTemp(){
        Map<Integer, String> resultMapToCnValue = new HashMap<>();
        resultMapToCnValue.put(1, "随访到且无肿瘤");
        resultMapToCnValue.put(2, "随访到且发生肿瘤");
        resultMapToCnValue.put(3, "死亡");
        resultMapToCnValue.put(4, "失访");
        resultMapToCnValue.put(5, "发生肿瘤并死亡");

        Map<Integer, String> resultMapToValue = new HashMap<>();
        resultMapToValue.put(1, "296320");
        resultMapToValue.put(2, "296321");
        resultMapToValue.put(3, "296322");
        resultMapToValue.put(4, "296323");
        resultMapToValue.put(5, "296324");

        List<RevisitResultTemp> rrts = oracleRvm.getResultTemp();
        for(int i = 0; i < rrts.size(); i++){
            RevisitResultTemp rrt = rrts.get(i);
            Integer patientId = mysqlRim.getPatientIdByCode(rrt.getPatientCode());
            Integer villageId = mysqlRim.getVillageIdByPatientCode(rrt.getPatientCode());
            Integer revisitId = mysqlRim.getReVisitIdByCode(rrt.getRevisitCode());
            Integer userId = mysqlRim.getUserIdByName(rrt.getUserName());
            Integer revisitResult = rrt.getResult();

            rrt.setRevisitId(revisitId);
            rrt.setPatientId(patientId);
            rrt.setUserId(userId);
            rrt.setVillageId(villageId);
            rrt.setResultStr(resultMapToCnValue.get(revisitResult));
            mysqlRim.insertRevisitResultTemp(rrt);

            RevisitCrfContentTemp rcct = new RevisitCrfContentTemp();
            rcct.setTemplateId(2134);
            rcct.setPatientId(patientId);
            rcct.setVillageId(villageId);
            rcct.setRevisitId(revisitId);
            rcct.setUserId(userId);
            rcct.setElemText("128778_2134_1");
            rcct.setElemType("radio");
            rcct.setElemValue(resultMapToValue.get(revisitResult));
            rcct.setElemCnValue(resultMapToCnValue.get(revisitResult));
            rcct.setDataSource("import");
            mysqlRim.insertCrfContentTemp(rcct);
            if(revisitResult == 2){
                List<RegisterCard> rcs = oracleRvm.getRegisterCard(rrt.getPatientCode(), rrt.getRevisitCode(),
                        rrt.getOracleUserId(), "R02");

                handleDiseaseCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContentTemp(rcct);
                    }
                });
            }
            if (revisitResult == 3){
                List<RegisterCard> rcs = oracleRvm.getRegisterCard(rrt.getPatientCode(), rrt.getRevisitCode(), rrt.getOracleUserId(), "R01");
                handleDeadCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContentTemp(rcct);
                    }
                });
            }
            if(revisitId == 4){
                handleLost(rrt, rcct, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContentTemp(rcct);
                    }
                });
            }
            if(revisitId == 5){
                List<RegisterCard> rcs = oracleRvm.getRegisterCard(rrt.getPatientCode(), rrt.getRevisitCode(), rrt.getOracleUserId(), "R02");
                handleDiseaseCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContentTemp(rcct);
                    }
                });
                rcs = oracleRvm.getRegisterCard(rrt.getPatientCode(), rrt.getRevisitCode(), rrt.getOracleUserId(), "R01");
                handleDeadCard(rrt, rcct, false, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContentTemp(rcct);
                    }
                });
            }
        }
        return 1;
    }

    /**
     * 处理失访情况
     */
    private void handleLost(RevisitResultTemp rrt, RevisitCrfContentTemp rcct, InsertData insertData){
        Map<String, String> valueToText = new HashMap<>();
        valueToText.put("外出打工", "128802_2134_1");
        valueToText.put("迁出", "128802_2134_1");
        valueToText.put("拒绝随访", "128802_2134_1");

        Map<String, String> valueToId = new HashMap<>();
        valueToId.put("外出打工", "296360");
        valueToId.put("迁出", "296361");
        valueToId.put("拒绝随访", "296362");

        String text = valueToText.get(rrt.getLostRevisitReason());
        if(text != null){
            rcct.setElemText(text);
            rcct.setElemType("radio");
            rcct.setElemValue(valueToId.get(rrt.getLostRevisitReason()));
            rcct.setElemCnValue(rrt.getLostRevisitReason());
            rcct.setDataSource("import");
            insertData.insertData(rcct);
        }
        if (rrt.getLostRevisitReason().startsWith("其他")){
            rcct.setElemText("128802_2134_1");
            rcct.setElemType("radio");
            rcct.setElemValue("296363");
            rcct.setElemCnValue("其他");
            rcct.setDataSource("import");
            insertData.insertData(rcct);

            rcct.setElemText("128803_2134_1");
            rcct.setElemType("text");
            String value  = rrt.getLostRevisitReason().split(":")[1];
            rcct.setElemValue(value);
            rcct.setElemCnValue(value);
            rcct.setDataSource("import");
            insertData.insertData(rcct);
        }

        rcct.setElemText("128782_2134_1");
        rcct.setElemType("text");
        Date date = rrt.getRevisitDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        rcct.setElemValue(sdf.format(date));
        rcct.setElemCnValue(sdf.format(date));
        rcct.setDataSource("import");
        insertData.insertData(rcct);
    }

    /**
     * 处理死亡登记卡
     * @param rrt
     */
    private void handleDeadCard(RevisitResultTemp rrt, RevisitCrfContentTemp rcct, boolean saveSign, List<RegisterCard> rcs, InsertData insertData){
        Map<String, String> elementIdToText = new HashMap<>();

        elementIdToText.put("R01.04", "128793_2134_1");
        elementIdToText.put("R01.05", "128794_2134_1");
        elementIdToText.put("R01.06", "128795_2134_1");
        elementIdToText.put("R01.07", "128796_2134_1");
        elementIdToText.put("R01.08", "128797_2134_1");
        elementIdToText.put("R01.09", "129087_2134_1");
        elementIdToText.put("R01.10", "129085_2134_1");


        for(int i = 0; i < rcs.size(); i++){
            RegisterCard rc = rcs.get(i);
            String elementId = rc.getElementId();
            String elementText = elementIdToText.get(elementId);
            if (elementText == null){
                continue;
            }
            String elementType = mysqlRim.getElementTypeByText(elementText);
            Assert.notNull(elementType, elementText + "没有找到对应的元素Type");
            String cnValue = rc.getCardValue();
            if (cnValue == null){
                continue;
            }
            String tempCnValue = cnValue;
            if (tempCnValue.startsWith("其他")){
                tempCnValue = "其他";
            }
            String elemValue = mysqlRim.getElemValueByCnValue(tempCnValue, elementText);
            if(elemValue == null){
                elemValue = tempCnValue;
            }

            rcct.setElemText(elementText);
            rcct.setElemType(elementType);
            rcct.setElemValue(elemValue);
            rcct.setElemCnValue(tempCnValue);
            rcct.setDataSource("import");
            insertData.insertData(rcct);
            if(cnValue.startsWith("其他") && elementText.equals("128797_2134_1") && cnValue.contains(":")){
                rcct.setElemText("128798_2134_1");
                rcct.setElemType("text");
                String newCnValue = cnValue.split(":")[1];
                rcct.setElemValue(newCnValue);
                rcct.setElemCnValue(newCnValue);
                insertData.insertData(rcct);
            }
            if(cnValue.startsWith("其他") && elementText.equals("129087_2134_1") && cnValue.contains(":")){
                rcct.setElemText("129091_2134_1");
                rcct.setElemType("text");
                String newCnValue = cnValue.split(":")[1];
                rcct.setElemValue(newCnValue);
                rcct.setElemCnValue(newCnValue);
                insertData.insertData(rcct);
            }
        }
        if(rcs.size() > 0){
            RegisterCard rc = rcs.get(0);
            // 死亡者被访家属签字
            rcct.setElemText("128801_2134_1");
            rcct.setElemType("text");
            rcct.setElemValue(rc.getPatientName());
            rcct.setElemCnValue(rc.getPatientName());
            rcct.setDataSource("import");
            insertData.insertData(rcct);

            if(saveSign){
                // 随访工作人员签字
                rcct.setElemText("128781_2134_1");
                rcct.setElemType("text");
                rcct.setElemValue(rc.getSign());
                rcct.setElemCnValue(rc.getSign());
                rcct.setDataSource("import");
                insertData.insertData(rcct);
            }

            if (saveSign){
                // 随访日期
                rcct.setElemText("128782_2134_1");
                rcct.setElemType("text");
                Date date = rc.getRegisterDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                rcct.setElemValue(sdf.format(date));
                rcct.setElemCnValue(sdf.format(date));
                rcct.setDataSource("import");
                insertData.insertData(rcct);
            }


        }

    }

    /**
     * 处理肿瘤发病卡
     * @param rrt
     */
    private void handleDiseaseCard(RevisitResultTemp rrt, RevisitCrfContentTemp rcct, boolean saveSign, List<RegisterCard> rcs, InsertData insertData){
        Map<String, String> elementIdToText = new HashMap<>();

        elementIdToText.put("R02.04", "129596_2134_1");
        elementIdToText.put("R02.05", "129597_2134_1");
        elementIdToText.put("R02.06", "129599_2134_1");
        elementIdToText.put("R02.07", "129602_2134_1");
        elementIdToText.put("R02.08", "129603_2134_1");
        elementIdToText.put("R02.09", "129601_2134_1");
        elementIdToText.put("R02.10", "129604_2134_1");


        for(int i = 0; i < rcs.size(); i++){
            RegisterCard rc = rcs.get(i);
            String elementId = rc.getElementId();
            String elementText = elementIdToText.get(elementId);
            if (elementText == null){
                continue;
            }
            String elementType = mysqlRim.getElementTypeByText(elementText);
            Assert.notNull(elementType, elementText + "没有找到对应的元素Type");
            String cnValue = rc.getCardValue();
            if (cnValue == null){
                continue;
            }
            String tempCnValue = cnValue;
            if (tempCnValue.startsWith("其他（请注明）:")){
                tempCnValue = "其他";
            }
            String elemValue = mysqlRim.getElemValueByCnValue(tempCnValue, elementText);
            if(elemValue == null){
                elemValue = tempCnValue;
            }

            rcct.setElemText(elementText);
            rcct.setElemType(elementType);
            rcct.setElemValue(elemValue);
            rcct.setElemCnValue(tempCnValue);
            rcct.setDataSource("import");
            insertData.insertData(rcct);
//            mysqlRim.insertCrfContentTemp(rcct);
            if(cnValue.startsWith("其他（请注明）:") && elementText.equals("129597_2134_1")){
                rcct.setElemText("129598_2134_1");
                rcct.setElemType("text");
                String newCnValue = cnValue.split(":")[1];
                rcct.setElemValue(newCnValue);
                rcct.setElemCnValue(newCnValue);
                insertData.insertData(rcct);
//                mysqlRim.insertCrfContentTemp(rcct);
            }
            if(cnValue.startsWith("其他（请注明）:") && elementText.equals("129599_2134_1")){
                rcct.setElemText("129600_2134_1");
                rcct.setElemType("text");
                String newCnValue = cnValue.split(":")[1];
                rcct.setElemValue(newCnValue);
                rcct.setElemCnValue(newCnValue);
                insertData.insertData(rcct);
//                mysqlRim.insertCrfContentTemp(rcct);
            }
        }
        if(rcs.size() > 0){
            RegisterCard rc = rcs.get(0);
            // 本人签字
            rcct.setElemText("128792_2134_1");
            rcct.setElemType("text");
            rcct.setElemValue(rc.getPatientName());
            rcct.setElemCnValue(rc.getPatientName());
            rcct.setDataSource("import");
            insertData.insertData(rcct);
//            mysqlRim.insertCrfContentTemp(rcct);

            if(saveSign){
                // 工作人员签字
                rcct.setElemText("128781_2134_1");
                rcct.setElemType("text");
                rcct.setElemValue(rc.getSign());
                rcct.setElemCnValue(rc.getSign());
                rcct.setDataSource("import");
                insertData.insertData(rcct);
//                mysqlRim.insertCrfContentTemp(rcct);
            }

            if (saveSign){
                // 随访日期
                rcct.setElemText("128782_2134_1");
                rcct.setElemType("text");
                Date date = rc.getRegisterDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                rcct.setElemValue(sdf.format(date));
                rcct.setElemCnValue(sdf.format(date));
                rcct.setDataSource("import");
                insertData.insertData(rcct);
//                mysqlRim.insertCrfContentTemp(rcct);
            }


        }

    }

    /**
     * 转换随访结果
     *
     * @return
     */
    @Override
    @Transactional
    public Integer convertRevisitResult() {
        Map<Integer, String> resultMap = new HashMap<>();
        resultMap.put(1, "随访到且无肿瘤");
        resultMap.put(2, "随访到且发生肿瘤");
        resultMap.put(3, "死亡");
        resultMap.put(4, "失访");
        resultMap.put(5, "发生肿瘤并死亡");

        Map<Integer, String> resultMapToValue = new HashMap<>();
        resultMapToValue.put(1, "296320");
        resultMapToValue.put(2, "296321");
        resultMapToValue.put(3, "296322");
        resultMapToValue.put(4, "296323");
        resultMapToValue.put(5, "296324");


        List<RevisitResultTemp> rrts = oracleRvm.getResult();
        for(int i = 0; i < rrts.size(); i++){
            RevisitResultTemp rrt = rrts.get(i);
            rrt.setRevisitId(mysqlRim.getReVisitIdByCode(rrt.getRevisitCode()));
            rrt.setPatientId(mysqlRim.getPatientIdByCode(rrt.getPatientCode()));
            rrt.setVillageId(mysqlRim.getVillageIdByPatientCode(rrt.getPatientCode()));
            rrt.setResultStr(resultMap.get(rrt.getResult()));
            mysqlRim.insertResult(rrt);


            Integer revisitResult = rrt.getResult();
            Integer patientId = mysqlRim.getPatientIdByCode(rrt.getPatientCode());
            Integer villageId = mysqlRim.getVillageIdByPatientCode(rrt.getPatientCode());
            Integer revisitId = mysqlRim.getReVisitIdByCode(rrt.getRevisitCode());


            RevisitCrfContentTemp rcct = new RevisitCrfContentTemp();
            rcct.setTemplateId(2134);
            rcct.setPatientId(patientId);
            rcct.setVillageId(villageId);
            rcct.setRevisitId(revisitId);
            rcct.setElemText("128778_2134_1");
            rcct.setElemType("radio");
            rcct.setElemValue(resultMapToValue.get(revisitResult));
            rcct.setElemCnValue(resultMap.get(revisitResult));
            rcct.setDataSource("import");
            mysqlRim.insertCrfContent(rcct);
            if(revisitResult == 2){
                List<RegisterCard> rcs = oracleRvm.getRegisterCardFinal(rrt.getPatientCode(), rrt.getRevisitCode(),
                         "R02");

                handleDiseaseCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContent(rcct);
                    }
                });
            }
            if (revisitResult == 3){
                List<RegisterCard> rcs = oracleRvm.getRegisterCardFinal(rrt.getPatientCode(), rrt.getRevisitCode(), "R01");
                handleDeadCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContent(rcct);
                    }
                });
            }
            if(revisitId == 4){
                handleLost(rrt, rcct, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContent(rcct);
                    }
                });
            }
            if(revisitId == 5){
                List<RegisterCard> rcs = oracleRvm.getRegisterCardFinal(rrt.getPatientCode(), rrt.getRevisitCode(), "R02");
                handleDiseaseCard(rrt, rcct, true, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContent(rcct);
                    }
                });
                rcs = oracleRvm.getRegisterCardFinal(rrt.getPatientCode(), rrt.getRevisitCode(), "R01");
                handleDeadCard(rrt, rcct, false, rcs, new InsertData() {
                    @Override
                    public void insertData(RevisitCrfContentTemp rcct) {
                        mysqlRim.insertCrfContent(rcct);
                    }
                });
            }
        }

        return 1;
    }
}
