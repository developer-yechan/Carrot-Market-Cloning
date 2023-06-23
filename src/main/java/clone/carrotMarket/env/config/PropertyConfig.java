package clone.carrotMarket.env.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/env.properties")
public class PropertyConfig {
}
