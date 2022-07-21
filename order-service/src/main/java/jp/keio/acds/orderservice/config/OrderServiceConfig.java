package jp.keio.acds.orderservice.config;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import com.scalar.db.service.TransactionFactory;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrderServiceConfig {

    private static final String SCALARDB_PROPERTIES =
            System.getProperty("user.dir") + File.separator + "scalardb.properties";

    private static final String USER_MS_URL = "http://localhost:8080";

    @Bean
    @Scope("singleton")
    WebClient createWebClient() {
        return WebClient.builder().baseUrl(USER_MS_URL).build();
    }

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
