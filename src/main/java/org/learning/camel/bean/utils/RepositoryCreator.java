package org.learning.camel.bean.utils;

import org.apache.camel.processor.aggregate.jdbc.JdbcAggregationRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

public class RepositoryCreator {
    public static JdbcAggregationRepository jdbcRepositoryCreator(){
        DataSource dataSource = createDataSource();
        PlatformTransactionManager manager = new DataSourceTransactionManager(dataSource);
        JdbcAggregationRepository repo = new JdbcAggregationRepository(manager, "aggregation_repo", dataSource);
        repo.setUseRecovery(true);
        repo.setHeadersToStoreAsText(List.of("owner", "bid"));
        return repo;
    }
    public static DataSource createDataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/carddb");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        return ds;
    }
}
