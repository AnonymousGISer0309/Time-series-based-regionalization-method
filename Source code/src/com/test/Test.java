package com.test;
import com.similarityFunction.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException, IOException {
//        System.out.println(Math.pow(81,1.0/4));
        changeData();

    }

    public static void changeData() throws IOException, ParseException {
        InputStreamReader read = new InputStreamReader(new FileInputStream("E:\\datas\\nanjingData11.csv"), "utf-8");
        BufferedReader br = new BufferedReader(read);
        BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\datas\\nanjingData22.csv"));//zoneData2
        String line = new String();

        while((line = br.readLine()) != null) {
            String[] str = line.split(",");
            for (int i=1; i<str.length; i++){
                bw.write(str[0]+","+str[i]);
                bw.newLine();
                bw.flush();
            }
        }
        bw.close();
        br.close();
        read.close();
    }
}
