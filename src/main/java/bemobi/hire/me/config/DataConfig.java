package bemobi.hire.me.config;

import bemobi.hire.me.domain.Constants;
import bemobi.hire.me.hash.HashGenerator;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by rrodovalho on 09/02/17.
 */

@Configuration
@MapperScan(basePackages= {"bemobi.hire.me.data", "bemobi.hire.me.hash"})
public class DataConfig {

    @Bean
    public HashGenerator getHashGenerator(){
        return new HashGenerator();
    }

    @Bean
    public DataSource getDataSource() {
        return getDataSource(Constants.DATABASE_CONFIG.URL_PRODUCTION);
    }

    private DataSource getDataSource(String dbUrl){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Constants.DATABASE_CONFIG.DRIVER);
        dataSource.setUrl(dbUrl);
        //These info could be setted through env var for increase security
        dataSource.setUsername(Constants.DATABASE_CONFIG.USERNAME);
        dataSource.setPassword(Constants.DATABASE_CONFIG.PASSWORD);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return sqlSessionFactory(null);
    }

    public SqlSessionFactory sqlSessionFactory(String dbUrl) throws Exception{
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        if (dbUrl == null)
            sessionFactory.setDataSource(getDataSource());
        else
            sessionFactory.setDataSource(getDataSource(dbUrl));

        return sessionFactory.getObject();
    }

}
