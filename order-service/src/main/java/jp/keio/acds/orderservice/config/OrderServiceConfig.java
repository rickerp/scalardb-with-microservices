package jp.keio.acds.orderservice.config;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import com.scalar.db.service.TransactionFactory;

import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OrderServiceConfig {

    private static final String SCALARDB_PROPERTIES =
            System.getProperty("user.dir") + File.separator + "scalardb.properties";

    @Bean
    @Scope("singleton")
    DistributedTransactionManager createScalarDistributedTransactionManager() throws IOException {
        TransactionFactory factory = TransactionFactory.create(SCALARDB_PROPERTIES);
        return factory.getTransactionManager();
    }

    @Bean
    @Scope("singleton")
    TwoPhaseCommitTransactionManager createScalarTwoPhaseCommitTransactionManager() throws IOException {
        TransactionFactory factory = TransactionFactory.create(SCALARDB_PROPERTIES);
        return factory.getTwoPhaseCommitTransactionManager();
    }
}
