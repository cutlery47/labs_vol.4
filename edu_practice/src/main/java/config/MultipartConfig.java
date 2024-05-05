package config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
// Класс конфигурации для pdf-файла
// Можно не добавлять в отчет
public class MultipartConfig {

    @Bean
    public MultipartResolver getMultipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
