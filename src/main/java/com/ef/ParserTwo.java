package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import com.ef.domain.*;
import com.ef.exception.ParserException;
import com.ef.util.DBUtil;
import com.ef.util.DateUtil;

/**
 * Created by gardiary on 02/04/18.
 *
 * This parser directly checks IP's without loading the logs into database
 */
public class ParserTwo extends BaseParser {

    public ParserTwo() {
    }

    public ParserTwo(File accessLog, Date startDate, Duration duration, Integer threshold, String ip) {
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

        try {
            BufferedReader br = new BufferedReader(new FileReader(accessLog));
            try {
                String line = br.readLine();
                Integer row = 0;

                Map<String, Integer> countMap = new HashMap<String, Integer>();

                while (line != null) {
                    row++;

                    String arrLine[] = line.split("\\|");

                    if(arrLine.length > 1) {
                        try {
                            Date date = DateUtil.parse2(arrLine[0]);
                            String ip = arrLine[1];

                            if(date.after(startDate) && date.before(endDate)) {
                                if(countMap.containsKey(ip)) {
                                    countMap.put(ip, countMap.get(ip) + 1);
                                } else {
                                    countMap.put(ip, 1);
                                }
                            }

                        } catch (Exception e) {
                            System.err.println("Invalid input line [" + line + "]");
                            e.printStackTrace();
                        }
                    }

                    line = br.readLine();
                }

                System.out.println("Found " + row + " row(s)");
                System.out.println("Found " + countMap.size() + " IP's:");

                if(countMap.size() > 0) {
                    determineBlockedIPs(countMap);
                }

            } finally {
                br.close();
            }


        } catch(Exception e) {
            throw new ParserException(e.getMessage());
        }

        System.out.println("Done parsing..");
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

                    ParserTwo parser = new ParserTwo(
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
