package io.infinite.datasync.common;

import io.infinite.datasync.entity.DbInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
public class DbConnection {

    public Connection createConnection(DbInfo db)
    {
        try
        {
            Class.forName(db.getDriver());
            Connection conn = DriverManager.getConnection(db.getUrl() + "&characterEncoding=gbk", db.getUsername(), db.getPassword());
            conn.setAutoCommit(false);
            return conn;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    public void destoryConnection(Connection conn)
    {
        try
        {
            if (conn != null) {
                conn.close();
                log.info("数据库连接关闭");
            }
        }
        catch (SQLException e)
        {
            log.error(e.getMessage());
        }
    }

}
