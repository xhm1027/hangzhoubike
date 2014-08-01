package com.xhm.hangzhoubike.model.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14-5-12
 * Time: 下午2:46
 */
public class BikeStationDO implements Serializable {
    private Long id;
    private Long stationId;
    private String watchStatus;//值守状态
    private String otherService;//其他服务
    private String name;//名称
    private String address;//站点地址：
    private String servicePeriod;//服务时间
    private String servicePhone;//服务电话
    private Integer canBeRent;//可租数量
    private Integer canBeReturn;//可还数量
    private String x;
    private String y;
    private Integer status;//不知道什么含义，估计是表示是否正常运营,“2”表示正常运营

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

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

    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServicePeriod() {
        return servicePeriod;
    }

    public void setServicePeriod(String servicePeriod) {
        this.servicePeriod = servicePeriod;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
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

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
