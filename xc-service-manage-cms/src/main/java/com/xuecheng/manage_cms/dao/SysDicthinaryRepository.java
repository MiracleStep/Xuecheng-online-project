package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDicthinaryRepository extends MongoRepository<SysDictionary,String> {
    public SysDictionary findByDType(String type);
}
