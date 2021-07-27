package com.xuecheng.manage_cms.service;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDicthinaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class SysDicthinaryService {

    @Autowired
    SysDicthinaryRepository sysDicthinaryRepository;

    public SysDictionary findListBytype(String type) {
        return sysDicthinaryRepository.findByDType(type);
    }
}
