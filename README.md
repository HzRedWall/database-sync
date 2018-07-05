### 数据库同步


#### 已完成功能

    1：指定数据库同步
    2：指定表同步
    3：通过http请求完成同步
    4：主键更新
    
#### todo

    1：数据库连接优化
    2: 一次可以同步多个库
    3：支持其他数据库类型，目前只支持mysql的同步
    
#### 使用步骤

    1：job.xml配置数据库联系信息
    
        源数据库信息
        <source>
            <url>jdbc:mysql://127.0.0.1:3306/test?characterSetResults=utf8</url>
            <username>root</username>
            <password></password>
            <dbtype>mysql</dbtype>
            <driver>com.mysql.jdbc.Driver</driver>
            <dbName>wtxfirecontrol</dbName>
            <destTable>a,b,c,d</destTable>
        </source>
        
        目标数据源信息
        <dest>
           <url>jdbc:mysql://127.0.0.1:3306/test?characterSetResults=utf8</url>
           <username>root</username>
           <password></password>
           <dbtype>mysql</dbtype>
           <driver>com.mysql.jdbc.Driver</driver>
           <dbName>wtx</dbName>
           <destTable>a,b,c,d</destTable>
       </dest>
       可配置多个节点，也就是多个目标数据源
       
    2：通过http请求同步
    
    path: ip:port/sync/send
    method: post
    param: dest 目标数据库名称 如果xml配置中不存在此数据库，返回相应的response
    
   
 
 
##### 欢迎issue