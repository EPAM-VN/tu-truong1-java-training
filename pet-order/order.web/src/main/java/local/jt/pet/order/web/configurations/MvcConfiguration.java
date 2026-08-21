package local.jt.pet.order.web.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                // 0-based index of the URL path segment containing the version
                .usePathSegment(1)
                .addSupportedVersions("1.0", "2.0")
                .setDefaultVersion("1.0");
    }
}
