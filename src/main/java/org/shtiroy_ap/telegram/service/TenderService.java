package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.Config;
import org.shtiroy_ap.telegram.model.Document;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.shtiroy_ap.telegram.model.TenderDto;
import org.shtiroy_ap.telegram.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class TenderService {
    private final static Logger log = LogManager.getLogger(TenderService.class.getName());
    private final WebClient webClient;
    private final ConfigService configService;
    @Value("${service.getNew}")
    private String apiUrlNew;
    @Value("${service.getDetail}")
    private String apiUrlDetail;
    @Value("${service.getUpdate}")
    private String apiUrlUpdate;
    @Value("${service.getDocs}")
    private String apiUrlDocs;

    public TenderService(WebClient webClient, ConfigService configService){
        this.webClient = webClient;
        this.configService = configService;
    }

    public List<TenderDto> fetchTenders() {
        log.info("Скачиваем новые тендеры");
        try {
            Config config = configService.getLastTender();
            List<TenderDto> list = webClient.post()
                    .uri(apiUrlNew)
                    .bodyValue(config.getValue())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<TenderDto>>() {
                    })
                    .block();
            config.setValue(DateUtil.dateTimeToStr(LocalDateTime.now()));
            configService.update(config);
            return list;
        } catch (Exception e) {
            log.error("Ошибка при скачивании {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public TenderDetailDto fetchTenderDetail(String tenderId){
        log.info("Скачиваем подробности {}", tenderId);
        try {
            TenderDetailDto detailDto = webClient.post()
                    .uri(apiUrlDetail)
                    .bodyValue(tenderId)
                    .retrieve()
                    .bodyToMono(TenderDetailDto.class)
                    .block();
            return detailDto;
        } catch (Exception e){
            log.info("Ошибка скачивания тендера {}", tenderId);
            return null;
        }
    }

    public TenderDetailDto verifyTenderDetail(String tenderId){
        log.info("Скачиваем подробности {}", tenderId);
        try {
            TenderDetailDto detailDto = webClient.post()
                    .uri(apiUrlUpdate)
                    .bodyValue(tenderId)
                    .retrieve()
                    .bodyToMono(TenderDetailDto.class)
                    .block();
            return detailDto;
        } catch (Exception e){
            log.info("Ошибка скачивания тендера {}", tenderId);
            return null;
        }
    }

    public List<Document> fetchDocumentList(String tenderId){
        log.info("Список документов по тендеру {}", tenderId);
        try{
            return webClient.post()
                    .uri(apiUrlDocs)
                    .bodyValue(tenderId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Document>>() {})
                    .block();
        } catch (Exception ex){
            log.info("Ошибка скачивания списка документов {}", tenderId);
            return null;
        }
    }
}
