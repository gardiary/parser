package com.ef;

import com.ef.domain.*;
import com.ef.exception.ParserException;
import com.ef.util.DBUtil;
import com.ef.util.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by gardiary on 02/04/18.
 *
 * This parser running slow for file contains hundred-thousands of logs and need to store it to DB first.
 * Have a look {@link ParserTwo} without storing the logs to DB.
 */
public class Parser extends BaseParser {

    public Parser() {
    }

    public Parser(File accessLog, Date startDate, Duration duration, Integer threshold, String ip) {
        this.accessLog = accessLog;
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
        this.ip = ip;

        if(duration == Duration.hourly) {
            this.endDate = DateUtil.addHours(startDate, 1);
        } else if(duration == Duration.daily) {
            this.endDate = DateUtil.addDays(startDate, 1);
        }

    }

    public void parse() throws ParserException {
        System.out.println("Start parsing..");

        if(accessLog == null) {
            throw new ParserException("Access log file required.");
        }


        String jobId = UUID.randomUUID().toString();    // generate random jobId

        loadLogsToDB(jobId);    // load logs to DB

        parseLogs(jobId);   // search and parse logs based on criteria/parameters and jobId

        if(ip != null) {
            parseLogsByIP(jobId);
        }

        System.out.println("Done parsing..");
    }

    public void loadLogsToDB(String jobId) throws ParserException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(accessLog));
            try {
                List<Log> logs = new ArrayList<Log>();

                String line = br.readLine();
                Integer row = 0;

                while (line != null) {
                    row++;

                    String arrLine[] = line.split("\\|");

                    if(arrLine.length >= 5) {
                        try {
                            Date date = DateUtil.parse2(arrLine[0]);
                            String ip = arrLine[1];
                            String request = arrLine[2];
                            Integer status = Integer.parseInt(arrLine[3]);
                            String userAgent = arrLine[4];

                            Log log = new Log(date, ip, request, status, userAgent, jobId);

                            //this method running slow when need to save hundred-thousands of records one by one
                            //DBUtil.insert(log);

                            logs.add(log);

                        } catch (Exception e) {
                            System.err.println("Invalid input line [" + line + "]");
                            e.printStackTrace();
                        }
                    }

                    line = br.readLine();
                }

                System.out.println("Found " + logs.size() + " log(s).");

                DBUtil.insertBatch(logs);
                System.out.println(logs.size() + " log(s) inserted to database.");

            } finally {
                br.close();
            }

        } catch(Exception e) {
            throw new ParserException(e.getMessage());
        }
    }

    public void parseLogs(String jobId) {

        List<Log> filteredLogs = DBUtil.searchLogs(jobId, startDate, endDate);

        System.out.println("Found " + filteredLogs.size() + " log(s)");

        Map<String, Integer> countMap = new HashMap<String, Integer>();

        for(Log log : filteredLogs) {
            if(countMap.containsKey(log.getIp())) {
                countMap.put(log.getIp(), countMap.get(log.getIp()) + 1);
            } else {
                countMap.put(log.getIp(), 1);
            }
        }

        System.out.println("Found " + countMap.size() + " IP's");

        if(countMap.size() > 0) {
            determineBlockedIPs(countMap);
        }

    }

    public void parseLogsByIP(String jobId) {

        List<Log> filteredLogs = DBUtil.searchLogs(jobId, ip);

        System.out.println("Found " + filteredLogs.size() + " log(s) for ip " + ip);

        // not really sure what to do

    }


    @Override
    public String toString() {
        return "[" +
                "accessLog=" + accessLog +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                ", threshold=" + threshold +
                ", ip=" + ip +
                ']';
    }

    public static void main(String args[]) {

        try {

            if (args.length > 0) {
                Map<ParamKey, Object> params = parseParams(args);

                System.out.println("Parameters : " + params);

                if(params.size() >= 4) {

                    Parser parser = new Parser(
                            (File) params.get(ParamKey.ACCESSLOG),
                            (Date) params.get(ParamKey.STARTDATE),
                            (Duration) params.get(ParamKey.DURATION),
                            (Integer) params.get(ParamKey.THRESHOLD),
                            (params.containsKey(ParamKey.IP) ? (String) params.get(ParamKey.IP) : null));

                    parser.parse();
                } else {
                    System.out.println("Please supply these parameters: --accesslog=/path/to/file --startDate=yyyy-MM-ss.HH:mm:ss --duration=hourly|daily --threshold=NUMBER --ip=IP(OPTIONAL).");
                }

            } else {
                System.out.println("Please supply these parameters: --accesslog=/path/to/file --startDate=yyyy-MM-ss.HH:mm:ss --duration=hourly|daily --threshold=NUMBER --ip=IP(OPTIONAL).");
            }

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        DBUtil.closeDBConnection();
        System.exit(0);
    }


}
