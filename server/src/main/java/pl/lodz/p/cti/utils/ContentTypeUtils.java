package pl.lodz.p.cti.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mateusz Wieczorek on 03.07.2017.
 */
@Slf4j
@Data
@Component
public class ContentTypeUtils implements InitializingBean {

    private List<String> contentTypes;

    @NotNull
    @Value("${app.extension}")
    private String extension;

    @Override
    public void afterPropertiesSet() throws Exception {
        contentTypes = Arrays.asList(extension.split(","));
    }

    public boolean isSupported(String contentType) {
        log.info("Checking that '{}' is supported", contentType);
        return !StringUtils.isBlank(contentType) &&
                contentTypes
                        .stream()
                        .filter(it -> it.equalsIgnoreCase(contentType))
                        .findAny()
                        .isPresent();
    }
}
