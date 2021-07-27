package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMarketMapper{
    CourseMarket findById(String courseId);

    void updateCourseMarket(CourseMarket courseMarket);
}
