package rk.configs;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CacheControlConfig extends WebMvcConfigurerAdapter{
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resources without Spring Security. No cache control response headers.
        registry.addResourceHandler("/app/**")
                .addResourceLocations("/app/")
                .setCachePeriod(3600 * 24);

        registry.addResourceHandler("/creatures/**")
                .addResourceLocations("/creatures/")
                .setCachePeriod(3600 * 24);

        registry.addResourceHandler("/node_modules/**")
                .addResourceLocations("/node_modules/")
                .setCachePeriod(3600 * 24);

        // Resources controlled by Spring Security, which
        // adds "Cache-Control: must-revalidate".
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                .setCachePeriod(3600 * 24);
    }
}