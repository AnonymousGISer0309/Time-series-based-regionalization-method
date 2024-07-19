package com.test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Zone表示一个包含多个子区域的一个区域集合
 */
public class Zone {
    private ArrayList<Block> blocks;

    public Zone(Block block) {
        this.blocks = new ArrayList<>();
        blocks.add(block);
    }

    //获取两个区域中点之间距离的最小值，相似性的最大值
    public double getMaxDisToZone(Zone zone, String similarityFunction){
        double maxDis = -1;
        for(int i=0; i<this.size(); i++){
            Block block1 = this.getBlockByIndex(i);
            for(int j=0; j<zone.size(); j++){
                Block block2 = zone.getBlockByIndex(j);
                double dis = Double.MAX_VALUE;
                switch (similarityFunction){
                    case "valueSimilarity":
                        dis = block1.getValueSimilarity(block2);break;
                    case "periodSimilarity":
                        dis = block1.getPeriodSimilarity(block2);break;
                    case "shapeSimilarity":
                        dis = block1.getShapeSimilarity(block2);break;
                }
                maxDis = dis>maxDis? dis : maxDis;
            }
        }
//        System.out.println("maxDis = "+maxDis);
        return maxDis;
    }


    //获取两个区域中点之间相似性的最小值
    public double getMinDisToZone(Zone zone){
        double minDis = Double.MIN_VALUE;
        for(int i=0; i<this.size(); i++){
            Block block1 = this.getBlockByIndex(i);
            for(int j=0; j<zone.size(); j++){
                Block block2 = zone.getBlockByIndex(j);
                double dis = block1.getAveDisToBlock(block2);
                minDis = dis<minDis? dis : minDis;
            }
        }
        return minDis;
    }


    //获取两个区域中点之间的相似性的平均值
    public double getAveDisToZone(Zone zone){
        double sum = 0;
        for(int i=0; i<this.size(); i++){
            Block block1 = this.getBlockByIndex(i);
            for(int j=0; j<zone.size(); j++){
                Block block2 = zone.getBlockByIndex(j);
                double dis = block1.getAveDisToBlock(block2);
                sum += dis;
            }
        }
        return sum/(this.size()*zone.size());
    }



   //判断两个zone是不是相邻
    public boolean isNeighbor(Zone zone){
        HashSet<Block> blockSet = new HashSet<>();
        for(int i=0; i<this.size(); i++){
            blockSet.addAll(this.getBlockByIndex(i).getNeighbors());
        }
        ArrayList<Block> blockList1 = new ArrayList<>();
        blockList1.addAll(blockSet);
        blockList1.retainAll(zone.getBlocks());
        if(blockList1.size()>0){
            return true;
        }
        return false;
    }



    public Block getBlockByIndex(int i){
        return this.getBlocks().get(i);
    }

    public void addBlock(Block p){
        this.blocks.add(p);
    }

    public int size(){
        return this.blocks.size();
    }


    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "blocks=" + blocks +
                '}';
    }
}
