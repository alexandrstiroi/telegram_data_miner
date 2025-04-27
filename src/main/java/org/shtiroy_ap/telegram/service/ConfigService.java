package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.entity.Config;
import org.shtiroy_ap.telegram.repository.ConfigRepository;
import org.shtiroy_ap.telegram.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfigService {
    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository){
        this.configRepository = configRepository;
    }

    public Config getLastTender(){
        return configRepository.findByParament("LAST_TENDER_QUERY").orElse(
                new Config("LAST_TENDER_QUERY", DateUtil.dateTimeToStr(LocalDateTime.now().minusHours(12)), LocalDateTime.now()));
    }

    public Config update(Config config){
        return configRepository.save(config);
    }
}
