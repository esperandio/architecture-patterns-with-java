package app.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import app.services.UnitOfWork;
import app.persistence.HibernateUnitOfWork;

@Configuration
public class UnitOfWorkConfiguration {
    @Bean
    public UnitOfWork unitOfWork() {
        return new HibernateUnitOfWork();
    }
}
