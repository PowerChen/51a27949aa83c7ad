package com.powerting.spweb.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
@RequestMapping("/api2")
public class HiveJdbcTemplateController {

//    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcTemplateController.class);

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
    @RequestMapping("/diy")
    public String doHive2Jdbc(String opType, String sql)  {
        String result = null;
//        logger.info("opType: " + opType+" sql:"+sql);
        if(opType != null && sql != null){
            switch (opType){
                case "create":
                    StringBuffer hsql = new StringBuffer(sql);
                    result = "Create table successfully...";
                    try {
                        hiveDruidTemplate.execute(hsql.toString());
                    } catch (DataAccessException dae) {
                        result = "Create table encounter an error: " + dae.getMessage();
//                        logger.error(result);
                    }
                    break;
                case "load":
                    result = "Load data into table successfully...";
                    try {
                        // hiveJdbcTemplate.execute(sql);
                        hiveDruidTemplate.execute(sql);
                    } catch (DataAccessException dae) {
                        result = "Load data into table encounter an error: " + dae.getMessage();
//                        logger.error(result);
                    }
                    break;
                case "delete":
                    result = "Drop table successfully...";

                    try {
                        // hiveJdbcTemplate.execute(sql);
                        hiveDruidTemplate.execute(sql);
                    } catch (DataAccessException dae) {
                        result = "Drop table encounter an error: " + dae.getMessage();
//                        logger.error(result);
                    }
                    break;
                default:
//                    logger.info("unknow opType: " + opType);
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
    @RequestMapping("/test/create")
    public String createTable() {
        String result = "Create table successfully...";
        return result;
    }

}
