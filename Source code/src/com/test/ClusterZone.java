package com.test;
import com.similarityFunction.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ClusterZone{
    public static final double THREAD = Double.MAX_VALUE;
    public static void main(String[] args) throws IOException, ParseException {
        int zoneNum = 4;//最终的区域数量
        String[] similarityFunction = {"valueSimilarity","periodSimilarity", "shapeSimilarity"};
        ArrayList<Block> blockList = new ArrayList<>();
        //valueDataIdSwap2   periodDataIdSwap2   shapeDataIdSwap2  nanjingData22
        ArrayList<Zone> zonelist  = ClusterZone.getZones("F:\\GIS_Study\\ArcPro\\Projects\\SpatialTimeSeriesSimilarity\\人工模拟数据\\模拟计算结果\\V6\\IDEA_Similarity\\dataFile\\periodDataIdSwap2.csv",blockList);//periodDataId2   shapeDataId2   valueDataId2读取每个点的时间戳信息，文件中的信息需要已经根据点的id进行排序  shapeData zoneData
        getNeighbors("F:\\GIS_Study\\ArcPro\\Projects\\SpatialTimeSeriesSimilarity\\人工模拟数据\\模拟计算结果\\V6\\IDEA_Similarity\\dataFile\\neighbor_sythetic_sts_mx.csv",  blockList);//读取邻居文件 neighbor_nanjingcity   neighbor_city  neighbor_sythetic_sts_mx
//        check2(blockList);
        mergeZone(zonelist, zoneNum, similarityFunction[1]);
        
        String outResult = "F:\\GIS_Study\\ArcPro\\Projects\\SpatialTimeSeriesSimilarity\\人工模拟数据\\模拟计算结果\\V6\\IDEA_Similarity\\dataFile\\r\\PeriodSimiResult.csv";
        
        printZoneList(zonelist, outResult);
    }




    public static void check2(ArrayList<Block> blockList){
        for (int i=0; i<blockList.size(); i++){
            Block block1 = blockList.get(i);
//            for (int j=0; j<blockList.size(); j++) {
//                Block block2 = blockList.get(j);
//                double dis = block1.getShapeSimilarity(block2);
//                System.out.println(block1.getBlockId()+","+block2.getBlockId()+"-----"+dis);
//            }
            System.out.println("========================================================="+block1.getBlockId()+"--"+block1.getTraj().size());
        }

    }

//    public static void check(ArrayList<Block> blockList){
//        for (int i=0; i<blockList.size(); i++){
//            Block block = blockList.get(i);
//            System.out.println(block.getTraj().size());
//
//        }
//    }

    //聚类
    public static void mergeZone(ArrayList<Zone> zonelist, int zoneNum,String similarityFunction){
        while (zonelist.size()>zoneNum){
            String index = getMinDisInZoneList(zonelist, similarityFunction);
            if(index == null){
                System.out.println("-----------finish merge!!-----------"+zonelist.size());
                return;}
            int index1 = Integer.parseInt(index.split(",")[0]);
            int index2 = Integer.parseInt(index.split(",")[1]);
//            System.out.println(""+(zonelist.get(index1).getBlockByIndex(0).getBlockId())+"---"+(zonelist.get(index2).getBlockByIndex(0).getBlockId()));
            System.out.println("merge two block: "+zonelist.get(index1)+"---"+zonelist.get(index2));
            Zone zone2 = zonelist.get(index2);
            zonelist.get(index1).getBlocks().addAll(zone2.getBlocks());
            zonelist.remove(index2);
        }

    }

    //读取每个区域的时间戳信息
    public static ArrayList<Zone> getZones(String readFileName, ArrayList<Block> blockList) throws IOException, ParseException {
        HashMap<Integer, ArrayList<Point>> mapData = new HashMap<>();
        ArrayList<Zone> zoneArrayList = new ArrayList<Zone>();
        InputStreamReader read = new InputStreamReader(new FileInputStream(readFileName), "utf-8");
        BufferedReader br = new BufferedReader(read);
        String line = new String();
        br.readLine();//跳过标题行

        String id = null;
        boolean flag = true;//读取的记录是否包含时间信息，如果包含时间信息则flag为TRUE，如果没有的话flag下面程序修改为FALSE

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = sdf.parse("2022-3-29 1:00:00");
        while((line = br.readLine()) != null) {
            int j=1;
            String[] str = line.split(",");
            if(str.length>2){
                Date date = sdf.parse(str[1]);
                Point point = new Point(date,Double.parseDouble(str[2]));
                if(mapData.containsKey(Integer.parseInt(str[0]))){//当前id已经存在
                    mapData.get(Integer.parseInt(str[0])).add(point);
                }else {//当前id还不存在
                    ArrayList<Point> traj = new ArrayList<>();
                    traj.add(point);
                    mapData.put(Integer.parseInt(str[0]),traj);
                }
            }else {
                flag = false;
                Date date2 = new Date(myDate.getTime()+60000*(j++));
                Point point = new Point(date2, Double.parseDouble(str[1]));
                if(mapData.containsKey(Integer.parseInt(str[0]))){//当前id已经存在
                    mapData.get(Integer.parseInt(str[0])).add(point);
                }else {//当前id还不存在
                    ArrayList<Point> traj = new ArrayList<>();
                    traj.add(point);
                    mapData.put(Integer.parseInt(str[0]),traj);
                }
            }
        }
        br.close();
        read.close();
        Set<Integer> keyset = mapData.keySet();
//        BufferedWriter bw = new BufferedWriter(new FileWriter("dataFile\\shapeDataId22.csv"));
//        bw.write("Id,time,value");
//        bw.newLine();bw.flush();
        for (Integer iditem: keyset){
            ArrayList<Point> traj = mapData.get(iditem);
            Collections.sort(traj);
            if(flag==false){
                modifyTimeInformation(iditem, traj);
            }
            Block block = new Block(iditem,traj);
            if(!(blockList.contains(block))){
                blockList.add(block);
            }
            Zone zone = new Zone(block);
            zoneArrayList.add(zone);
        }
        return zoneArrayList;
    }

    public static void modifyTimeInformation(Integer iditem, ArrayList<Point> traj) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = sdf.parse("2022-3-29 1:00:00");
        for (int i=0; i<traj.size(); i++){
            Point p1 = traj.get(i);
            Date date2 = new Date(myDate.getTime()+60000*(i));
            p1.setTime(date2);
        }
    }
    //读取每个区域的时间戳信息
    public static ArrayList<Zone> getZones2(String readFileName, ArrayList<Block> blockList) throws IOException, ParseException {
        HashMap<Integer, ArrayList<Point>> mapData = new HashMap<>();
        ArrayList<Zone> zoneArrayList = new ArrayList<Zone>();
        InputStreamReader read = new InputStreamReader(new FileInputStream(readFileName), "utf-8");
        BufferedReader br = new BufferedReader(read);
        String line = new String();
        br.readLine();//跳过标题行

        String id = null;
        boolean flag = true;//读取的记录是否包含时间信息，如果包含时间信息则flag为TRUE，如果没有的话flag下面程序修改为FALSE

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = sdf.parse("2022-3-29 1:00:00");
        while((line = br.readLine()) != null) {
            int j=1;
            String[] str = line.split(",");
            if(str.length>2){
                Date date = sdf.parse(str[1]);
                Point point = new Point(date,Double.parseDouble(str[2]));
                if(mapData.containsKey(Integer.parseInt(str[0]))){//当前id已经存在
                    mapData.get(Integer.parseInt(str[0])).add(point);
                }else {//当前id还不存在
                    ArrayList<Point> traj = new ArrayList<>();
                    traj.add(point);
                    mapData.put(Integer.parseInt(str[0]),traj);
                }
            }else {
                flag = false;
                Date date2 = new Date(myDate.getTime()+60000*(j++));
                Point point = new Point(date2, Double.parseDouble(str[1]));
                if(mapData.containsKey(Integer.parseInt(str[0]))){//当前id已经存在
                    mapData.get(Integer.parseInt(str[0])).add(point);
                }else {//当前id还不存在
                    ArrayList<Point> traj = new ArrayList<>();
                    traj.add(point);
                    mapData.put(Integer.parseInt(str[0]),traj);
                }
            }
        }
        br.close();
        read.close();
        Set<Integer> keyset = mapData.keySet();
        BufferedWriter bw = new BufferedWriter(new FileWriter("dataFile\\shapeDataId22.csv"));
        bw.write("Id,time,value");
        bw.newLine();bw.flush();
        for (Integer iditem: keyset){
            ArrayList<Point> traj = mapData.get(iditem);
            Collections.sort(traj);
            if(flag==false){
                modifyTimeInformation2(iditem, traj, bw);
            }
            Block block = new Block(iditem,traj);
            if(!(blockList.contains(block))){
                blockList.add(block);
            }
            Zone zone = new Zone(block);
            zoneArrayList.add(zone);
        }
        bw.close();
        return zoneArrayList;
    }
    public static void modifyTimeInformation2(Integer iditem, ArrayList<Point> traj,BufferedWriter bw) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = sdf.parse("2022-3-29 1:00:00");
        for (int i=0; i<traj.size(); i++){
            Point p1 = traj.get(i);
            Date date2 = new Date(myDate.getTime()+60000*(i));
            p1.setTime(date2);
            bw.write(iditem+","+sdf.format(date2)+","+p1.getValue());
            bw.newLine();
            bw.flush();
        }
    }

    //读取邻居信息
    public static void getNeighbors(String readFileName, ArrayList<Block> blockList)throws IOException{
        InputStreamReader read = new InputStreamReader(new FileInputStream(readFileName), "utf-8");
        BufferedReader br = new BufferedReader(read);
        String line = new String();
        while((line = br.readLine()) != null) {
//            System.out.println("neighbors informations: " + line);
            String[] str = line.split(",");
            int id = Integer.parseInt(str[0]);

            Block block = getBlockByIdFromBlockList(blockList,id);
            if (block == null){
                continue;
            }
            for(int i=1; i<str.length; i++){
                Block p = getBlockByIdFromBlockList(blockList,Integer.parseInt(str[i]));
                if (p == null){
                    continue;
                }
                block.addNeighbor(p);
            }
//            System.out.println("this block: "+block.getBlockId()+", neighbors: "+block.getNeighbors());
        }
    }

    public static Block getBlockByIdFromBlockList(ArrayList<Block> blockList, int id){
        for (int i=0; i<blockList.size(); i++){
            Block block = blockList.get(i);
            if(block.getBlockId() == id){
                return block;
            }
        }
        return  null;
    }

    //获取两个区域集合中所有区域距离的最大值
    public static String getMinDisInZoneList(ArrayList<Zone> zonelist ,String similarityFunction){
        int index1=-1, index2=-1;
        double minDis = Double.MAX_VALUE;
        for(int i=0; i<zonelist.size()-1; i++){
            for(int j=i+1; j<zonelist.size(); j++){
                Zone zone1 = zonelist.get(i);
                Zone zone2 = zonelist.get(j);
                if(zone1.isNeighbor(zone2)){
//                    double dis = zone1.getAveDisToZone(zone2);//获取两个区域中点之间相似性的平均值
//                    double dis = zone1.getMinDisToZone(zone2);//获取两个区域中点之间相似性的最小值
//                    System.out.println(zone1.getBlockByIndex(0).getBlockId()+"------"+zone2.getBlockByIndex(0).getBlockId());
                    double dis = zone1.getMaxDisToZone(zone2, similarityFunction);//获取两个区域中点之间相似性的最大值
//                    dis = dis * (Math.pow(zone1.getBlocks().size(),1.0/2)*Math.pow(zone2.getBlocks().size(),1.0/2));
                    dis = dis * (Math.pow(zone1.getBlocks().size(),1.0/2)*Math.pow(zone2.getBlocks().size(),1.0/2));
//                    System.out.println(""+(index1+1)+"--"+(index2+1)+"---"+minDis);
//                    System.out.println(zone1.getBlockByIndex(0).getBlockId()+"------"+zone2.getBlockByIndex(0).getBlockId()+"---------"+dis);
                    if (dis<minDis){
                        index1 = i;
                        index2 = j;
                        minDis = dis;
                    }
                }
            }
        }
//        System.out.println(""+(index1+1)+"--"+(index2+1)+"------"+minDis);
//        System.out.println(zonelist.get(index1).getBlockByIndex(0).getBlockId()+",========"+zonelist.get(index2).getBlockByIndex(0).getBlockId()+"------"+minDis);
        if(minDis<THREAD){
            return index1+","+index2;
        }
        return null;
    }

    public static void printZoneList(ArrayList<Zone> zoneList, String resultFile) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
        bw.write("clusterId,zoneId");
        bw.newLine();
        int clusterId = 1;
        System.out.println("\nprint all blocks: ");
        for (int i=0; i<zoneList.size(); i++){
            System.out.println(zoneList.get(i));
            ArrayList<Block> blocks = zoneList.get(i).getBlocks();
            for (int j=0; j<blocks.size(); j++){
                bw.write(clusterId+","+blocks.get(j).getBlockId());
                bw.newLine();
            }
            bw.flush();
            clusterId++;
        }
        bw.close();
    }


}
