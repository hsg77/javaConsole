package com.cwgis;

import com.cwgis.gs.GeoServerUtil;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
            throws Exception
    {
        System.out.println( "Hello World!" );
        //testInsertPointCopy.main();  //OK
        //test_sqlite_jdbc.main();     //OK
        //test_threads.main(new String[]{});   //OK
        //test_proj4j_coorTrans.main();   //OK
        //test_footPoint.main();
        //testScale();
        //test_mysql_jdbc.main();
        //test_mongodb.main();

        //test_kafka.main();
        //test_kafka kfk=new test_kafka();
        //kfk.test();
        //kfk.testThreadPool();
        //
        //GeoServerUtil.unPublishShpAndReloadStore("geowave","hash8_tk_2");
        //System.out.println("remove layer OK!");
        //GeoServerUtil.createWorkspace("hsg");
        //GeoServerUtil.removeDatastore("geowave","hash8_tk_2");
        GeoServerUtil.createDatastore("geowave","hash8_tk_2","hash8_tk_2","EPSG:4326","DistributedRender-Polygon");
        System.out.println("OK!");
    }
    public static void testScale()
    {
        double earch_radius = 6371008.8D;   // 地球半径 平均值  米
        double resolution=2*Math.PI*earch_radius;    //赤道分辨率    （像素/米）
        System.out.println(resolution);
        System.out.println(resolution/256);
        double lat=30.77751;
        double cpResolution=resolution*Math.cos(lat*Math.PI/180.0);   //某一纬度分辨率
        double scale=cpResolution*72*32.3701;
        System.out.println(scale);
    }


}
