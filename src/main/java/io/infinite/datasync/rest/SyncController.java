package io.infinite.datasync.rest;

import io.infinite.datasync.common.ApiResponse;
import io.infinite.datasync.common.DbConnection;
import io.infinite.datasync.dbhelper.DbFactory;
import io.infinite.datasync.dbhelper.DbHelper;
import io.infinite.datasync.entity.DbInfo;
import io.infinite.datasync.parse.DbInfoParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
* 数据同步
* @author RedWall
* @mail walkmanlucas@gmail.com
* @date 2018/7/3
* @since JDK 1.8
**/
@Controller
@RequestMapping(value = "/sync")
@Slf4j
public class SyncController {

    /**
    * 数据同步
    * @author RedWall
    * @mail walkmanlucas@gmail.com
    * @param dest 目的库
    * @date 2018/7/3
    * @return 
    **/
    @PostMapping(value = "/send")
    @ResponseBody
    public ApiResponse send(@RequestParam(value = "dest") String dest){
        log.info("开始同步库");
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(dest)){
            log.error("同步库--目的库为空");
            return ApiResponse.createFail("目的库为空");
        }
        DbInfoParse dbInfoParse = new DbInfoParse();
        DbInfo sourceDbinfo = dbInfoParse.generateSourceDbinfo();
        DbInfo destDbinfo = dbInfoParse.generateDestDbInfo(dest);
        DbConnection dbConnection = new DbConnection();
        Connection inConn = null;
        Connection outConn = null;
        try {
            inConn = dbConnection.createConnection(sourceDbinfo);
            outConn = dbConnection.createConnection(destDbinfo);
            if(inConn == null){
                log.error("请检查源数据连接!");
                return ApiResponse.createFail("请检查源数据连接");
            } else if(outConn == null){
                log.error("请检查目标数据连接!");
                return ApiResponse.createFail("请检查目标数据连接");
            }
            DbHelper dbHelper = DbFactory.create(destDbinfo.getDbtype());
            long start = new Date().getTime();
            String[] tables = sourceDbinfo.getDestTable().split(",");
            for (int i = 0; i < tables.length; i++) {
                String uuid = UUID.randomUUID().toString().replace("-","");
                String sql = dbHelper.assembleSQL(sourceDbinfo.getDbName(),
                        inConn,tables[i],uuid);
                log.info("组装SQL耗时: " + (new Date().getTime() - start) + "ms");
                if(sql != null){
                    long eStart = new Date().getTime();
                    dbHelper.executeSQL(sql, outConn,uuid);
                    log.info("执行SQL语句耗时: " + (new Date().getTime() - eStart) + "ms");
                }
            }
            long endTime = System.currentTimeMillis();
            return ApiResponse.createSuccessByMsg("执行同步结束,耗时:"
                    + 1.0 * (endTime - startTime)/1000+ "秒");
        }
        catch (SQLException e) {
            log.error(e.getMessage());
            log.error(" SQL执行出错，请检查是否存在语法错误");
        }
        finally {
            dbConnection.destoryConnection(inConn);
            log.info("关闭源数据库连接");
            dbConnection.destoryConnection(outConn);
            log.info("关闭目标数据库连接");
            log.info("同步数据库结束");
        }
        return null;
    }



}
