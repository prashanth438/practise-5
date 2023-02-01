package 878.api;

import 878.core.common.config.CommonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author IBIT程序猿
 */
@SpringBootApplication(scanBasePackages = {
        "878"
})
@MapperScan(basePackages = {
        "878.db.mapper"
})
@EnableAsync
@Import(CommonConfig.class)
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
