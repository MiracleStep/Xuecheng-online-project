package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "XC-SERVICE-MANAGE-CMS")  //指定远程调用的服务名
@RequestMapping("/cms/page")
public interface CmsPageClient {

    //根据页面id查询页面信息,远程调用cms请求数据
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id);

    //添加页面，用于课程预览
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage);

    //一键发布页面
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
