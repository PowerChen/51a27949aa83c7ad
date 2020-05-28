package com.powerting.spweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用 JdbcTemplate 操作 Hive
 */
@RestController
@RequestMapping("/hive2")
public class HiveJdbcTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcTemplateController.class);

    @Autowired
    @Qualifier("hiveDruidTemplate")
    private JdbcTemplate hiveDruidTemplate;

    @Autowired
    @Qualifier("hiveTemplate")
    private JdbcTemplate hiveJdbcTemplate;



    /**
     * 通用自己定义传入Hive Sql
     * opTYpe:  create, load, delete
     */
    @RequestMapping("/jdbc")
    public String doHive2Jdbc(String opType, String sql)  {
        String result = null;
        logger.info("opType: " + opType+" sql:"+sql);
        if(opType != null && sql != null){
            switch (opType){
                case "create":
                    StringBuffer hsql = new StringBuffer(sql);
                    result = "Create table successfully...";
                    try {
                        hiveDruidTemplate.execute(hsql.toString());
                    } catch (DataAccessException dae) {
                        result = "Create table encounter an error: " + dae.getMessage();
                        logger.error(result);
                    }
                    break;
                case "load":
                    result = "Load data into table successfully...";
                    try {
                        // hiveJdbcTemplate.execute(sql);
                        hiveDruidTemplate.execute(sql);
                    } catch (DataAccessException dae) {
                        result = "Load data into table encounter an error: " + dae.getMessage();
                        logger.error(result);
                    }
                    break;
                case "delete":
                    result = "Drop table successfully...";

                    try {
                        // hiveJdbcTemplate.execute(sql);
                        hiveDruidTemplate.execute(sql);
                    } catch (DataAccessException dae) {
                        result = "Drop table encounter an error: " + dae.getMessage();
                        logger.error(result);
                    }
                    break;
                default:
                    logger.info("unknow opType: " + opType);
                    break;
            }
        }else{
            result ="opType or sql is null";
        }

        return result;
    }

    /**
     * 示例：创建新表
     */
    @RequestMapping("/table/create")
    public String createTable() {
        StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sql.append("user_test");
        sql.append("(user_num BIGINT, user_name STRING, user_gender STRING, user_age INT)");
        sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' "); // 定义分隔符
        sql.append("STORED AS TEXTFILE"); // 作为文本存储

        logger.info("Running: " + sql);
        String result = "Create table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql.toString());
            hiveDruidTemplate.execute(sql.toString());
        } catch (DataAccessException dae) {
            result = "Create table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;

    }

    /**
     * 示例：将Hive服务器本地文档中的数据加载到Hive表中
     */
    @RequestMapping("/table/load")
    public String loadIntoTable() {
        String filepath = "/home/dxp/testfile";
        String sql = "load data local inpath '" + filepath + "' into table user_test";
        String result = "Load data into table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql);
            hiveDruidTemplate.execute(sql);
        } catch (DataAccessException dae) {
            result = "Load data into table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;
    }

    /**
     * 示例：删除表
     */
    @RequestMapping("/table/delete")
    public String delete(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        String result = "Drop table successfully...";
        logger.info("Running: " + sql);
        try {
            // hiveJdbcTemplate.execute(sql);
            hiveDruidTemplate.execute(sql);
        } catch (DataAccessException dae) {
            result = "Drop table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;
    }
}
