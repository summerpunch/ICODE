package com.icode.cas.common.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title: mybatis-plus配置相关<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:04<br>
 */
@Configuration
@MapperScan("com.icode.cas.repository.mapper*")
public class MyBatisPlusConfig {

    /**
     * Title: mybatis-plus SQL执行效率插件【生产环境可以关闭】<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 19:03<br>
     * Param: []<br>
     * Return: com.baomidou.mybatisplus.plugins.PerformanceInterceptor
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * Title: mybatis-plus分页插件<br>
     * Description: 文档：http://mp.baomidou.com<br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 19:03<br>
     * Param: []<br>
     * Return: com.baomidou.mybatisplus.plugins.PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setLocalPage(true);
        return paginationInterceptor;
    }

}
