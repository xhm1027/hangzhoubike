package com.xhm.hangzhoubike.service.impl;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.pt.commons.persistence.page.Page;
import com.xhm.hangzhoubike.dao.BikeStationDao;
import com.xhm.hangzhoubike.dao.BikeStationHistoryDao;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.model.dataobject.BikeStationHistoryDO;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <P></P>
 * Date: 14-3-31
 * Time: 上午10:29
 */
@Component("demoService")
public class BikeStationServiceImpl implements BikeStationService {
    Logger logger = Logger.getLogger("serviceLogger");
    private final static String REQUEST_URL_LOCATION = "http://www.hzbus.cn/Page/NearbyBicyle.aspx";
    private final static String REQUEST_URL_QUERYNAME = "http://www.hzbus.cn/Page/BicyleSquare.aspx";

    @Resource
    BikeStationDao bikeStationDao;

    @Resource
    BikeStationHistoryDao bikeStationHistoryDao;
    
    @Override
    public String hello() {
        return "this is demo service";
    }

    @Override
    public List<BikeStationDO> queryBikeStationByLocation(String range, String x, String y) {
        try {
            if(StringUtil.isBlank(range)){
                range= "500";
            } logger.info("query by NearbyBicyle:range="+range+",x="+x+"y="+y);
            Document doc = Jsoup.connect(REQUEST_URL_LOCATION).timeout(10000)
                    .data("w", range).data("x", x).data("y", y).get();
            return getBikeStationList(getResult(doc));
        } catch (Exception e) {
            logger.error("queryBikeStationByLocation fail",e);
        }
        return new ArrayList<BikeStationDO>();
    }

    @Override
    public List<BikeStationDO> queryBikeStationByName(String name) {
        if (StringUtil.isNotBlank(name)) {
            int index = 1;
            Long stationId = null;
            List<BikeStationDO> list = new ArrayList<BikeStationDO>();
            while(true){
                List<BikeStationDO> l = fetchBikeStationList(name,index);
               
//                    已经是最后一页的情况判断如下：
//                    1、这一页为空；
//                    2、这一页第一个站点的id和记录的id相同；
                if(l==null||l.size()==0){
                    break;
                }
                if(stationId!=null&&stationId.longValue()==l.get(0).getStationId()){
                    break;
                }
                list.addAll(l);
                stationId = l.get(0).getStationId();
                index++;
            }
            return list;
        }
        return new ArrayList<BikeStationDO>();
    }
    
    private List<BikeStationDO> fetchBikeStationList(String name,int index){
        logger.info("query by BicyleSquare:nm="+name+",__EVENTARGUMENT="+index);
        try {
            Document doc = Jsoup.connect(REQUEST_URL_QUERYNAME)
                    .timeout(10000)
                    .data("nm", name)
                    .data("area", "-1")
                    .data("rnd", "2")
                    .data("__EVENTARGUMENT", ""+index)
                    .data("__EVENTTARGET", "AspNetPager1")
                    .get();

            List<BikeStationDO> l = getBikeStationList(getResult(doc));
            return l;
        } catch (IOException e) {
            logger.error("fetchBikeStationList fail", e);
            return null;
        }
    }

    @Override
    public List<BikeStationDO> queryBikeStationPageByName(String name, int page) {
        if(page<1){
            page = 1;
        }
        return fetchBikeStationList(name,page);
    }

    @Override
    public Page<BikeStationDO> queryBikeStationPageInDB(int page, int pageSize) {
        Page<BikeStationDO> stationDOPage= null;
        try{
            stationDOPage= bikeStationDao.page("page",new BikeStationDO(),page*pageSize,pageSize);
        }catch (Exception e){
            stationDOPage= bikeStationDao.page("page",new BikeStationDO(),page*pageSize,pageSize);
        }
        return stationDOPage;
    }

    @Override
    public BikeStationDO queryBikeStationByStationId(Long stationId) {
        BikeStationDO query = new BikeStationDO();
        query.setStationId(stationId);
        List<BikeStationDO> bikeStationList = null;
        try{
            bikeStationList = bikeStationDao.find(query);
        }catch (Exception e){
            bikeStationList = bikeStationDao.find(query);
        }
        if(bikeStationList==null||bikeStationList.size()==0||bikeStationList.size()>1){
            return null;
        }
        return bikeStationList.get(0);
    }

    @Override
    public List<BikeStationHistoryDO> queryHistory(BikeStationHistoryDO query) {
        List<BikeStationHistoryDO> historyDOList = null;
        try{
            historyDOList = bikeStationHistoryDao.find(query);
        }catch (Exception e){
            historyDOList = bikeStationHistoryDao.find(query);
        }
        return historyDOList;
    }

    private List<BikeStationDO> getBikeStationList(List<String[]> list){
        List<BikeStationDO> bikeStationList = new ArrayList<BikeStationDO>();
        if(list==null&&list.size()==0){
            return bikeStationList;
        }
        for(String[] array: list){
            try{
                if(array.length!=16){
                    continue;
                }
                BikeStationDO bikeStation = new BikeStationDO();
                bikeStation.setWatchStatus(array[0]);
                bikeStation.setOtherService(array[1]);
                bikeStation.setName(array[2]);
                bikeStation.setAddress(array[3]);
                bikeStation.setServicePeriod(array[4]);
                bikeStation.setServicePhone(array[5]);
                String canBeRent = array[6].replaceAll(" ","");
                if(StringUtil.isNumeric(canBeRent)){
                    bikeStation.setCanBeRent(Integer.valueOf(canBeRent));
                }
                String canBeReturn= array[7].replaceAll(" ","");
                if(StringUtil.isNumeric(canBeReturn)){
                    bikeStation.setCanBeReturn(Integer.valueOf(canBeReturn));
                }
                bikeStation.setX(array[8]);
                bikeStation.setY(array[9]);
                String status= array[14].replaceAll(" ","");
                if(StringUtil.isNumeric(status)){
                    bikeStation.setStatus(Integer.valueOf(status));
                }
                String id = array[2].split(" ")[0];
                id = id.replaceAll("№", "");
                if(StringUtil.isBlank(id)){
                    continue;
                }
                bikeStation.setStationId(Long.valueOf(id));

                bikeStationList.add(bikeStation);

            }catch (Exception e){
                logger.error("get do fail,String[]="+array,e);
            }
        }
//        saveDB(bikeStationList);
        return bikeStationList;
    }
    
    private void saveDB(List<BikeStationDO> list){
        if(list==null){
            return;
        }
        for(BikeStationDO bikeStationDO : list){
//            BikeStationDO dbDO = bikeStationDao.load(bikeStationDO.getStationId());
//            if(dbDO==null){
                bikeStationDao.create(bikeStationDO);
//            }else{
//                bikeStationDao.update(bikeStationDO);
//            }
        }
    }

    private List<String[]> getResult(Document doc) {
        Iterator<Element> lis = doc.select("#dvInit li.bt").iterator();
        List<String[]> list = new ArrayList<String[]>();
        while (lis.hasNext()) {
            Element li = lis.next();
            String regex = "\'.*?\'";
            Pattern pt = Pattern.compile(regex);
            String result = li.attr("onclick");
            Matcher m = pt.matcher(result);
            int i = 0;
            String[] l = new String[16];
            while (m.find() && i < 16) {
                String s = unescape(m.group());
                l[i] = s.substring(1, s.length() - 1);
                i++;
            }
            list.add(l);
        }
        return list;
    }

    private String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }


    public static void main(String[] args) {
        BikeStationServiceImpl service = new BikeStationServiceImpl();
//        List<BikeStationDO> arrayList = service.queryBikeStationByLocation("500", "120.16400115634164","30.23913689644955");
         List<BikeStationDO> arrayList = service.queryBikeStationByName("武林小广场");
        System.out.println(JSON.toJSONString(arrayList));
    }
}
