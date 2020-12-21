package com.hyht.amap_historical_building.utils;

public class CoordinateTransferUtil {
    private double EarthRadius = 6378137.0;
    private double e2 = 0.00669437999013;
    private double tracetarlon, tracetarlat, tracetarheight;
    private double maxHdiff;//最大高度误差阈值
    private double X0 = 0, Y0 = 0, Z0 = 0;
    double tracetarN;//卯酉圆曲率半径
    private int iterCount = 40;///迭代法求纬度的迭代次数。
    private double sintracetarlon, costracetarlon, sintracetarlat, costracetarlat;
    public CoordinateTransferUtil(double a, double b, double c) {
        this.tracetarlon = a;
        this.tracetarlat = b;
        this.tracetarheight = c;

        this.sintracetarlon = Math.sin(Math.PI * tracetarlon / 180);
        this.costracetarlon = Math.cos(Math.PI * tracetarlon / 180);
        this.sintracetarlat = Math.sin(Math.PI * tracetarlat / 180);
        this.costracetarlat = Math.cos(Math.PI * tracetarlat / 180);
        this.tracetarN = EarthRadius / (Math.sqrt(1 - e2 * Math.pow(sintracetarlat, 2)));

        /// 探测中心tracetar坐标转换为大地直角坐标
        this.X0 = (tracetarN + tracetarheight) * costracetarlat * costracetarlon;
        this.Y0 = (tracetarN + tracetarheight) * costracetarlat * sintracetarlon;
        this.Z0 = (tracetarN * (1 - e2) + tracetarheight) * sintracetarlat;
    }

/// <summary>
/// WGS-84经纬度以及高度转换为以探测中心traceTar为中心的极坐标
///
///方法：1、将空间地理坐标、tracetar坐标转换为大地直角坐标
///      2、将空间地理坐标的大地直角坐标转换为以tracetar为坐标原点的的enu（东北天）坐标//站心直角坐标
///      3、将gps相对tracetar的的enu坐标转换为极坐标。   //站心极坐标
/// </summary>
/// <param name="lon">目标经度</param>
/// <param name="lat">目标纬度</param>
/// <param name="height">目标海拔高度</param>
/// <param name="jzb[0]">斜距</param>
/// <param name="jzb[1]">方位角</param>
/// <param name="jzb[2]">俯仰角</param>
public double[] coordinate2polor(double lon, double lat, double height) {
    double[] jzb = new double[3];
    double r, a, b;
    double sinlon = Math.sin(Math.PI * lon / 180);//经度sin 参数
    double coslon = Math.cos(Math.PI * lon / 180);
    double sinlat = Math.sin(Math.PI * lat / 180);//维度sin参数
    double coslat = Math.cos(Math.PI * lat / 180);
    double N = EarthRadius / (Math.sqrt(1 - e2 * Math.pow(sinlat, 2)));//卯酉圆曲率半径

    ///1、gps 转换为大地直角坐标
    double X = (N + height) * coslat * coslon;
    double Y = (N + height) * coslat * sinlon;
    double Z = (N * (1 - e2) + height) * sinlat;
    jzb[0] = X;
    jzb[1] = Y;
    jzb[2] = Z;
    return jzb;
}
    public double[] wgs842polar(double lon, double lat, double height)
    {
        double[] jzb = new double[3];
        double r,a,b;
        double sinlon = Math.sin(Math.PI * lon / 180);//经度sin 参数
        double coslon = Math.cos(Math.PI * lon / 180);
        double sinlat = Math.sin(Math.PI * lat / 180);//维度sin参数
        double coslat = Math.cos(Math.PI * lat / 180);
        double N = EarthRadius / (Math.sqrt(1 - e2 * Math.pow(sinlat, 2)));//卯酉圆曲率半径

        ///1、gps 转换为大地直角坐标
        double X = (N + height) * coslat * coslon;
        double Y = (N + height) * coslat * sinlon;
        double Z = (N * (1 - e2) + height) * sinlat;


        ///2、大地直角坐标转换为enu坐标 即站心直角坐标
        double x = -sintracetarlon * (X - X0) + costracetarlon * (Y - Y0);
        double y = -sintracetarlat * costracetarlon * (X - X0) - sintracetarlon * sintracetarlat * (Y - Y0) + costracetarlat * (Z - Z0);
        double z = costracetarlat * costracetarlon * (X - X0) + costracetarlat * sintracetarlon * (Y - Y0) + sintracetarlat * (Z - Z0);

        ///3、站心直角坐标转换为站心极坐标。
        r = Math.sqrt(x * x + y * y + z * z);
        if (x > 0 && y > 0)
        {
            a = Math.atan(x / y) * 180 / Math.PI;

        }
        else if (x < 0 && y > 0)
        {

            a = 360 - Math.atan(Math.abs(x) / y) * 180 / Math.PI;
        }
        else if (x < 0 && y < 0)
        {
            a = 180 + Math.atan(Math.abs(x) / Math.abs(y)) * 180 / Math.PI;
        }
        else if (x > 0 && y < 0)
        {

            a = 180 - Math.atan(x / Math.abs(y)) * 180 / Math.PI;
        }
        else if (x == 0 && y > 0)
        {

            a = 0;
        }
        else if (x == 0 && y < 0)
        {
            a = 180;
        }
        else if (x > 0 && y == 0)
        {
            a = 90;
        }
        else
        {
            a = 270;
        }
        b = Math.asin(z / r) * 180 / Math.PI;
        jzb[0] = r;
        jzb[1] =a;
        jzb[2] =b;
        return jzb;


    }
    /**
     极坐标（中心坐标）转换为经纬度高度
     ///1、站心极坐标转为站心直角坐标。
     ///2、站心直角坐标系 转换为 大地直角坐标系
     ///3、大地直角坐标转换为经纬度WGS-84
     /// r—斜距， a—方位， b—俯仰角
     **/
    public double[] polar2WGS84(double r, double a, double b)
    {
        double[] jwgd= new double[3];
        double lon,lat,height;
        ///1、站心极坐标转为站心直角坐标。
        double x1 = r * Math.cos(Math.PI * b / 180) * Math.sin(Math.PI * a / 180);
        double y1 = r * Math.cos(Math.PI * b / 180) * Math.cos(Math.PI * a / 180);
        double z1 = r * Math.sin(Math.PI * b / 180);
        ///2、站心直角坐标系 转换为 大地直角坐标系
        double xe = -sintracetarlon * x1 - sintracetarlat * costracetarlon * y1 + costracetarlon * costracetarlat * z1 + X0;
        double yn = costracetarlon * x1 - sintracetarlat * sintracetarlon * y1 + costracetarlat * sintracetarlon * z1 + Y0;
        double zu = costracetarlat * y1 + sintracetarlat * z1 + Z0;
        ///3、大地直角坐标转换为经纬度WGS-84

        lon = Math.atan(yn / xe) * 180 / Math.PI;
        if (lon < 0)
            lon = 180 + lon;
        double initLat = Math.atan(zu / (Math.sqrt(xe * xe + yn * yn))) * 180 / Math.PI;
        lat = latIterate(initLat, xe, yn, zu);
        height = Math.sqrt(xe * xe + yn * yn) / Math.cos(Math.PI * lat / 180) - tracetarN;
        jwgd[0]= lon;
        jwgd[1]= lat;
        jwgd[2]= height;
        return jwgd;

    }

    public double[] coordinate2WGS84(double xe, double yn, double zu)
    {
        double[] jwgd= new double[3];
        double lon,lat,height;


        ///3、大地直角坐标转换为经纬度WGS-84

        lon = Math.atan(yn / xe) * 180 / Math.PI;
        if (lon < 0)
            lon = 180 + lon;
        double initLat = Math.atan(zu / (Math.sqrt(xe * xe + yn * yn))) * 180 / Math.PI;
        lat = latIterate(initLat, xe, yn, zu);
        height = Math.sqrt(xe * xe + yn * yn) / Math.cos(Math.PI * lat / 180) - tracetarN;
        jwgd[0]= lon;
        jwgd[1]= lat;
        jwgd[2]= height;
        return jwgd;

    }

    private double latIterate(double initLat, double x, double y, double z)
    {

        double finalLat = initLat;
        int i = 0;
        while (true)
        {
            if (i >= iterCount)
            {
                break;
            }
            double sinlat = Math.sin(Math.PI * finalLat / 180);
            double N = EarthRadius / (Math.sqrt(1 - e2 * Math.pow(sinlat, 2)));
            finalLat = Math.atan((z + N * e2 * sinlat) / Math.sqrt(x * x + y * y)) * 180 / Math.PI;
            i++;
        }
        return finalLat;
    }
}
