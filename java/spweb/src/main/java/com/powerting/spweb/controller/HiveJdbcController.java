package com.powerting.spweb.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * 使用 DataSource 操作 Hive
 */
@RestController
@RequestMapping("/api")
public class HiveJdbcController {

//    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcController.class);

    @Autowired
    @Qualifier("hiveDruidDataSource")
    DataSource druidDataSource;

    @Autowired
    @Qualifier("hiveDataSourceCfg")
    DataSource jdbcDataSource;

    /**
     * 列举当前Hive库中的所有数据表
     * @return return
     */
    @RequestMapping("/test/list")
    public String listAllTables()  {
        String result = "Running test list";
//        logger.info(result);
        return result;
    }

}