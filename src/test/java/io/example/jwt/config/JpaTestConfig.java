package io.example.jwt.config;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import io.example.jwt.config.p6spy.P6spyLogMessageFormatConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import(P6spyLogMessageFormatConfiguration.class)
public class JpaTestConfig extends BaseTestAnnotations {

    @Autowired
    EntityManager entityManager;

    public void flush(){
        entityManager.flush();
    }

    public void clear(){
        entityManager.clear();
    }

    public void flushAndClearPersistContext(){
        entityManager.flush();
        entityManager.clear();
    }
}