package io.infinite.datasync.dbhelper;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbHelper {

    String assembleSQL(String dbName, Connection conn, String table, String uuid)
    throws SQLException;

    void executeSQL(String sql, Connection conn, String uuid)
            throws SQLException;

}
