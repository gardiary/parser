package com.ef;

import com.ef.domain.BlockedIP;
import com.ef.domain.Duration;
import com.ef.domain.Param;
import com.ef.domain.ParamKey;
import com.ef.exception.ParserException;
import com.ef.util.DBUtil;
import com.ef.util.DateUtil;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gardiary on 03/04/18.
 */
public class BaseParser {

    protected File accessLog;
    protected Date startDate;
    protected Date endDate;
    protected Duration duration;
    protected Integer threshold;
    protected String ip;

    protected static Map<ParamKey, Object> parseParams(String[] args) throws ParserException {
        Map<ParamKey, Object> params = new HashMap<ParamKey, Object>();

        for (String arg : args) {

            Param param = parseParam(arg);

            if(param != null) {
                params.put(param.getKey(), param.getValue());
            }

        }

        return params;
    }


    protected static Param parseParam(String arg) throws ParserException {
        Param param = null;

        String[] keyValue = arg.split("=");

        if(keyValue.length != 2) {
            throw new ParserException("Invalid parameter [" + arg + "].");
        }

        for(ParamKey paramKey : ParamKey.values()) {

            if(keyValue[0].equalsIgnoreCase(paramKey.getKey())) {
                param = new Param();
                param.setKey(paramKey);

                if(paramKey == ParamKey.THRESHOLD) {

                    try {
                        param.setValue(Integer.parseInt(keyValue[1]));
                    } catch (Exception e) {
                        throw new ParserException("Invalid duration number [" + keyValue[1] + "].");

                    }

                } else if(paramKey == ParamKey.STARTDATE) {

                    try {
                        param.setValue(DateUtil.parse1(keyValue[1]));
                    } catch (Exception e) {
                        throw new ParserException("Invalid startDate [" + keyValue[1] + "], use format 'yyyy-MM-ss.HH:mm:ss'.");
                    }

                } else if(paramKey == ParamKey.ACCESSLOG) {

                    try {
                        File file = new File(keyValue[1]);

                        if(file.exists() && file.isFile()) {
                            param.setValue(file);
                        } else {
                            throw new ParserException("Invalid accesslog path [" + keyValue[1] + "].");
                        }
                    } catch (Exception e) {
                        throw new ParserException("Invalid accesslog path [" + keyValue[1] + "].");
                    }
                } else if(paramKey == ParamKey.DURATION) {

                    try {
                        param.setValue(Duration.valueOf(keyValue[1]));
                    } catch (Exception e) {
                        throw new ParserException("Invalid duration [" + keyValue[1] + "], use hourly or daily.");
                    }

                } else if(paramKey == ParamKey.IP) {

                    param.setValue(keyValue[1]);
                }

                break;
            }
        }

        return param;
    }

    protected void determineBlockedIPs(Map<String, Integer> countMap) {
        Map<String, Integer> blockedMap = new HashMap<String, Integer>();

        for(String key : countMap.keySet()) {
            Integer count = countMap.get(key);

            if(count >= threshold) {
                blockedMap.put(key, count);

                // store to DB
                BlockedIP blockedIP = new BlockedIP();
                blockedIP.setIp(key);
                blockedIP.setCount(count);
                blockedIP.setStartDate(startDate);
                blockedIP.setEndDate(endDate);
                blockedIP.setThreshold(threshold);
                blockedIP.setReason("Reached max threshold (" + threshold + ") with attempt " + count + " times.");

                DBUtil.insert(blockedIP);
            }

        }

        System.out.println("Found " + blockedMap.size() + " blocked IP's");

        for(String key : blockedMap.keySet()) {
            Integer count = blockedMap.get(key);
            System.out.println("Blocked IP : " + key + ", count : " + count);
        }
    }
}
