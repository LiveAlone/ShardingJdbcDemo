package org.yqj.sj.demo.transaction;

import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingjdbc.transaction.api.config.NestedBestEffortsDeliveryJobConfiguration;
import io.shardingjdbc.transaction.api.config.SoftTransactionConfiguration;
import org.yqj.sj.demo.util.DataSourceUtil;
import org.yqj.sj.demo.util.algorithm.ModuloShardingDatabaseAlgorithm;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yaoqijun on 2017-12-06.
 */
public class TransactionMain {

    private static boolean useNestedJob = true;

    // CHECKSTYLE:OFF
    public static void main(final String[] args) throws SQLException {
        // CHECKSTYLE:ON
        DataSource dataSource = getShardingDataSource();
        dropTable(dataSource);
        createTable(dataSource);
        insert(dataSource);
//        updateFailure(dataSource);
    }

    private static void createTable(final DataSource dataSource) throws SQLException {
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS t_order (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))");
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS t_order_item (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (item_id))");
    }

    private static void dropTable(final DataSource dataSource) throws SQLException {
        executeUpdate(dataSource, "DROP TABLE IF EXISTS t_order_item");
        executeUpdate(dataSource, "DROP TABLE IF EXISTS t_order");
    }

    private static void executeUpdate(final DataSource dataSource, final String sql) throws SQLException {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void insert(final DataSource dataSource) throws SQLException {
        String sql = "INSERT INTO t_order VALUES (1000, 10, 'INIT');";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }

//    private static void updateFailure(final DataSource dataSource) throws SQLException {
//        String sql1 = "UPDATE t_order SET status='UPDATE_1' WHERE user_id=10 AND order_id=1000";
//        String sql2 = "UPDATE t_order SET not_existed_column=1 WHERE user_id=1 AND order_id=?";
//        String sql3 = "UPDATE t_order SET status='UPDATE_2' WHERE user_id=10 AND order_id=1000";
//        SoftTransactionManager transactionManager = new SoftTransactionManager(getSoftTransactionConfiguration(dataSource));
//        transactionManager.init();
//        BEDSoftTransaction transaction = (BEDSoftTransaction) transactionManager.getTransaction(SoftTransactionType.BestEffortsDelivery);
//        Connection conn = null;
//        try {
//            conn = dataSource.getConnection();
//            transaction.begin(conn);
//            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
//            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
//            preparedStatement2.setObject(1, 1000);
//            PreparedStatement preparedStatement3 = conn.prepareStatement(sql3);
//            preparedStatement1.executeUpdate();
//            preparedStatement2.executeUpdate();
//            preparedStatement3.executeUpdate();
//        } finally {
//            transaction.end();
//            if (conn != null) {
//                conn.close();
//            }
//        }
//    }

    private static DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("ds_trans_${0..1}.t_order_${0..1}");
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
        orderItemTableRuleConfig.setLogicTable("t_order_item");
        orderItemTableRuleConfig.setActualDataNodes("ds_trans_${0..1}.t_order_item_${0..1}");
        shardingRuleConfig.getTableRuleConfigs().add(orderItemTableRuleConfig);

        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");

        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", ModuloShardingDatabaseAlgorithm.class.getName()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", ModuloShardingDatabaseAlgorithm.class.getName()));
        return new ShardingDataSource(shardingRuleConfig.build(createDataSourceMap()));
    }

    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(2, 1);
        result.put("ds_trans_0", DataSourceUtil.createDataSource("ds_trans_0"));
        result.put("ds_trans_1", DataSourceUtil.createDataSource("ds_trans_1"));
        return result;
    }

//    private static SoftTransactionConfiguration getSoftTransactionConfiguration(final DataSource dataSource) {
//        SoftTransactionConfiguration result = new SoftTransactionConfiguration(dataSource);
//        if (useNestedJob) {
//            result.setBestEffortsDeliveryJobConfiguration(Optional.of(new NestedBestEffortsDeliveryJobConfiguration()));
//        }
//        result.setTransactionLogDataSource(createTransactionLogDataSource());
//        return result;
//    }
}
