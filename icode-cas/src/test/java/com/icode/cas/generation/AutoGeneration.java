package com.icode.cas.generation;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

public class AutoGeneration {

    @Test
    public void auto() {
        String packageName = "com.icode.cas";
        String dbUrl = "jdbc:mysql://localhost:3306/icode?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
        String paths = "D:\\ideaWorkspace\\ott-hms\\src\\main\\java";
        boolean serviceNameStartWithI = true;// 设置生成的service接口的名字的首字母是否为I
        generateByTables(
                serviceNameStartWithI,
                packageName,
                dbUrl,
                paths,
                "sys_acl",
                "sys_acl_module",
                "sys_dept",
                "sys_log",
                "sys_role",
                "sys_role_acl",
                "sys_role_user",
                "sys_user",
                "cas_sys_dictionary"
        );
    }



    private void generateByTables(boolean serviceNameStartWithI, String packageName,String dbUrl,String paths, String... tableNames) {

        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)// 是否支持AR模式
                .setAuthor("xiachong")// 作者
                .setOutputDir(paths)// 生成路径
                .setFileOverride(true)//文件覆盖
                .setBaseResultMap(true)//XML ResultMap
                .setBaseColumnList(true)//XML columList
                .setEnableCache(false)// XML 二级缓存
                .setIdType(IdType.AUTO);// 主键策略
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");// 设置生成的service接口的名字的首字母是否为I
        }


        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)// 设置数据库类型
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("123456")
                .setDriverName("com.mysql.jdbc.Driver");

        //3. 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)//全局大写命名
                .setEntityLombokModel(false)//实体是否为lombok模型（默认 false）
                .setDbColumnUnderline(true)// 指定表名 字段名是否使用下划线
                .setNaming(NamingStrategy.underline_to_camel)// 数据库表映射到实体的命名策略
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组

        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent(packageName)
                .setController("controller")
                .setEntity("repository.entity");

        //5. 整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(pkConfig);

        //6. 执行
        ag.execute();
    }
}