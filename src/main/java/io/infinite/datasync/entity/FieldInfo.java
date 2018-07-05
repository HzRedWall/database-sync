package io.infinite.datasync.entity;

import lombok.Data;

import java.util.List;

@Data
public class FieldInfo {

    /**
    *  主键字段
    */
    private String primaryKey;
    
    /**
    *  全部字段
    */
    private List<String> allFields;

}
