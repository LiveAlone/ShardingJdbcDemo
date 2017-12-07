package org.yqj.sj.demo.yaml;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.yqj.sj.demo.util.RawJdbcRepository;

import javax.sql.DataSource;
import java.io.File;



public final class RawJdbcYamlMasterSlaveMain {
    
    public static void main(final String[] args) throws Exception {
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(new File(
                RawJdbcYamlMasterSlaveMain.class.getResource("/yaml/yamlMasterSlave.yaml").getFile()));
        new RawJdbcRepository(dataSource).demo();
    }
}
