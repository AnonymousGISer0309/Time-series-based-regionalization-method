package com.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class Main {
    public static final double THREAD = 14000;
    public static void main(String[] args) throws IOException, ParseException {
        int zoneNum = 4;//最终的区域数量
        String[] similarityFunction = {"valueSimilarity","periodSimilarity", "shapeSimilarity"};
        ArrayList<Block> blockList = new ArrayList<>();
        ArrayList<Zone> zonelist  = ClusterZone.getZones("dataFile\\shapeData.csv",blockList);//zoneData读取每个点的时间戳信息，文件中的信息需要已经根据点的id进行排序  shapeData
        ClusterZone.getNeighbors("dataFile\\neighbor.csv", blockList);//读取邻居文件
        ClusterZone.mergeZone(zonelist, zoneNum, similarityFunction[0]);//选择相似度算法
        ClusterZone.printZoneList(zonelist, "");//输出结果
    }
}
