package io.infinite.datasync.dbhelper;

import io.infinite.datasync.common.Tool;
import io.infinite.datasync.entity.FieldInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySqlHelper implements DbHelper {


    /**
    * 组装sql
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param 
    * @date 2018/7/4
    * @return 
    **/
    @Override
    public String assembleSQL(String dbName, Connection conn, String table, String uuid) throws SQLException {

        String uniqueName = Tool.generateString(10);
        String destTable = table;
        FieldInfo fieldInfo = this.getFieldsByTable(table,conn,dbName);
        List<String> allFields = fieldInfo.getAllFields();
        String primaryKey = fieldInfo.getPrimaryKey();
        List<String> updateFieldList = new ArrayList<>(allFields);
        String[] updateFields = updateFieldList.toArray(new String[updateFieldList.size()]);
        if (!StringUtils.isEmpty(primaryKey)){
            updateFieldList.remove(primaryKey);
        }
        String updateFieldsArr = org.apache.commons.lang.StringUtils.join(
                updateFields,",");
        String[] fields = allFields.toArray(new String[allFields.size()]);
        String destTableFields = org.apache.commons.lang.StringUtils.join(fields,",");
        String srcSql = "select * from " + table;
        PreparedStatement pst = conn.prepareStatement(srcSql);
        ResultSet rs = pst.executeQuery();
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ").append(table).append(" (").append(destTableFields).append(") values ");
        long count = 0;
        while (rs.next()) {
            sql.append("(");
            for (int index = 0; index < fields.length; index++) {
                /**
                 * 解决mysql 插入的时候 null 值 转化
                 * */
                String fieldValue = rs.getString(fields[index]);
                sql.append("'").append(fieldValue).append(index == (fields.length - 1) ? "'" : "',");
            }
            sql.append("),");
            count++;
        }
        /**
         * mysql 非字符串类型 不能插入 "" 和  'null' 在这做个转化0
         * */
        if (sql.toString() != null && sql.toString().length() > 1){
            String sqlQuery = sql.toString();
            sql = new StringBuffer(sqlQuery.replace("'null'","NULL"));
        }
        if (rs != null) {
            rs.close();
        }
        if (pst != null) {
            pst.close();
        }
        if (count > 0) {
            sql = sql.deleteCharAt(sql.length() - 1);
            if ((!updateFieldsArr.equals("")) && (!updateFieldsArr.equals(""))) {
                sql.append(" on duplicate key update ");
                for (int index = 0; index < updateFields.length; index++) {
                    sql.append(updateFields[index]).append("= values(").append(updateFields[index]).append(index == (updateFields.length - 1) ? ")" : "),");
                }
                return new StringBuffer("alter table ").append(destTable).append(" add constraint ").append(uniqueName).append(" unique (").append(primaryKey).append(")")
                        .append(uuid)
                        .append(sql.toString()).append(uuid)
                        .append("alter table ").append(destTable).append(" drop index ").append(uniqueName).toString();
            }
            return sql.toString();
        }
        return null;
    }


    @Override
    public void executeSQL(String sql, Connection conn, String uuid) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("");
        String[] sqlList = sql.split(uuid);
        for (int index = 0; index < sqlList.length; index++) {
            log.info(sqlList[index]);
            pst.addBatch(sqlList[index]);
        }
        pst.executeBatch();
        conn.commit();
        pst.close();
    }

    /**
    * 获取制定表的全部字段
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param 
    * @date 2018/7/4
    * @return 
    **/
    private FieldInfo getFieldsByTable(String table, Connection connection, String dbName) throws SQLException{
        String sql = "SELECT * FROM information_schema.COLUMNS WHERE table_name = '" + table + "'"
                + " AND TABLE_SCHEMA =" + "'" + dbName + "'";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            List<String> destFields = new ArrayList<>();
            String primaryKey = new String();
            int count = 0;
            while(rs.next()){
                if ("PRI".equals(rs.getString("COLUMN_KEY")) && count == 0){
                    // 多个主键 取第一个
                    primaryKey = rs.getString("COLUMN_NAME");
                    count++;
                }
                destFields.add(rs.getString("COLUMN_NAME"));
            }
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setAllFields(destFields);
            if (!StringUtils.isEmpty(primaryKey)){
                fieldInfo.setPrimaryKey(primaryKey);
            }
            return fieldInfo;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
