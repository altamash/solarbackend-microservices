package com.solaramps.api.it.config.tenant;

import com.solaramps.api.AppConstants;
import com.solaramps.api.saas.model.tenant.MasterTenant;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public final class DataSourceUtilIT {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceUtilIT.class);

    public static DataSource createAndConfigureDataSource(MasterTenant masterTenant) {
        HikariDataSource ds = new HikariDataSource();
        String url = masterTenant.getUrl();
        String[] urlParts = url.split("@");
        String jdbcUrl = urlParts[urlParts.length - 1];
        String userNamePass =
                url.substring(0, url.indexOf(jdbcUrl) - 1).substring(AppConstants.API_DATABASE_PREFIX.length());
        String[] userNamePassParts = userNamePass.split(":");
        ds.setUsername(userNamePassParts[0]);
        ds.setPassword(userNamePassParts[1]);
        jdbcUrl = AppConstants.API_DATABASE_PREFIX + jdbcUrl;
        ds.setJdbcUrl(jdbcUrl);
        ds.setDriverClassName(masterTenant.getDriverClass());
        // HikariCP settings - could come from the master_tenant table but
        // hardcoded here for brevity
        // Maximum waiting time for a connection from the pool
//        ds.setConnectionTimeout(240000);//
//        ds.setConnectionTimeout(20000);
        ds.setConnectionTimeout(240000);
        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(5);//
//        ds.setMinimumIdle(2);//
        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(250);
//        ds.setMaximumPoolSize(20);
        // Maximum time that a connection is allowed to sit idle in the pool
//        ds.setIdleTimeout(300000);//
        ds.setIdleTimeout(1800);// The HikariCP idleTimeout should be less than or equal to the MySQL wait_timeout
//        ds.setIdleTimeout(1500000);
//        ds.setIdleTimeout(120000);//
//        ds.setConnectionTimeout(20000);
//        ds.setMaxLifetime(1800000);//Added
        ds.setLeakDetectionThreshold(300000);
        // Setting up a pool name for each tenant datasource
        String tenantConnectionPoolName = masterTenant.getDbName() + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        LOG.info("Configured datasource:" + masterTenant.getDbName() + ". Connection pool name:" + tenantConnectionPoolName);
        return ds;
    }
}
