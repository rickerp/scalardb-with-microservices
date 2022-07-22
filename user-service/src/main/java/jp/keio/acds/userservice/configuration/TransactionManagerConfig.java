package jp.keio.acds.userservice.configuration;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import com.scalar.db.service.TransactionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;

@Configuration
public class TransactionManagerConfig {

    private static final String SCALARDB_PROPERTIES =
            System.getProperty("user.dir") + File.separator + "src/main/resources/scalardb.properties";

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
