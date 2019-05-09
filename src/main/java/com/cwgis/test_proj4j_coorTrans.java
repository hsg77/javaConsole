package com.cwgis;

import org.locationtech.proj4j.*;

public class test_proj4j_coorTrans {
    public static void main() {
        CRSFactory crsFactory = new CRSFactory();
        //cgs2000  4490   //wgs84   4326
        CoordinateReferenceSystem src = crsFactory.createFromName("epsg:4490");   //cgs2000  4490
        CoordinateReferenceSystem dest = crsFactory.createFromName("epsg:4523");  //cgs2000 3  35

        CoordinateTransformFactory ctf = new CoordinateTransformFactory();
        CoordinateTransform transform = ctf.createTransform(src, dest);

        ProjCoordinate srcPt = new ProjCoordinate(105.222047895135D, 30.8570779270239D);
        ProjCoordinate destPt = new ProjCoordinate();

        transform.transform(srcPt, destPt);
        System.out.println(srcPt + " ==> " + destPt);

        // do it again
        ProjCoordinate destPt2 = new ProjCoordinate();
        transform.transform(srcPt, destPt2);
        System.out.println(srcPt + " ==> " + destPt2);

        System.out.println(destPt.equals(destPt2));
    }
}
