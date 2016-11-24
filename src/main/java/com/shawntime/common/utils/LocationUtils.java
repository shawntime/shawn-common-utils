package com.shawntime.common.utils;

/**
 * 地理坐标处理WGS-84坐标转换成百度坐标
 * Created by luoxianchao on 2016/11/15.
 */
public final class LocationUtils {

    private LocationUtils() {
        //can't instance
    }

    private final static double pi = 3.14159265358979324;

    private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    //
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    final static double a = 6378245.0;
    final static double ee = 0.00669342162296594323;


    //
    // World Geodetic System ==> Mars Geodetic System
    public static double[] transform(double wgLat, double wgLon) {
        double mgLat;
        double mgLon;
        if (outOfChina(wgLat, wgLon)) {
            mgLat = wgLat;
            mgLon = wgLon;

        } else {
            double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
            double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
            double radLat = wgLat / 180.0 * pi;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
            mgLat = wgLat + dLat;
            mgLon = wgLon + dLon;
        }
        double[] point = {mgLat, mgLon};
        return point;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * wgs-84坐标转换成百度坐标
     *
     * @param mapLat 纬度
     * @param mapLon 经度
     * @return double[] 经纬度数组
     */
    public static double[] getBaiDuLocation(double mapLat, double mapLon) {
        double[] j_localtion = transform(mapLat, mapLon);
        double baiduLat = j_localtion[0];//纬度
        double baiduLon = j_localtion[1];//经度
        double z = Math.sqrt(baiduLon * baiduLon + baiduLat * baiduLat) + 0.00002 * Math.sin(baiduLat * x_pi);
        double theta = Math.atan2(baiduLat, baiduLon) + 0.000003 * Math.cos(baiduLon * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        double result[] = {bd_lat, bd_lon};
        return result;
    }


    /**
     * 判断纬度是否在合理范围内
     *
     * @param mapLat 纬度
     * @return true or false
     */
    public static boolean outOfMapLat(double mapLat) {
        if (mapLat < 0.8293 || mapLat > 55.8271){
            return true;
        }
        return false;
    }


    public static void main(String args[]) {
       /* double reuslt[] = transform(116.3058137,39.7967234);
        System.out.println(reuslt[0]);
        System.out.println(reuslt[1]);*/
    }
}
