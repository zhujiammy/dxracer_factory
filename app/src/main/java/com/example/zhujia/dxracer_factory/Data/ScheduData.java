package com.example.zhujia.dxracer_factory.Data;

import java.sql.PreparedStatement;

/**
 * Created by zhujia on 2017/11/21.
 *
 * 排单生产实体类
 */

public class ScheduData {

    private String id;
    private String business_id;
    private String planDate;
    private String dealerName;
    private String orderNo;
    private String totalQuantity;
    private String purchasePlan;
    private String orderstatus;
    private String orderId;


    private String productCode;
    private String quantity;

    private String partNo;
    private String name;
    private String supplierPartNo;

    private String remark;
    private String operator;
    private String createTime;

    private String productionDate;
    private String fcno;
    private String productionOrderNo;
    private String status;
    private String field4;
    private String searchMaterialPerson;
    private String searchMaterialStatus;
    private String searchMaterialScore;
    private String field1;

    private String craftName;
    private String unitPrice;
    private String overtimePay;
    private String totalprice;
    private String personCode;
    private String realName;
    private String location;


    private String stepImg;
    private String stepName;
    private String procedureId;
    private String craftTypeId;
    private String modelCraftProcedurename;
    private String modelCraftTypename;
    private String stencilNo;
    private String stencilId;



    private String purchaseOrderNo;
    private String originalPurchaseOrderNo;
    private String purchasePaymentOrder;
    private String supplierNo;
    private String planProductionDate;
    private String sendTime;
    private String plateNumber;


    public String getSupplierPartNo() {
        return supplierPartNo;
    }

    public void setSupplierPartNo(String supplierPartNo) {
        this.supplierPartNo = supplierPartNo;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getOriginalPurchaseOrderNo() {
        return originalPurchaseOrderNo;
    }

    public void setOriginalPurchaseOrderNo(String originalPurchaseOrderNo) {
        this.originalPurchaseOrderNo = originalPurchaseOrderNo;
    }

    public String getPurchasePaymentOrder() {
        return purchasePaymentOrder;
    }

    public void setPurchasePaymentOrder(String purchasePaymentOrder) {
        this.purchasePaymentOrder = purchasePaymentOrder;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getPlanProductionDate() {
        return planProductionDate;
    }

    public void setPlanProductionDate(String planProductionDate) {
        this.planProductionDate = planProductionDate;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStencilId() {
        return stencilId;
    }

    public void setStencilId(String stencilId) {
        this.stencilId = stencilId;
    }

    public String getStepImg() {
        return stepImg;
    }

    public void setStepImg(String stepImg) {
        this.stepImg = stepImg;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getCraftTypeId() {
        return craftTypeId;
    }

    public void setCraftTypeId(String craftTypeId) {
        this.craftTypeId = craftTypeId;
    }

    public String getModelCraftProcedurename() {
        return modelCraftProcedurename;
    }

    public void setModelCraftProcedurename(String modelCraftProcedurename) {
        this.modelCraftProcedurename = modelCraftProcedurename;
    }

    public String getModelCraftTypename() {
        return modelCraftTypename;
    }

    public void setModelCraftTypename(String modelCraftTypename) {
        this.modelCraftTypename = modelCraftTypename;
    }

    public String getStencilNo() {
        return stencilNo;
    }

    public void setStencilNo(String stencilNo) {
        this.stencilNo = stencilNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(String overtimePay) {
        this.overtimePay = overtimePay;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getFcno() {
        return fcno;
    }

    public void setFcno(String fcno) {
        this.fcno = fcno;
    }

    public String getProductionOrderNo() {
        return productionOrderNo;
    }

    public void setProductionOrderNo(String productionOrderNo) {
        this.productionOrderNo = productionOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getSearchMaterialPerson() {
        return searchMaterialPerson;
    }

    public void setSearchMaterialPerson(String searchMaterialPerson) {
        this.searchMaterialPerson = searchMaterialPerson;
    }

    public String getSearchMaterialStatus() {
        return searchMaterialStatus;
    }

    public void setSearchMaterialStatus(String searchMaterialStatus) {
        this.searchMaterialStatus = searchMaterialStatus;
    }

    public String getSearchMaterialScore() {
        return searchMaterialScore;
    }

    public void setSearchMaterialScore(String searchMaterialScore) {
        this.searchMaterialScore = searchMaterialScore;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPurchasePlan() {
        return purchasePlan;
    }

    public void setPurchasePlan(String purchasePlan) {
        this.purchasePlan = purchasePlan;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
