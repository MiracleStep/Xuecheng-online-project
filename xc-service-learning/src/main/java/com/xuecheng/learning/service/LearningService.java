package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.response                                            .GetMediaResult;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningService {

    @Autowired
    CourseSearchClient courseSearchClient;

    //获取课程学习地址（视频播放地址）
    public GetMediaResult getmedia(String courseId, String teachplanId) {
        //远程调用搜索服务查询课程计划所对应的课程媒资信息
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getmedia(teachplanId);
        if(teachplanMediaPub==null|| StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())){
            //获取学习地址错误
            ExceptionCast.cast(LearningCode.CMS_ADDPAGE_EXISTSNAME);
        }
        return new GetMediaResult(CommonCode.SUCCESS,
                teachplanMediaPub,
                teachplanMediaPub.getMediaUrl());
    }
}