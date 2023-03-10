package 878.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import 878.api.interceptor.LoginInterceptor;
import 878.core.common.config.IntegerCommonEnumConverterFactory;
import 878.core.common.config.StringCommonEnumConverterFactory;
import 878.core.common.serialize.CommonEnumSerializer;
import 878.core.module.session.service.SessionService;
import tech.ibit.mybatis.CommonEnum;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Web配置
 *
 * @author IBIT程序猿
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionService sessionService;

    /**
     * 增加登陆拦截器
     *
     * @param registry 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 增加认证的拦截器
        registry.addInterceptor(new LoginInterceptor(sessionService))
                .excludePathPatterns(getIgnorePaths());
    }

    private List<String> getIgnorePaths() {
        return Arrays.asList(
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/v2/api-docs",
                "/swagger-resources/configuration/security");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 这里这么做，主要是因为swagger测试的时候，默认用的枚举的名称
        registry.addConverterFactory(new StringCommonEnumConverterFactory());
        registry.addConverterFactory(new IntegerCommonEnumConverterFactory());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN));
        stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        converters.add(customJackson2HttpMessageConverter());
    }

    /**
     * 序列化
     */
    private MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        SimpleModule module = new SimpleModule();
        module.addSerializer(CommonEnum.class, new CommonEnumSerializer(CommonEnum.class));
        objectMapper.registerModule(module);

        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}
