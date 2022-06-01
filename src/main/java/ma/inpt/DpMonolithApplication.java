package ma.inpt;

import com.cloudinary.Cloudinary;
import ma.inpt.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DpMonolithApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpMonolithApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringApplicationContext springApplicationContext()
    {
        return new SpringApplicationContext();
    }

    @Bean(name="AppProperties")
    public AppProperties getAppProperties()
    {
        return new AppProperties();
    }
    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dpckdbkbe");
        config.put("api_key", "712896963797844");
        config.put("api_secret", "rmCz5BZnscJsw4-Dv5diAkohMss");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
