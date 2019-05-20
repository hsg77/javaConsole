package com.cwgis;

//测试点到直线的距离和垂足点计算
public class test_footPoint {
     public static void main()
     {
         Coordinate point=getNewCoordinate(10,5);
         Coordinate pnt1=getNewCoordinate(-1,0);
         Coordinate pnt2=getNewCoordinate(20,0);
         Coordinate fp=getFootPoint(point,pnt1,pnt2);
         System.out.println("x="+fp.x+",y="+fp.y);   //x=10.0,y=0.0
         System.out.println("  距离="+getDistancePoineToLine_planeCoord(point,pnt1,pnt2));
         System.out.println("hw距离="+getDistanceByPointToLine_hw(point,pnt1,pnt2));
         //x=10.0,y=0.0
         //  距离=5.0
         //hw距离=5.0

         point=getNewCoordinate(10,5);
         pnt1=getNewCoordinate(0,-1);
         pnt2=getNewCoordinate(0,20);
         fp=getFootPoint(point,pnt1,pnt2);
         System.out.println("");
         System.out.println("x="+fp.x+",y="+fp.y);  //x=0.0,y=5.0
         System.out.println("  距离="+getDistancePoineToLine_planeCoord(point,pnt1,pnt2));
         System.out.println("hw距离="+getDistanceByPointToLine_hw(point,pnt1,pnt2));
         //x=0.0,y=5.0
         //  距离=10.0
         //hw距离=10.0


         point=getNewCoordinate(10,10);
         pnt1=getNewCoordinate(0,10);
         pnt2=getNewCoordinate(10,0);
         fp=getFootPoint(point,pnt1,pnt2);
         System.out.println("");
         System.out.println("x="+fp.x+",y="+fp.y);  //x=5.0,y=5.0
         System.out.println("  距离="+getDistancePoineToLine_planeCoord(point,pnt1,pnt2));
         System.out.println("hw距离="+getDistanceByPointToLine_hw(point,pnt1,pnt2));
         //x=5.0,y=5.0
         //  距离=7.071067811865475
         //hw距离=7.071067811865475
     }
    //点到直线的距离
    public static double getDistanceByPointToLine_hw(Coordinate point, Coordinate pnt1, Coordinate pnt2)
    {
        double dis = 0;
        if (pnt1.x == pnt2.x)
        {
            if (pnt1.y == pnt2.y)
            {
                double dx = point.x - pnt1.x;
                double dy = point.y - pnt1.y;
                dis = Math.sqrt(dx * dx + dy * dy);
            }
            else
                dis = Math.abs(point.x - pnt1.x);
        }
        else
        {
            double lineK = (pnt2.y - pnt1.y) / (pnt2.x - pnt1.x);
            double lineC = (pnt2.x * pnt1.y - pnt1.x * pnt2.y) / (pnt2.x - pnt1.x);
            dis = Math.abs(lineK * point.x - point.y + lineC) / (Math.sqrt(lineK * lineK + 1));
        }
        //
        return dis;
    }
    //点到直线的垂足点
    public static Coordinate getFootPoint(Coordinate point, Coordinate pnt1, Coordinate pnt2)
    {
        double A=pnt2.y-pnt1.y;     //y2-y1
        double B=pnt1.x-pnt2.x;     //x1-x2;
        double C=pnt2.x*pnt1.y-pnt1.x*pnt2.y;     //x2*y1-x1*y2
        if (A * A + B * B < 1e-13) {
            return pnt1;   //pnt1与pnt2重叠
        }
        else if (Math.abs(A * point.x + B * point.y + C) < 1e-13) {
            return point;   //point在直线上(pnt1_pnt2)
        }
        else {
            double x = (B * B * point.x - A * B * point.y - A * C) / (A * A + B * B);
            double y = (-A * B * point.x + A * A * point.y - B * C) / (A * A + B * B);
            return getNewCoordinate(x,y);
        }
    }
    public static double getDistancePoineToLine_planeCoord(Coordinate point, Coordinate pnt1, Coordinate pnt2)
    {   //平面坐标中
        double A=pnt2.y-pnt1.y;     //y2-y1
        double B=pnt1.x-pnt2.x;     //x1-x2;
        double C=pnt2.x*pnt1.y-pnt1.x*pnt2.y;     //x2*y1-x1*y2
        if (A * A + B * B < 1e-13) {   //pnt1与pnt2重叠
            double dx = point.x - pnt1.x;
            double dy = point.y - pnt1.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        else if (Math.abs(A * point.x + B * point.y + C) < 1e-13) {
            return 0;   //point在直线上(pnt1_pnt2)
        }
        else {
            double distance = Math.abs(A * point.x + B * point.y + C) / Math.sqrt(A * A + B * B);
            return distance;
        }
    }
    public class Coordinate{
         public double x=0.0D;
         public double y=0.0D;
         public double z=0.0D;
         public Coordinate()
         {
         }
        public Coordinate(double x,double y)
        {
            this.x=x;
            this.y=y;
        }
    }
    //注意：必须先有外部类的对象才能生成内部类的对象，因为内部类需要访问外部类中的成员变量，成员变量必须实例化才有意义。
    public static Coordinate getNewCoordinate(double x,double y)
    {   //调用内部类的方法
        test_footPoint fp=new test_footPoint();
        return fp.new Coordinate(x,y);
    }
}
