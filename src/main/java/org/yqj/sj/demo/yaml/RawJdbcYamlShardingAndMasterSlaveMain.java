package org.yqj.sj.demo.yaml;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import org.yqj.sj.demo.util.RawJdbcRepository;

import javax.sql.DataSource;
import java.io.File;

public final class RawJdbcYamlShardingAndMasterSlaveMain {
    
    // CHECKSTYLE:OFF
    public static void main(final String[] args) throws Exception {
    // CHECKSTYLE:ON
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(new File(
                RawJdbcYamlShardingAndMasterSlaveMain.class.getResource("/META-INF/yamlShardingAndMasterSlave.yaml").getFile()));
        new RawJdbcRepository(dataSource).demo();
    }
}
