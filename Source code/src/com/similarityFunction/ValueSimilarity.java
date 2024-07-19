package com.similarityFunction;

import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class ValueSimilarity {
   /* public static void main(String[] args) {
        ArrayList<Double> list1 = new ArrayList<Double>();
        list1.add(1.0);list1.add(2.0);list1.add(0.0);list1.add(1.0);list1.add(1.0);list1.add(2.0);
        ArrayList<Double> list2 = new ArrayList<Double>();
        list2.add(1.0);list2.add(0.0);list2.add(1.0);
        double dis = getValueSimilarity(list1,list2);
        System.out.println("distance = :"+dis);
    }*/

    public static double getValueSimilarity(ArrayList<Double> list1, ArrayList<Double> list2){
        double[][] path = new double[list1.size()+1][list2.size()+1];
        fillMAXValue(path);
        path[0][0] = 0;
        for(int i=0; i<list1.size(); i++){
            for(int j=0; j<list2.size(); j++){
                double cost = Math.sqrt(Math.pow((list1.get(i)-list2.get(j)),2));
                path[i+1][j+1] = cost + getMin(path[i][j+1], path[i+1][j], path[i][j]);
            }
        }
        return path[list1.size()][list2.size()];
    }



    public static void fillMAXValue(double[][] path){
        for(int i=0; i<path.length; i++){
            for (int j=0; j<path[0].length;j++){
                path[i][j] = Double.MAX_VALUE;
            }
        }
    }
    public static double getMin(double a, double b, double c){
        double min = Double.MAX_VALUE;
        min = a<b? a: b;
        min = min<c? min : c;
        return min;
    }

    public static void printArray(double[][] path){
        DecimalFormat df = new DecimalFormat("#.00");
        for(int i=1; i<path.length; i++){
            StringBuffer sb = new StringBuffer("[");
            for (int j=1; j<path[0].length-1; j++){
                sb.append(df.format(path[i][j])+", ");
            }
            sb.append(df.format(path[i][path[i].length-1])+"]");
            System.out.println(sb.toString());
        }
    }


}
