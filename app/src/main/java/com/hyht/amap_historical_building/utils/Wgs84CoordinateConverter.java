package com.hyht.amap_historical_building.utils;

public class Wgs84CoordinateConverter {

    public double[] BLToGauss(double B,double L)
    {
        //单位皆为弧度
        int width=6;   //6度分带投影，由此来确定中央子午线经度
        int zonenum=(int)L/width+1;
        double L0=(zonenum-0.5)*width;   //中央子午线经度
        double ipi=Math.PI/180;
        B=B*ipi;
        L=L*ipi;
        double[] out=new double[2];
        //WGS84椭球  参数
        double a=6378137.0;   //长半轴
        double f=1.0/298.257223563;  //扁平率
        System.out.println(f);
        double b=a*(1-f);    //短半轴
        double e=Math.sqrt(a*a-b*b)/a;   //椭球第一偏心率
        double e2=Math.sqrt(a*a-b*b)/b;  //椭球第二偏心率

        //计算公式各类参数
        double m0=a*(1-e*e);
        double m2=3.0/2 *e*e*m0;
        double m4=5.0/4 *e*e*m2;
        double m6=7.0/6 *e*e*m4;
        double m8=9.0/8 *e*e*m6;

        double a0=m0+m2/2+3.0/8*m4+5.0/16*m6+35.0/128*m8;
        double a2=m2/2+m4/2+15.0/32*m6+7.0/16*m8;
        double a4=m4/8+3.0/16*m6+7.0/32*m8;
        double a6=m6/32+m8/16;
        double a8=m8/128;

        System.out.println("m0:"+m0);
        System.out.println("m2:"+m2);
        System.out.println("m4:"+m4);
        System.out.println("m6:"+m6);
        System.out.println("m8:"+m8);
        //子午线弧长
        double X=a0*B-Math.sin(B)*Math.cos(B)*((a2-a4+a6)+(2*a4-16.0/3*a6)*Math.pow(Math.sin(B),2)+ 16.0/3*a6*Math.pow(Math.sin(B),4));
        //double p=180/Math.PI*3600;
        double p=1.0;
        double q=e2*e2*Math.pow(Math.cos(B),2);
        double t=Math.tan(B);
        double N=a*Math.pow(1-e*e*Math.pow(Math.sin(B),2),-0.5);   //子午圈曲率半径
        L0=L0*ipi;  //中央子午线经度=>弧度
        double l=L-L0;
        System.out.println(X);
        System.out.println(N);
        //System.out.println(l);

        double x=X+N*Math.sin(B)*Math.cos(B)*l*l/(2*p*p)
                +N*Math.sin(B)*Math.pow(Math.cos(B),3)*(5-t*t+9*q+4*q*q)*Math.pow(l,4)/(24*Math.pow(p,4))
                +N*Math.sin(B)*Math.pow(Math.cos(B),5)*(61-58*t*t+Math.pow(t,4))*Math.pow(l,6)/(720*Math.pow(p,6));
        double y=N*Math.cos(B)*l/p+N*Math.pow(Math.cos(B),3)*(1-t*t+q)*l*l*l/(6*Math.pow(p,3))
                +N*Math.pow(Math.cos(B),5)*(5-18*t*t+t*t*t*t+14*q-58*t*t*q)*Math.pow(l,5)/(120*Math.pow(p,5));

        out[0]=x;
        out[1]=y;

        return out;
    }

    public double[] GaussToBL(double x,double y)
    {
        double[] output=new double[2];

        //计算一些基本常量
        //WGS84椭球  参数
        double a=6378137.0;   //长半轴
        double f=1.0/298.257223563;  //扁平率
        double L0=120.0;   //中央子午线经度  正常情况下经纬度转x,y，y结果会+带号*1000000+500000一次来判断处于几带
        double ipi=Math.PI/180;
        System.out.println(f);
        double b=a*(1-f);    //短半轴
        double e=Math.sqrt(a*a-b*b)/a;   //椭球第一偏心率
        double e2=Math.sqrt(a*a-b*b)/b;  //椭球第二偏心率

        //计算公式各类参数
        double m0=a*(1-e*e);
        double m2=3.0/2 *e*e*m0;
        double m4=5.0/4 *e*e*m2;
        double m6=7.0/6 *e*e*m4;
        double m8=9.0/8 *e*e*m6;

        double a0=m0+m2/2+3.0/8*m4+5.0/16*m6+35.0/128*m8;
        double a2=m2/2+m4/2+15.0/32*m6+7.0/16*m8;
        double a4=m4/8+3.0/16*m6+7.0/32*m8;
        double a6=m6/32+m8/16;
        double a8=m8/128;

        //迭代计算Bf，Bf为底点纬度，直到Bf(i+1)-Bfi<c;
        double bf;
        double Bf1=x/a0;   //初始化Bf
        //按子午线弧长公式迭代计算
        double Bfi0=Bf1;
        double Bfi1;
        double FBfi0;
        while (true)
        {
            FBfi0=-a2*Math.sin(2*Bfi0)/2+a4*Math.sin(4*Bfi0)/4-a6*Math.sin(6*Bfi0)/6+a8*Math.sin(8*Bfi0)/8;
            Bfi1=(x-FBfi0)/a0;
            if(Math.abs(Bfi1-Bfi0)<Math.PI*Math.pow(10,-8)/(36*18))
                break;

            Bfi0=Bfi1;
        }
        bf=Bfi1;
        //根据公式计算B，L
        double Nf=a*Math.pow(1-e*e*Math.pow(Math.sin(bf),2),-1.0/2);
        double Mf=a*(1-e*e)*Math.pow(1-e*e*Math.pow(Math.sin(bf),2),-3.0/2);
        double tf=Math.tan(bf);
        double qf2=e2*e2*Math.cos(bf);

        double B=bf-tf*y*y/(2*Mf*Nf)+tf*(5+3*tf*tf+qf2-9*qf2*tf*tf)*Math.pow(y,4)/(24*Mf*Math.pow(Nf,3))
                -tf*(61+90*tf*tf+45*Math.pow(tf,4))*Math.pow(y,6)/(720*Mf*Math.pow(Nf,5));
        double l=y/(Nf*Math.cos(bf))-y*y*y*(1+2*tf*tf+qf2)/(6*Nf*Nf*Nf*Math.cos(bf))
                +Math.pow(y,5)*(5+28*tf*tf+24*Math.pow(tf,4)+6*qf2+8*qf2*tf*tf)/(120*Math.pow(Nf,5)*Math.cos(bf));
        //B,l结果为弧度,需要转换
        double L=L0+l/ipi;
        output[0]=B/ipi;
        output[1]=L;
        return output;
    }
























    public double[] GaussToBL2(double X, double Y)   //, double *longitude, double *latitude)
    {
        int ProjNo; int ZoneWide;           //带宽
        double[] output = new double[2];
        double longitude1,latitude1, longitude0, X0,Y0, xval,yval;//latitude0,
        double e1,e2,f,a, ee, NN, T,C, M, D,R,u,fai, iPI;
        iPI = 0.0174532925199433;           //3.1415926535898/180.0;
        //a = 6378245.0; f = 1.0/298.3;     //54年北京坐标系参数
        a=6378140.0; f=1/298.257;           //80年西安坐标系参数
        ZoneWide = 6;                       //6度带宽
        ProjNo = (int)(X/1000000L) ;        //查找带号
        longitude0 = (ProjNo-1) * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI ;     //中央经线

        X0 = ProjNo*1000000L+500000L;
        Y0 = 0;
        xval = X-X0; yval = Y-Y0;           //带内大地坐标
        e2 = 2*f-f*f;
        e1 = (1.0-Math.sqrt(1-e2))/(1.0+Math.sqrt(1-e2));
        ee = e2/(1-e2);
        M = yval;
        u = M/(a*(1-e2/4-3*e2*e2/64-5*e2*e2*e2/256));
        fai = u+(3*e1/2-27*e1*e1*e1/32)*Math.sin(2*u)+(21*e1*e1/16-55*e1*e1*e1*e1/32)*Math.sin(
                4*u)
                +(151*e1*e1*e1/96)*Math.sin(6*u)+(1097*e1*e1*e1*e1/512)*Math.sin(8*u);
        C = ee*Math.cos(fai)*Math.cos(fai);
        T = Math.tan(fai)*Math.tan(fai);
        NN = a/Math.sqrt(1.0-e2*Math.sin(fai)*Math.sin(fai));
        R = a*(1-e2)/Math.sqrt((1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin
                (fai)*Math.sin(fai)));
        D = xval/NN;
        //计算经度(Longitude) 纬度(Latitude)  弧度
        longitude1 = longitude0+(D-(1+2*T+C)*D*D*D/6+(5-2*C+28*T-3*C*C+8*ee+24*T*T)*D
                *D*D*D*D/120)/Math.cos(fai);
        latitude1 = fai -(NN*Math.tan(fai)/R)*(D*D/2-(5+3*T+10*C-4*C*C-9*ee)*D*D*D*D/24
                +(61+90*T+298*C+45*T*T-256*ee-3*C*C)*D*D*D*D*D*D/720);
        //转换为度 DD
        output[0] = longitude1 / iPI;
        output[1] = latitude1 / iPI;
        return output;
    }

    //由经纬度反算成高斯投影坐标
    public double[] BLToGauss2(double longitude, double latitude)
    {
        int ProjNo=0; int ZoneWide;             //带宽
        double longitude1,latitude1, longitude0,latitude0, X0,Y0, xval,yval;
        double a,f, e2,ee, NN, T,C,A, M, iPI;
        iPI = 0.0174532925199433;              //3.1415926535898/180.0;
        ZoneWide = 6;                           //6度带宽
        a=6378245.0; f=1.0/298.3;               //54年北京坐标系参数
        a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ProjNo = (int)(longitude / ZoneWide) ;
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI ;
        latitude0 = 0;
        System.out.println(latitude0);
        longitude1 = longitude * iPI ; //经度转换为弧度
        latitude1 = latitude * iPI ; //纬度转换为弧度
        e2=2*f-f*f;
        ee=e2*(1.0-e2);
        NN=a/Math.sqrt(1.0-e2*Math.sin(latitude1)*Math.sin(latitude1));
        T=Math.tan(latitude1)*Math.tan(latitude1);
        C=ee*Math.cos(latitude1)*Math.cos(latitude1);
        A=(longitude1-longitude0)*Math.cos(latitude1);
        M=a*((1-e2/4-3*e2*e2/64-5*e2*e2*e2/256)*latitude1-(3*e2/8+3*e2*e2/32+45*e2*e2
                *e2/1024)*Math.sin(2*latitude1)
                +(15*e2*e2/256+45*e2*e2*e2/1024)*Math.sin(4*latitude1)-(35*e2*e2*e2/3072)*Math.sin(6*latitude1));
        xval = NN*(A+(1-T+C)*A*A*A/6+(5-18*T+T*T+72*C-58*ee)*A*A*A*A*A/120);
        yval = M+NN*Math.tan(latitude1)*(A*A/2+(5-T+9*C+4*C*C)*A*A*A*A/24
                +(61-58*T+T*T+600*C-330*ee)*A*A*A*A*A*A/720);
        X0 = 1000000L*(ProjNo+1)+500000L;
        Y0 = 0;

        double[] lonlat = new double[2];
        lonlat[0] = xval+X0;
        lonlat[1] = yval+Y0;
        return lonlat;
    }

    public double[] GaussToBLToGauss(double longitude, double latitude, double meridian)
    {
        int ProjNo=0;
        double longitude1,latitude1, X0,Y0, xval,yval;
        double a,f, e2,ee, NN, T,C,A, M, iPI;
        iPI = 0.0174532925199433; //3.1415926535898/180.0;
        a=6378245.0; f=1.0/298.3; //54年北京坐标系参数
        a=6378140.0; f=1/298.257; //80年西安坐标系参数

        meridian = meridian * iPI ;
        longitude1 = longitude * iPI ; //经度转换为弧度
        latitude1 = latitude * iPI ; //纬度转换为弧度

        e2=2*f-f*f;
        ee=e2*(1.0-e2);
        NN=a/Math.sqrt(1.0-e2*Math.sin(latitude1)*Math.sin(latitude1));
        T=Math.tan(latitude1)*Math.tan(latitude1);
        C=ee*Math.cos(latitude1)*Math.cos(latitude1);
        A=(longitude1-meridian)*Math.cos(latitude1);
        M=a*((1-e2/4-3*e2*e2/64-5*e2*e2*e2/256)*latitude1-(3*e2/8+3*e2*e2/32+45*e2*e2
                *e2/1024)*Math.sin(2*latitude1)
                +(15*e2*e2/256+45*e2*e2*e2/1024)*Math.sin(4*latitude1)-(35*e2*e2*e2/3072)*Math.sin(6*latitude1));
        xval = NN*(A+(1-T+C)*A*A*A/6+(5-18*T+T*T+72*C-58*ee)*A*A*A*A*A/120);
        yval = M+NN*Math.tan(latitude1)*(A*A/2+(5-T+9*C+4*C*C)*A*A*A*A/24
                +(61-58*T+T*T+600*C-330*ee)*A*A*A*A*A*A/720);
        X0 = 1000000L*(ProjNo+1)+500000L;
        Y0 = 0;
        double[] lonlat = new double[2];
        lonlat[0] = xval+X0;
        lonlat[1] = yval+Y0;
        return lonlat;
    }








    /**
     * 由经纬度反算成高斯投影坐标
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public static double[] GaussToBLToGauss(double longitude, double latitude) {
        int ProjNo = 0;
        int ZoneWide; // //带宽
        double[] output = new double[2];
        double longitude1, latitude1, longitude0, X0, Y0, xval, yval;
        double a, f, e2, ee, NN, T, C, A, M, iPI;
        iPI = 0.0174532925199433; // //3.1415926535898/180.0;
        ZoneWide = 6; // //6度带宽
        a = 6378245.0;
        f = 1.0 / 298.3; // 54年北京坐标系参数
        // //a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ProjNo = (int) (longitude / ZoneWide);
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI;
        longitude1 = longitude * iPI; // 经度转换为弧度
        latitude1 = latitude * iPI; // 纬度转换为弧度
        e2 = 2 * f - f * f;
        ee = e2 / (1.0 - e2);
        NN = a
                / Math.sqrt(1.0 - e2 * Math.sin(latitude1)
                * Math.sin(latitude1));
        T = Math.tan(latitude1) * Math.tan(latitude1);
        C = ee * Math.cos(latitude1) * Math.cos(latitude1);
        A = (longitude1 - longitude0) * Math.cos(latitude1);
        M = a
                * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256)
                * latitude1
                - (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2
                / 1024) * Math.sin(2 * latitude1)
                + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024)
                * Math.sin(4 * latitude1) - (35 * e2 * e2 * e2 / 3072)
                * Math.sin(6 * latitude1));
        // 因为是以赤道为Y轴的，与我们南北为Y轴是相反的，所以xy与高斯投影的标准xy正好相反;
        xval = NN
                * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 14
                * C - 58 * ee)
                * A * A * A * A * A / 120);
        yval = M
                + NN
                * Math.tan(latitude1)
                * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61
                - 58 * T + T * T + 270 * C - 330 * ee)
                * A * A * A * A * A * A / 720);
        X0 = 1000000L * (ProjNo + 1) + 500000L;
        Y0 = 0;
        xval = xval + X0;
        yval = yval + Y0;
        output[0] = xval;
        output[1] = yval;
        return output;
    }


    public double[] getGauss(double longitude, double latitude)
    {
        int ProjNo=0; int ZoneWide; ////带宽
        double longitude1,latitude1, longitude0,latitude0, X0,Y0, xval,yval;
        double a,f, e2,ee, NN, T,C,A, M, iPI;
        iPI = 0.0174532925199433; ////3.1415926535898/180.0;
        ZoneWide = 6; ////6度带宽
        a=6378245.0; f=1.0/298.3; //54年北京坐标系参数
        ////a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ProjNo = (int)(longitude / ZoneWide) ;
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI ;
        latitude0 = 0;
        System.out.println(latitude0);
        longitude1 = longitude * iPI ; //经度转换为弧度
        latitude1 = latitude * iPI ; //纬度转换为弧度
        e2=2*f-f*f;
        ee=e2*(1.0-e2);
        NN=a/Math.sqrt(1.0-e2*Math.sin(latitude1)*Math.sin(latitude1));
        T=Math.tan(latitude1)*Math.tan(latitude1);
        C=ee*Math.cos(latitude1)*Math.cos(latitude1);
        A=(longitude1-longitude0)*Math.cos(latitude1);
        M=a*((1-e2/4-3*e2*e2/64-5*e2*e2*e2/256)*latitude1-(3*e2/8+3*e2*e2/32+45*e2*e2
                *e2/1024)*Math.sin(2*latitude1)
                +(15*e2*e2/256+45*e2*e2*e2/1024)*Math.sin(4*latitude1)-(35*e2*e2*e2/3072)*Math.sin(6*latitude1));
        xval = NN*(A+(1-T+C)*A*A*A/6+(5-18*T+T*T+72*C-58*ee)*A*A*A*A*A/120);
        yval = M+NN*Math.tan(latitude1)*(A*A/2+(5-T+9*C+4*C*C)*A*A*A*A/24
                +(61-58*T+T*T+600*C-330*ee)*A*A*A*A*A*A/720);
        X0 = 1000000L*(ProjNo+1)+500000L;
        Y0 = 0;
        xval = xval+X0; yval = yval+Y0;
        //*X = xval;
        //*Y = yval;
        double[] res = new double[2];
        res[0] = xval;
        res[1] = yval;
        return res;

    }


    public static double[] getBL(double X, double Y)//, double *longitude, double *latitude)

    {
        int ProjNo; int ZoneWide; ////带宽
        double[] output = new double[2];
        double longitude1,latitude1, longitude0, X0,Y0, xval,yval;//latitude0,
        double e1,e2,f,a, ee, NN, T,C, M, D,R,u,fai, iPI;
        iPI = 0.0174532925199433; ////3.1415926535898/180.0;
        //a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数
        a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ZoneWide = 3; ////6度带宽
        ProjNo = (int)(X/1000000L) ; //查找带号
        longitude0 = (ProjNo-1) * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI ; //中央经线


        X0 = ProjNo*1000000L+500000L;
        Y0 = 0;
        xval = X-X0; yval = Y-Y0; //带内大地坐标
        e2 = 2*f-f*f;
        e1 = (1.0-Math.sqrt(1-e2))/(1.0+Math.sqrt(1-e2));
        ee = e2/(1-e2);
        M = yval;
        u = M/(a*(1-e2/4-3*e2*e2/64-5*e2*e2*e2/256));
        fai = u+(3*e1/2-27*e1*e1*e1/32)*Math.sin(2*u)+(21*e1*e1/16-55*e1*e1*e1*e1/32)*Math.sin(
                4*u)
                +(151*e1*e1*e1/96)*Math.sin(6*u)+(1097*e1*e1*e1*e1/512)*Math.sin(8*u);
        C = ee*Math.cos(fai)*Math.cos(fai);
        T = Math.tan(fai)*Math.tan(fai);
        NN = a/Math.sqrt(1.0-e2*Math.sin(fai)*Math.sin(fai));
        R = a*(1-e2)/Math.sqrt((1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin
                (fai)*Math.sin(fai)));
        D = xval/NN;
        //计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0+(D-(1+2*T+C)*D*D*D/6+(5-2*C+28*T-3*C*C+8*ee+24*T*T)*D
                *D*D*D*D/120)/Math.cos(fai);
        latitude1 = fai -(NN*Math.tan(fai)/R)*(D*D/2-(5+3*T+10*C-4*C*C-9*ee)*D*D*D*D/24
                +(61+90*T+298*C+45*T*T-256*ee-3*C*C)*D*D*D*D*D*D/720);
        //转换为度 DD
        output[0] = longitude1 / iPI;
        output[1] = latitude1 / iPI;
        return output;
        //*longitude = longitude1 / iPI;
        //*latitude = latitude1 / iPI;
    }
}