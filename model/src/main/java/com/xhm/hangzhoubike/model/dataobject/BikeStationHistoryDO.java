package com.xhm.hangzhoubike.model.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14-5-12
 * Time: 下午2:46
 */
public class BikeStationHistoryDO implements Serializable {
    private Long id;
    private Long stationId;
    private Integer canBeRent;//可租数量
    private Integer canBeReturn;//可还数量
    private Date logTime;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Integer getCanBeRent() {
        return canBeRent;
    }

    public void setCanBeRent(Integer canBeRent) {
        this.canBeRent = canBeRent;
    }

    public Integer getCanBeReturn() {
        return canBeReturn;
    }

    public void setCanBeReturn(Integer canBeReturn) {
        this.canBeReturn = canBeReturn;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
