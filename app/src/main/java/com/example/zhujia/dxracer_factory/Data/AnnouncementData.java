package com.example.zhujia.dxracer_factory.Data;

/**
 * Created by DXSW5 on 2017/9/20.
 *
 * 公告中心实体类
 */

public class AnnouncementData {
    private String id;
    private String businessId;
    private String annTheme;
    private String annWriter;
    private String createTime;
    private String annContent;

    public String getAnnContent() {
        return annContent;
    }

    public void setAnnContent(String annContent) {
        this.annContent = annContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAnnTheme() {
        return annTheme;
    }

    public void setAnnTheme(String annTheme) {
        this.annTheme = annTheme;
    }

    public String getAnnWriter() {
        return annWriter;
    }

    public void setAnnWriter(String annWriter) {
        this.annWriter = annWriter;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
