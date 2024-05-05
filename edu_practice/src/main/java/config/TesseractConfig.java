package config;


import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
// Класс конфигурации для Tesseract
// Можно не добавлять в отчет
public class TesseractConfig {

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Programming\\leti\\edu_practice\\src\\main\\resources\\tessdata");
        tesseract.setLanguage("eng+rus");

        return tesseract;
    }
}
