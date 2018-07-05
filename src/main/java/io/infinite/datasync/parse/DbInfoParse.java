package io.infinite.datasync.parse;

import io.infinite.datasync.common.ApiResponse;
import io.infinite.datasync.entity.DbInfo;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbInfoParse {


    /**
    * xml 解析为源数据库信息
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param 
    * @date 2018/7/4
    * @return 
    **/
    public DbInfo generateSourceDbinfo(){
        SAXReader reader = new SAXReader();
        try {
            //读取xml的配置文件名，并获取其里面的节点
            String path = this.getClass().getClassLoader().getResource("").getPath();
            Element root = reader.read(path + "jobs.xml").getRootElement();
            Element src = root.element("source");
            DbInfo sourceDb = new DbInfo();
            sourceDb = (DbInfo) this.elementInObject(src,sourceDb);
            return sourceDb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * 生成需要同步的同步库信息
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param 
    * @date 2018/7/4
    * @return 
    **/
    public DbInfo generateDestDbInfo(String destDb){
        SAXReader reader = new SAXReader();
        try {
            //读取xml的配置文件名，并获取其里面的节点
            String path = this.getClass().getClassLoader().getResource("").getPath();
            Element root = reader.read(path + "jobs.xml").getRootElement();
            List<DbInfo> list = new ArrayList<>();
            for(Iterator it = root.elementIterator("dest"); it.hasNext();){
                list.add((DbInfo) elementInObject((Element)it.next(), new DbInfo()));
            }
            DbInfo dest = list.stream().filter(dbInfo -> dbInfo.getDbName().equals(destDb) ).findFirst().get();
            if (dest != null){
                return dest;
            }else {
                ApiResponse.createFail("目标数据库不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * element 转化为dbinfo entity
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param
    * @date 2018/7/4
    * @return
    **/
    public Object elementInObject(Element e, Object o) throws IllegalArgumentException, IllegalAccessException{
        Field[] fields = o.getClass().getDeclaredFields();
        for(int index = 0; index < fields.length; index++){
            fields[index].setAccessible(true);
            if (e.element(fields[index].getName()) != null){
                fields[index].set(o, e.element(fields[index].getName()).getTextTrim());
            }
        }
        return o;
    }

}
