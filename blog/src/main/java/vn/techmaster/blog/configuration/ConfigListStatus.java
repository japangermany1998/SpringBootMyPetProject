package vn.techmaster.blog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConfigListStatus implements WebMvcConfigurer {
    @Bean
    public static List<String> listStatus(){
        List<String> list=new ArrayList<>();
        list.add("Not Fixed");
        list.add("Fixed");
        list.add("Escalate");
        return list;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path bugRequest = Paths.get("/bugRequest");
        String uploadPath=bugRequest.toFile().getAbsolutePath();

        registry.addResourceHandler("/bugRequest/**").addResourceLocations("file:/"+uploadPath+"/");
    }
}
