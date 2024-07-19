package com.test;
import com.similarityFunction.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Date;


/**
 * Block表示一个小区域
 */
public class Block {
    private int blockId;
    private ArrayList<Point> traj;
    private ArrayList<Block> neighbors;



    public Block(int blockId, ArrayList<Point> traj) {
        this.blockId = blockId;
        this.traj = traj;
        this.neighbors = new ArrayList<>();
    }





    public void addNeighbor(Block block){
        this.getNeighbors().add(block);
    }

    //获取两个点之间的距离，取每个时间戳特征差的平均值
    public double getAveDisToBlock(Block block){
        double sum = 0;
        for(int i=0; i<this.getTraj().size(); i++){
            sum += Math.abs(this.getTraj().get(i).getValue() - block.getTraj().get(i).getValue());
        }
        return sum/this.getTraj().size();
    }


    public double getPeriodSimilarity(Block block){
        double similarity = PeriodSimilarity.getSimilarity(this.getTrajDouble(), block.getTrajDouble());
        return similarity;
    }

    public double getValueSimilarity(Block block){
        double dis = ValueSimilarity.getValueSimilarity(this.getTrajDouble(), block.getTrajDouble());
        return dis;

    }

    public double getShapeSimilarity(Block block){
        double dis = ShapeSimilarity.getSimilariryByTrajectorys((this.getTraj()), block.getTraj());
        return dis;
    }





    @Override
    public String toString() {
        return "{" +blockId +
//                ", traj=" + traj +
//                ", neighbors=" + neighbors +
                '}';
    }

    public int getBlockId() {
        return blockId;
    }

    public ArrayList<Point> getTraj() {
        return traj;
    }

    public ArrayList<Double> getTrajDouble(){
        ArrayList<Double> list = new ArrayList<>();
        for(int i=0; i<this.traj.size(); i++){
            list.add(this.traj.get(i).getValue());
        }
        return list;
    }

    public ArrayList<Block> getNeighbors() {
        return neighbors;
    }
}
