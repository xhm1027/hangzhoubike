package com.xhm.hangzhoubike.task;

import com.alibaba.fastjson.JSON;
import com.xhm.hangzhoubike.dao.BikeStationDao;
import com.xhm.hangzhoubike.dao.BikeStationHistoryDao;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.model.dataobject.BikeStationHistoryDO;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14/8/2
 * Time: 下午11:04
 */
@Component("refreshStationTask")
public class RefreshStationTask {
    Logger logger = Logger.getLogger("taskLogger");
    @Resource
    BikeStationService bikeStationService;

    @Resource
    BikeStationDao bikeStationDao;
    @Resource
    BikeStationHistoryDao bikeStationHistoryDao;

    @Scheduled(cron = "0 0 * * * ?")
    public void refresh(){
        long start = System.currentTimeMillis();
        Date logTime = new Date(start);
        Map<Long,BikeStationDO> map = new HashMap<Long,BikeStationDO>();
        logger.info("refresh start!");
        for(int i=0;i<10;i++)
        {
            List<BikeStationDO> list = bikeStationService.queryBikeStationByName("" + i);
            if(list==null){
                continue;
            }
            for(BikeStationDO bikeStationDO : list){
                map.put(bikeStationDO.getStationId(),bikeStationDO);
            }
        }
        //save db
        for(BikeStationDO bikeStationDO : map.values()){
            try{
                bikeStationDao.create(bikeStationDO);
            }catch (Exception e){
                logger.error("create bikestation fail:"+ JSON.toJSONString(bikeStationDO),e);
            }
            try{
                BikeStationHistoryDO historyDO = new BikeStationHistoryDO();
                historyDO.setStationId(bikeStationDO.getStationId());
                historyDO.setCanBeRent(bikeStationDO.getCanBeRent());
                historyDO.setCanBeReturn(bikeStationDO.getCanBeReturn());
                historyDO.setLogTime(logTime);
                bikeStationHistoryDao.create(historyDO);
            }catch (Exception e){
                logger.error("create bikeStationHistory fail:"+ JSON.toJSONString(bikeStationDO),e);
            }
        }
        long end = System.currentTimeMillis();
        long period = end -start;
        logger.info("refresh end,fresh station:"+map.size()+",use "+ period + "ms.");
    }
}
