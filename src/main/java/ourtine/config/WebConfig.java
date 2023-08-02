package ourtine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ourtine.converter.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
       registry.addConverter(new DayConverter());
       registry.addConverter(new SortConverter());
       registry.addConverter(new EmotionConverter());
       registry.addConverter(new HabitStatusConverter());
       registry.addConverter(new HabitFollowerStatusConverter());
    }
}
