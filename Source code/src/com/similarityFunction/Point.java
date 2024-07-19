package com.similarityFunction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Point  implements Comparable{
    Date time;
    double value;

    public Point( Date time, double value) {
        this.time = time;
        this.value = value;
    }
    public Point( double value) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =new Date();
        this.time = date;
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        Point p2 = (Point) o;
        if (this.getTime().after(p2.getTime())) {
            return 1;
        }
        return -1;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        return  "("+sdf.format(this.getTime())+", "+this.value+") ";
    }
}
