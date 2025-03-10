package com.taild.expresstix.application.service.event.impl;

import com.taild.expresstix.application.service.event.EventAppService;
import com.taild.expresstix.domain.service.HiDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EventAppServiceImpl implements EventAppService {

    @Resource
    private HiDomainService hiDomainService;

    @Override
    public String sayHi(String name) {
        return hiDomainService.sayHi(name);
    }
}
