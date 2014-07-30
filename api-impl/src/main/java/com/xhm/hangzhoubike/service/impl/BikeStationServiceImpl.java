package com.xhm.hangzhoubike.service.impl;

import com.alibaba.common.lang.StringUtil;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14-3-31
 * Time: 上午10:29
 */
@Component("demoService")
public class BikeStationServiceImpl implements BikeStationService {
    private static Logger logger = LoggerFactory.getLogger(BikeStationServiceImpl.class);
    private final static String REQUEST_URL_LOCATION = "http://www.hzbus.cn/Page/NearbyBicyle.aspx";
    private final static String REQUEST_URL_QUERYNAME = "http://www.hzbus.cn/Page/BicyleSquare.aspx";
    @Override
    public String hello() {
        return "this is demo service";
    }

    @Override
    public List<BikeStationDO> queryBikeStationByLocation(String range, String x, String y) {
        try {
            if(StringUtil.isBlank(range)){
                range= "500";
            }
            Document doc = Jsoup.connect(REQUEST_URL_LOCATION).timeout(10000)
                    .data("w", range).data("x", x).data("y", y).get();
            return getBikeStationList(getResult(doc));
        } catch (IOException e) {
            logger.error("queryBikeStationByLocation fail",e);
        }
        return new ArrayList<BikeStationDO>();
    }

    @Override
    public List<BikeStationDO> queryBikeStationByName(String name) {
        if (StringUtil.isNotBlank(name)) {
            try {
                Document doc = Jsoup.connect(REQUEST_URL_QUERYNAME)
                        .timeout(10000).data("nm", name).data("area", "-1").get();
                return getBikeStationList(getResult(doc));
            } catch (IOException e) {
                logger.error("queryBikeStationByName fail", e);
            }
        }
        return new ArrayList<BikeStationDO>();
    }

    private List<BikeStationDO> getBikeStationList(List<String[]> list){
        List<BikeStationDO> bikeStationList = new ArrayList<BikeStationDO>();
        if(list==null&&list.size()==0){
            return bikeStationList;
        }
        for(String[] array: list){
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
            bikeStation.setId(Long.valueOf(id.replaceAll("№","")));

            bikeStationList.add(bikeStation);
        }

        return bikeStationList;
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
}