package com.test;

import java.io.*;
import java.text.ParseException;

public class Test2 {
    public static void main(String[] args) throws IOException, ParseException {
        changeData2();
    }
    public static void changeData2() throws IOException, ParseException {
        InputStreamReader read = new InputStreamReader(new FileInputStream("E:\\datas\\nanjingData.csv"), "utf-8");
        BufferedReader br = new BufferedReader(read);
        BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\datas\\nanjingData2.csv"));//zoneData2
        String line = new String();
        br.readLine();//跳过标题行
        int num = 1;
        bw.write("1,");
        while((line = br.readLine()) != null) {
            String[] str = line.split(",");
            int id = Integer.parseInt(str[0]);
            if(id == num){
                bw.write(str[1]+",");
            }else {
                num = id;
                bw.newLine();
                bw.flush();
                bw.write(id+","+str[1]+",");
            }


        }
        bw.newLine();
        bw.flush();
        bw.close();
        br.close();
        read.close();
    }
}
