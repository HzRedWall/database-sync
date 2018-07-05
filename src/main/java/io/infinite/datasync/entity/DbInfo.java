package io.infinite.datasync.entity;

import lombok.Data;

@Data
public class DbInfo {
    String url;
    String username;
    String password;
    String dbtype;
    String driver;
    String destTable;
    String dbName;
}
