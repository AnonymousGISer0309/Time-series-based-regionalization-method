package com.similarityFunction;

import java.util.ArrayList;

public class PeriodSimilarity {
    public static final double PI = 3.14;

    public static double[] listToArray(ArrayList<Double> list){
        int len = list.size();
        double[] array = new double[len];
        for (int i=0; i<len; i++){
            array[i] = list.get(i);
        }
        return array;
    }

    public static double getSimilarity(ArrayList<Double> list1, ArrayList<Double> list2){
        double[] arr1 = listToArray(list1);
        double[] arr2 = listToArray(list2);
        int T1 = arr1.length;
        double[] coefficient1 = new double[T1];
        getCoefficient(arr1, coefficient1);
        double[] coefficient2 = new double[T1];
        getCoefficient(arr2, coefficient2);
        int n = (int)((T1-1)/2);
        double similarity = -1;
        double sum = 0;//每个n值的和
        for(int i=0; i<n; i++){
            double lamtak = 2*PI*i/T1; //当前n值的lamta
            for(int j=0; j<T1; j++){
                if(j==0){
                    sum += Math.pow((coefficient1[j] - coefficient2[j]), 2);
                }else {
                    sum += Math.pow(((coefficient1[j]-coefficient2[j]) * Math.cos(lamtak * j) * 2), 2);
                }
            }
        }
        similarity = Math.pow(sum, 0.5) / n;
        return similarity;
    }

    /**
     * 获取系数数组
     * @param arr 表示X_T
     * @return X系数数组
     */
    public static double[] getCoefficient(double[] arr, double[] coefficient){
        int length = arr.length;
        for(int i=0; i<length; i++){
            double sumRow = 0;
            for(int j=0; j<length-i;j++){
                sumRow += arr[j+i]* arr[j];
//                System.out.print(arr[j+i] + ", " + arr[j]+"\t");
            }
            coefficient[i] = sumRow;
//            System.out.println(i+", "+sumRow);
        }
        return coefficient;
    }

}
