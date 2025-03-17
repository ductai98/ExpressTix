package com.taild.expresstix.application.service.ticket;

import com.taild.expresstix.domain.model.dto.TicketDetailDTO;
import com.taild.expresstix.infrastructure.cache.redis.RedisInfraService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.taild.expresstix.application.constant.redis.CacheConstant.CACHE_STOCK_PREFIX;

@Component
@Slf4j
@AllArgsConstructor
public class RedisData {

    private TicketDetailAppService ticketDetailAppService;

    private RedisInfraService redisInfraService;

    @PostConstruct
    public void initRedisData() {
        List<TicketDetailDTO> ticketDetailDTOS = ticketDetailAppService.getAll();
        for (TicketDetailDTO ticketDetailDTO : ticketDetailDTOS) {
            log.info("Loading ticket detail into Redis: {}", ticketDetailDTO);
            redisInfraService.setObject(CACHE_STOCK_PREFIX + ticketDetailDTO.getId(), ticketDetailDTO.getStockAvailable());
        }
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void updateRedisData() { //upload redis cache every 10 seconds
        List<TicketDetailDTO> ticketDetailDTOS = ticketDetailAppService.getAll();
        for (TicketDetailDTO ticketDetailDTO : ticketDetailDTOS) {
            redisInfraService.setObject(CACHE_STOCK_PREFIX + ticketDetailDTO.getId(), ticketDetailDTO.getStockAvailable());
        }
    }
}
