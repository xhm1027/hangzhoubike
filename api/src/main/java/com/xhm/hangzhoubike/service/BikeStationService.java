package com.xhm.hangzhoubike.service;

import com.alibaba.pt.commons.persistence.page.Page;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.model.dataobject.BikeStationHistoryDO;

import java.util.List;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14-3-31
 * Time: 上午10:26
 */
public interface BikeStationService {
    /**
     * 
     * @return
     */
    public String hello();

    /**
     * 根据坐标查找站点
     * @param range 范围
     * @param x 经度
     * @param y 纬度
     * @return
     */
    public List<BikeStationDO> queryBikeStationByLocation(String range, String x,String y);

    /**
     * 根据名称查找站点
     * @param name
     * @return
     */
    public List<BikeStationDO> queryBikeStationByName(String name);

    /**
     * 根据名称查找站点page
     * @param name
     * @param page
     * @return
     */
    public List<BikeStationDO> queryBikeStationPageByName(String name,int page);


    /**
     * 
     * @param page
     * @param pageSize
     * @return
     */
    public Page<BikeStationDO> queryBikeStationPageInDB(int page,int pageSize);


    /**
     * 
     * @param stationId
     * @return
     */
    public BikeStationDO queryBikeStationByStationId(Long stationId);


    /**
     * 
     * @param query
     * @return
     */
    public List<BikeStationHistoryDO> queryHistory(BikeStationHistoryDO query);
}
