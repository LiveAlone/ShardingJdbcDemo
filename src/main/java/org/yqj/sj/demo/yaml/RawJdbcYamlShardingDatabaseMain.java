/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package org.yqj.sj.demo.yaml;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import org.yqj.sj.demo.util.RawJdbcRepository;

import javax.sql.DataSource;
import java.io.File;

public final class RawJdbcYamlShardingDatabaseMain {
    
    public static void main(final String[] args) throws Exception {
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(new File(
                RawJdbcYamlShardingDatabaseMain.class.getResource("/yaml/yamlShardingDatabase.yaml").getFile()));
        new RawJdbcRepository(dataSource).demo();
    }
}
