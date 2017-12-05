package org.yqj.sj.demo.raw;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import org.yqj.sj.demo.util.DataSourceUtil;
import org.yqj.sj.demo.util.RawJdbcRepository;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yaoqijun on 2017-12-05.
 */
public class RawJdbcJavaShardingTableOnlyMain {
    public static void main(String[] args) throws Exception{
        new RawJdbcRepository(getShardingDataSource()).demo();
    }

    private static DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new HashMap<String, Object>(), new Properties());
    }

    private static TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("demo_ds.t_order_${[0, 1]}");
        orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
        return orderTableRuleConfig;
    }

    /**
     * 创建数据表的配置规则
     * @return
     */
    private static TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
        orderItemTableRuleConfig.setLogicTable("t_order_item");
        orderItemTableRuleConfig.setActualDataNodes("demo_ds.t_order_item_${[0, 1]}");
        return orderItemTableRuleConfig;
    }

    private static Map<String, DataSource> createDataSourceMap(){
        Map<String, DataSource> result = new HashMap<>();
        result.put("demo_ds", DataSourceUtil.createDataSource("demo_ds"));
        return result;
    }
}
