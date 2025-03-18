package com.taild.expresstix.application.service.ticket.impl;

import com.taild.expresstix.application.model.cache.TicketDetailCache;
import com.taild.expresstix.application.service.ticket.TicketDetailAppService;
import com.taild.expresstix.application.service.ticket.cache.TicketDetailCacheService;
import com.taild.expresstix.domain.model.dto.TicketDetailDTO;
import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.repository.ticket.TicketDetailRepository;
import com.taild.expresstix.infrastructure.cache.redis.RedisInfraService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.taild.expresstix.application.constant.redis.CacheConstant.CACHE_STOCK_PREFIX;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TicketDetailRepository ticketDetailRepository;
    @Autowired
    private RedisInfraService redisInfraService;


    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        TicketDetailDTO ticketDetail;
        TicketDetailCache ticketCache = ticketDetailCacheService.getTicketDetailCache(ticketId, version);
        ticketDetail = modelMapper.map(ticketCache, TicketDetailDTO.class);
        ticketDetail.setVersion(ticketCache.getVersion());
        return ticketDetail;
    }


    @Override
    public boolean decreaseTicketStock(Long ticketId, int quantity) {

        int stockAvailable = decreaseStockCacheByLUA(ticketId, quantity);

        return ticketDetailRepository.decreaseTicketStock(ticketId, quantity) > 0;
    }

    public int decreaseStockCacheByLUA(Long ticketId, Integer quantity) {
        String keyStockLUA = CACHE_STOCK_PREFIX + ticketId;
        String luaScript = "local stock = tonumber(redis.call('GET', KEYS[1])); " +
                "if (stock >= tonumber(ARGV[1])) then " +
                "   redis.call('SET', KEYS[1], stock - tonumber(ARGV[1])); " +
                "   return stock; " +
                "end; " +
                "   return 0; ";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisInfraService.getRedisTemplate().execute(
                redisScript, Collections.singletonList(keyStockLUA), quantity);

        return result.intValue();
    }


    @Override
    public Long getAvailableStock(Long ticketId) {
        TicketDetailCache ticketDetailCache = ticketDetailCacheService.getTicketDetailCache(ticketId, System.currentTimeMillis());
        if (ticketDetailCache != null) {
            return ticketDetailCache.getStockAvailable();
        }

        return 0L;
    }

    @Override
    public List<TicketDetailDTO> getAll() {
        List<TicketDetailEntity> entities = ticketDetailRepository.findAll();

        return entities.stream().map(
                entity -> modelMapper.map(entity, TicketDetailDTO.class)
        ).toList();
    }
}
