package com.similarityFunction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ShapeSimilarity {
    static double th = 0.00005;//斜率阈值,必须为正数

    public static void run(String dataPath) throws IOException, ParseException {
        ArrayList<Trajectory> trajectoryList = new ArrayList<Trajectory>();
        getDataFromFile(dataPath, trajectoryList);
        System.out.println("The smaller the value, the more similar it is！！！");
        for(int i=0; i<trajectoryList.size()-1; i++){
            for(int j=i+1; j<trajectoryList.size(); j++){
                double similarity = getSimilariryByTrajectorys(trajectoryList.get(i), trajectoryList.get(j));
                System.out.println((i+1)+", "+(j+1)+" similarity is: "+similarity);
            }
        }
    }


    public static void test() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d11 = sdf.parse("2021-10-24 1:00:00");        Date d12 = sdf.parse("2021-10-24 1:20:00");        Date d13 = sdf.parse("2021-10-24 1:40:00");
        Date d21 = sdf.parse("2021-10-24 1:10:00");        Date d22 = sdf.parse("2021-10-24 1:30:00");        Date d23 = sdf.parse("2021-10-24 1:50:00");

        Point p11 = new Point(d11, 0);        Point p12 = new Point(d12, 2);        Point p13 = new Point(d13, 0);
        Point p21 = new Point(d21, 2);        Point p22 = new Point(d22, 0);        Point p23 = new Point(d23, 2);
        ArrayList<Point> ps1 = new ArrayList<>();              ps1.add(p12);        ps1.add(p13);ps1.add(p11);
        ArrayList<Point> ps2 = new ArrayList<>();               ps2.add(p22);        ps2.add(p23); ps2.add(p21);

        Trajectory t1 = new Trajectory(ps1);
        Trajectory t2 = new Trajectory(ps2);

        double similarity = getSimilariryByTrajectorys(t1, t2);
        System.out.println(similarity);
        System.out.println("--------------------");
    }

    /**
     * 数据格式：轨迹id,时间戳,值 (用英文逗号分隔, 时间戳格式：yyyy-MM-dd HH:mm:ss)
     * @param filePath 数据文件的路径
     * @throws IOException
     */
    public static ArrayList<Trajectory> getDataFromFile(String filePath,  ArrayList<Trajectory> trajectoryList) throws IOException, ParseException {

        InputStreamReader read = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(read);
        String line = null;
        br.readLine();//跳过标题行
        String tid = null;
        Trajectory trajectory = new Trajectory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while((line = br.readLine()) != null) {
            String[] str = line.split(",");
//            System.out.println(line);
            if(str[0].equals(tid)){//表示当行与前一行属于同一个轨迹的点
                Date date = sdf.parse(str[1]);
                Point point = new Point(date,Double.parseDouble(str[2]));
                trajectory.add(point);
            }else {//表示是一条新轨迹
                if(tid != null){//执行第一行，此时还tid还没有数据
                    trajectoryList.add(trajectory.clone());
                    trajectory.clear();

                }
                tid = str[0];
                Date date = sdf.parse(str[1]);
                Point point = new Point(date,Double.parseDouble(str[2]));
                trajectory.add(point);
            }
        }
        if(!trajectory.isEmpty()){
            trajectoryList.add(trajectory.clone());
            trajectory.clear();
        }
        return trajectoryList;
    }

    public static double getSimilariryByTrajectorys(Trajectory t1, Trajectory t2){
        t1.sortTraj();
        t2.sortTraj();//将轨迹中的点按照时间顺序进行排序
        Date d11 = t1.getPoint(0).getTime();//轨迹1的起始时间
        Date d12 = t1.getPoint(t1.getSize()-1).getTime();//轨迹1的终止时间
        Date d21 = t2.getPoint(0).getTime();//轨迹2的起始时间
        Date d22 = t2.getPoint(t2.getSize()-1).getTime();//轨迹2的终止时间
        if(d12.before(d21) || d11.after(d22)){//两条轨迹没有交集
            return 0;
        }
        ArrayList<Trajectory> ts = padTraj(t1,t2);//补点
        Trajectory modeT1 = getModeOfTrajectory(ts.get(0));//求模式轨迹
        Trajectory modeT2 = getModeOfTrajectory(ts.get(1));
        double modeSimilarity = getModeSimilarity(modeT1, modeT2);
        return modeSimilarity;
    }
    public static double getSimilariryByTrajectorys(ArrayList<Point> list1, ArrayList<Point> list2){
        Trajectory t1 = new Trajectory(list1);
        Trajectory t2 = new Trajectory(list2);
        t1.sortTraj();
        t2.sortTraj();//将轨迹中的点按照时间顺序进行排序
        Date d11 = t1.getPoint(0).getTime();//轨迹1的起始时间
        Date d12 = t1.getPoint(t1.getSize()-1).getTime();//轨迹1的终止时间
        Date d21 = t2.getPoint(0).getTime();//轨迹2的起始时间
        Date d22 = t2.getPoint(t2.getSize()-1).getTime();//轨迹2的终止时间
        if(d12.before(d21) || d11.after(d22)){//两条轨迹没有交集
            return 0;
        }
        ArrayList<Trajectory> ts = padTraj(t1,t2);//补点
        Trajectory modeT1 = getModeOfTrajectory(ts.get(0));//求模式轨迹
        Trajectory modeT2 = getModeOfTrajectory(ts.get(1));
        double modeSimilarity = getModeSimilarity(modeT1, modeT2);
        return modeSimilarity;
    }


    /**
     * 获取两个模式轨迹的相似性
     * @param modeT1 {(时间1,模式值1)，(时间2,模式值2)}
     * @param modeT2 {(时间1,模式值1)，(时间2,模式值2)}
     * @return 两个模式轨迹之间的相似性
     */
    public static double getModeSimilarity(Trajectory modeT1, Trajectory modeT2){
        double sum = 0;
        //获取两个轨迹之间重叠的起点和终点
        int start1=-1, end1=-1, start2=-1, end2=-1;
        for(int i=1; i<modeT1.getSize()-1; i++){//第一个点（下标为0的点）是辅助点，不参与模式相似度计算
            Point p1 = modeT1.getPoint(i);

            for(int j=1; j<modeT2.getSize()-1; j++){//第一个点（下标为0的点）是辅助点，不参与模式相似度计算
                Point p2 = modeT2.getPoint(j);
                if(p1.getTime().before(p2.getTime())){//p1在p2之前
                    break;
                }else if(p1.getTime().after(p2.getTime())){
                    continue;
                }else if(p1.getTime().equals(p2.getTime())){
                    if (start1 == -1 && start2==-1){//start==-1说明之前两条轨迹没有重叠，当前是第一次
                        start1 = i;                   start2 = j;
                    }
                    if((start1!=-1 && start2!=-1) && (i==modeT1.getSize()-2 || j==modeT2.getSize()-2)){//此时说明一条轨迹已经遍历到末尾了
                        end1 = i;                     end2 = j;
                    }
                }
            }
        }
        if(end1==-1 || end2==-1){
            return 0;
        }

        //获取两个模式轨迹重叠部分的时间
        Date d11 = modeT1.getPoint(start1-1).getTime();//轨迹1中与轨迹2能够匹配部分的起始时间
        Date d12 = modeT1.getPoint(end1).getTime();//轨迹1中与轨迹2能够匹配部分的终止时间
        double totalTime = Math.abs(d12.getTime() - d11.getTime())/1000/60;//以分钟为单位

        for(int i=start1; i<=end1; i++){
            Date d21 = modeT1.getPoint(i-1).getTime();//获取前一个点的时间
            Date d22 = modeT1.getPoint(i).getTime();//获取当前点的时间

            double currentTime = Math.abs(d22.getTime() - d21.getTime())/1000/60;//以分钟为单位 获取当前时间段的持续时间
            sum += Math.abs(1.0 * (modeT1.getPoint(i).getValue() - modeT2.getPoint(start2++).getValue()) * currentTime/totalTime);
        }
        return sum;
    }

    /**
     * 基于当前轨迹，根据每一段的斜率与前一段斜率的差值，计算出轨迹的模式值
     * @param t1 轨迹中的点的格式：(时间，模式值)，模式共7种
     */
    public static Trajectory getModeOfTrajectory(Trajectory t1){
        Trajectory modeTrajectory = new Trajectory();
        Point p01 =  t1.getPoint(1);
        Point p02 = t1.getPoint(2);
        double k1 = (p02.getValue()-p01.getValue()) / ((p02.getTime().getTime()-p01.getTime().getTime())/1000/60);//此处的时间单位是分钟

        Point startPoint = new Point(t1.getPoint(0).getTime(),Double.MAX_VALUE);
        modeTrajectory.add(startPoint);

        for(int i=1; i<t1.getSize()-1; i++){
            Point p1 = t1.getPoint(i);
            Point p2 = t1.getPoint(i+1);
            double k2 = (p2.getValue()-p1.getValue()) / ((p2.getTime().getTime()-p1.getTime().getTime())/1000/60);//此处的时间单位是分钟
            Point point = null;
            if(k2<(-1*th)){//k<-th
                if(k1 == k2){
                    point = new Point(p1.getTime(),-2);
                }else if(k2 - k1 < 0) {
                    point = new Point(p1.getTime(),-3);
                }else if(k2 - k1 > 0){
                    point = new Point(p1.getTime(), -1);
                }
            }else if(k2>th){//k > th
                if(k1 == k2){
                    point = new Point(p1.getTime(),2);
                }else if(k2 - k1 > 0) {
                    point = new Point(p1.getTime(),3);
                }else  if(k2 - k1 < 0) {
                    point = new Point(p1.getTime(), 1);
                }
            }else if(k2>(-1*th) && k2<th){//k<-th && k>th
                point = new Point(p1.getTime(),0);
            }
            modeTrajectory.add(point);
            k1 = k2;
        }
        Point endPoint = new Point(t1.getPoint(t1.getSize()-1).getTime(),Double.MAX_VALUE);
        modeTrajectory.add(endPoint);
        return modeTrajectory;
    }

    public static ArrayList<Trajectory> padTraj(Trajectory t1, Trajectory t2){
        ArrayList<Trajectory> trajs = new ArrayList<>();
        Trajectory t11 = new Trajectory((ArrayList<Point>) t1.getPointList().clone());//复制t1中所有的点
        Trajectory t22 = new Trajectory((ArrayList<Point>) t2.getPointList().clone());//复制t2中所有的点
        ArrayList<Point> pAll = ((ArrayList<Point>) t1.getPointList().clone());
        pAll.addAll((ArrayList<Point>) t2.getPointList().clone());//pAll保存t1和t2的并集
        Collections.sort(pAll);//将pAll中所有的点按时间进行排序
        for(int i=0; i<pAll.size(); i++){//依次遍历pAll中所有的点
            Date date = pAll.get(i).getTime();
            for(int j=0; j<t11.getSize()-1; j++){
                Point p1 = t11.getPoint(j);
                Point p2 = t11.getPoint(j+1);
                if(date.after(t11.getPoint(t11.getSize()-1).getTime()) || date.equals(p1.getTime())){//如果当前想插入的时间点在轨迹结束之后 或者等于当前点的时间，说明不需要插入
                    break;
                }
                if(date.before(p1.getTime())){//如果当前想插入的时间点在轨迹开始之前
                    continue;
                }
                if(p1.getTime().before(date) && p2.getTime().after(date)){//如果当前点，在轨迹1中的两个点之间，说明要向轨迹1中添加该时间戳的点
                    double value = 0;
                    if(p1.getValue() > p2.getValue()){
                        value = 1.0 * (p2.getTime().getTime()-date.getTime()) / (p2.getTime().getTime()-p1.getTime().getTime()) * (p1.getValue()-p2.getValue()) + p2.getValue();
                    }else if(p1.value < p2.getValue()){
                        value = 1.0 * (date.getTime()-p1.getTime().getTime()) / (p2.getTime().getTime()-p1.getTime().getTime()) * (p2.getValue()-p1.getValue()) + p1.getValue();
                    }else {
                        value = 0;
                    }
                    Point newPoint = new Point(date,value);
                    t11.addPointByIndex(j+1, newPoint);
                }
            }
            for(int j=0; j<t22.getSize()-1; j++){
                Point p1 = t22.getPoint(j);
                Point p2 = t22.getPoint(j+1);
                if(date.after(t22.getPoint(t22.getSize()-1).getTime()) || date.equals(p1.getTime())){//如果当前想插入的时间点在轨迹结束之后 或者等于当前点的时间，说明不需要插入
                    break;
                }
                if(date.before(p1.getTime())){//如果当前想插入的时间点在轨迹开始之前
                    continue;
                }
                if(p1.getTime().before(date) && p2.getTime().after(date)){//如果当前点，在轨迹1中的两个点之间，说明要向轨迹1中添加该时间戳的点
                    double value = 0;
                    if(p1.getValue() > p2.getValue()){
                        value = 1.0 * (p2.getTime().getTime()-date.getTime())/(p2.getTime().getTime()-p1.getTime().getTime()) * (p1.getValue()-p2.getValue()) + p2.getValue();
                    }else if(p1.value < p2.getValue()){
                        value = 1.0 * (date.getTime()-p1.getTime().getTime())/(p2.getTime().getTime()-p1.getTime().getTime()) * (p2.getValue()-p1.getValue()) + p1.getValue();
                    }else {
                        value = 0;
                    }
                    Point newPoint = new Point(date,value);
                    t22.addPointByIndex(j+1, newPoint);
                }
            }
        }

        trajs.add(t11);
        trajs.add(t22);
        return trajs;
    }
}




class Trajectory {
    ArrayList<Point> pointList;

    public Trajectory() {
        this.pointList = new ArrayList<>();
    }

    public Trajectory(ArrayList<Point> pointList) {
        this.pointList = pointList;
    }
    public void add(Point point){
        this.getPointList().add(point);
    }

    public void sortTraj(){
        Collections.sort(this.getPointList());
    }

    public Point getPoint(int index){
        return this.getPointList().get(index);
    }

    public void addPointByIndex(int index, Point point){
        this.pointList.add(index, point);
    }

    public ArrayList<Point> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<Point> pointList) {
        this.pointList = pointList;
    }

    public int getSize() {
        return this.pointList.size();
    }

    public Trajectory clone(){
        Trajectory t2 = new Trajectory();
        for(int i=0; i<this.getSize(); i++){
            t2.add(new Point(this.getPoint(i).getTime(),this.getPoint(i).getValue()));
        }
        return t2;
    }

    public void clear(){
        this.getPointList().clear();
    }
    public boolean isEmpty(){
        return this.getSize()>0? false : true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        for(int i=0; i<this.getPointList().size(); i++){
            sb.append("("+sdf.format(this.getPoint(i).getTime())+", "+this.getPoint(i).value+"), ");
        }
        return sb.toString();
    }
}