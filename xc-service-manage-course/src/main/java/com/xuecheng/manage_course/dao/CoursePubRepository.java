package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator.
 */
public interface CoursePubRepository extends JpaRepository<CoursePub,String>, JpaSpecificationExecutor<CoursePub> {
}
