package com.taild.expresstix.infrastructure.persistence.repository;

import com.taild.expresstix.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HiDomainRepositoryImpl implements HiDomainRepository {

    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }
}
