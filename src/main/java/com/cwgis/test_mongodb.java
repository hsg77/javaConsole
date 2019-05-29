package com.cwgis;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class test_mongodb {

    public static void main()
    {
         //1 获取metadatas表集合
         MongoCollection<Document> coll=mongodbUtil.getConnect("china400").getCollection("metadatas");
         //2 插入一个文档
         Document doc=new Document("name","张三");
         doc.append("sex","男");
         doc.append("age",18);
         coll.insertOne(doc);
         System.out.println("插入一个文档完毕！");
         //3 插入多个文档
        List<Document> list = new ArrayList<>();
        for(int i = 1; i <= 3; i++) {
            Document document = new Document("name", "张三")
                    .append("sex", "男")
                    .append("age", 18);
            list.add(document);
        }
        coll.insertMany(list);
        System.out.println("插入多个文档完毕！");
        //4 修改多个文档
        //修改过滤器
        Bson filter = Filters.eq("name", "张三");
        //指定修改的更新文档
        Document document = new Document("$set", new Document("age", 100));
        //修改多个文档
        coll.updateMany(filter, document);
        //5 查找集合中的所有文档
        FindIterable fit=coll.find();
        MongoCursor cursor=fit.iterator();
        while(cursor.hasNext())
        {
            System.out.println(cursor.next());
        }
        //6指定查询过滤器
        Bson t_filter = Filters.eq("name", "张三");
        //指定查询过滤器查询
        FindIterable findIterable = coll.find(t_filter);
        MongoCursor t_cursor = findIterable.iterator();
        while (t_cursor.hasNext()) {
            System.out.println(t_cursor.next());
        }

        //7 取出查询到的第一个文档
        FindIterable fItColl = coll.find();
        Document first_doc = (Document) fItColl.first();
        //打印输出
        System.out.println("第一个文档:"+first_doc);


        //8 删除一个文档
        coll.deleteOne(Filters.eq("age",100));
        System.out.println("删除一个文档完毕！");
        //9 删除匹配的所有文档
        coll.deleteMany(Filters.eq("age",100));
        System.out.println("删除匹配的所有文档完毕！");

        writeMetadatas(coll);

    }

    public static void writeMetadatas(MongoCollection<Document> coll)
    {
        Document doc=new Document("tilewidth",256);
        doc.append("mapstatusHashCode",1597604222);
        doc.append("transparent",true);
        doc.append("mapParameter",null);
        doc.append("tileType","Image");
        doc.append("tileFormat","PNG");
        //test_mongodb db=new test_mongodb();
        //doc.append("bounds",db.new bounds());
        doc.append("tileRuleVersion","1.0");
        doc.append("mapName","china");
        doc.append("tileHeight",256);
        doc.append("tilesetName","1181328117");
        coll.insertOne(doc);
    }
    public class bounds
    {
        public double east=103.1;
        public double west=102.4;
        public double north=30.3;
        public double bei=30.5;
        public bounds(){}
    }

    public static class mongodbUtil{
        public static String host="192.168.30.113";
        public static int port=27017;
        public static MongoDatabase getConnect(String dbName){
            //连接到mongodb服务
            MongoClient mc=new MongoClient(host,port);
            //连接到数据库
            MongoDatabase mdb=mc.getDatabase(dbName);
            return mdb;
        }
        public static MongoDatabase getConnectByCred(String dbName,String username,String pwd)
        {
            List<ServerAddress> adds=new ArrayList<>();
            ServerAddress sAddr=new ServerAddress(host,port);
            adds.add(sAddr);
            //
            List<MongoCredential> credList=new ArrayList<>();
            MongoCredential mcred=MongoCredential.createScramSha1Credential(username,dbName,pwd.toCharArray());
            credList.add(mcred);

            //通过连接认证获取mongodb连接
            MongoClient mc=new MongoClient(adds,credList);
            //连接到数据库
            MongoDatabase mdb=mc.getDatabase(dbName);
            return mdb;
        }
    }
}
