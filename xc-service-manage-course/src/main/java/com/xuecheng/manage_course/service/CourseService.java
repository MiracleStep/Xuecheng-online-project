package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository  courseBaseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Autowired
    CoursePubRepository coursePubRepository;

    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;



    @Value("${course???publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course???publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course???publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course???publish.siteId}")
    private String publish_siteId;
    @Value("${course???publish.templateId}")
    private String publish_templateId;
    @Value("${course???publish.previewUrl}")
    private String previewUrl;


    //????????????id??????????????????
    public TeachplanNode findTeachplanList(String courseId){

        return teachplanMapper.selectList(courseId);
    }

    //??????????????????
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
       if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname())){
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
       }
       //??????id
        String courseid = teachplan.getCourseid();
       //???????????????parentId
        String parentid = teachplan.getParentid();
        //?????????????????????parentid?????????????????????
        if(StringUtils.isEmpty(parentid)){
            //???????????????????????????
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();
        //??????????????????
        String grade = parentNode.getGrade();
        //?????????
        Teachplan teachplanNew = new Teachplan();
        //??????????????????teachplan???????????????teachplanNew?????????
        BeanUtils.copyProperties(teachplan,teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        //??????,?????????????????????????????????
        if(grade.equals("1")){
            teachplanNew.setGrade("2");
        }else {
            teachplanNew.setGrade("3");
        }

        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //??????teachplan??????????????????(?????????????????????)
    private String getTeachplanRoot(String courseId){
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId,"0");
        if(teachplanList == null||teachplanList.size()<=0){
            //???????????????????????????????????????
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }

    public QueryResponseResult<CourseInfo> findCourseList(String company_id, int page, int size, CourseListRequest courseListRequest) {
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(company_id);
        if(page <= 0){
            page = 0;
        }
        if(size<5){
            size=5;
        }
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setList(courseListPage.getResult());
        courseInfoQueryResult.setTotal(courseListPage.getTotal());
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS,courseInfoQueryResult);
    }

    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase courseBaseById = this.getCourseBaseById(id);
        if (courseBaseById != null){
            courseBaseRepository.save(courseBase);
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        if(courseBase==null){
            return new AddCourseResult(CommonCode.FAIL,null);
        }
        courseBase.setStatus("202001");
        CourseBase save = courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,save.getId());
    }

    public CourseMarket getCourseMarketById(String courseId) {
        CourseMarket courseMarket = courseMarketMapper.findById(courseId);
        if (courseMarket!=null){
            return courseMarket;
        }
        return null;
    }

    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket cs = this.getCourseMarketById(id);
        if(cs!=null){
            courseMarketMapper.updateCourseMarket(courseMarket);
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    @Transactional
    public ResponseResult addCoursePic(String courseId,String pic){
        CoursePic coursePic = null;
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            coursePic = picOptional.get();
        }
        if(coursePic==null){
            coursePic = new CoursePic();
        }

        coursePic.setPic(pic);
        coursePic.setCourseid(courseId);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //??????????????????
    public CoursePic findCoursePic(String courseId) {
        //??????????????????
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            return coursePic;
        }
        return null;
    }

    //??????????????????
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //????????????????????????????????????????????????????????????????????????????????????
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //???????????????????????????
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        //??????????????????
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(coursePic);
        }
        //???????????????????????????
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        //??????????????????
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //??????id????????????????????????
    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }
    //????????????,???????????????id
    public CoursePublishResult preview(String id) {
        //????????????
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //??????cms????????????
        //??????cmsPage??????
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//??????id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//????????????url
        cmsPage.setPageName(id+".html");//????????????
        cmsPage.setPageAliase(courseBaseById.getName());//?????????????????????????????????
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//??????????????????
        cmsPage.setPageWebPath(publish_page_webpath);//??????webpath
        cmsPage.setTemplateId(publish_templateId);
        //????????????cms
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);

        if(!cmsPageResult.isSuccess()){
            //????????????
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //?????????????????????url
        String url = previewUrl+pageId;
        //??????CoursePublicResult??????(??????????????????????????????url)
        return new CoursePublishResult(CommonCode.SUCCESS,url);
    }

    //????????????
    @Transactional
    public CoursePublishResult publish(String id) {
        //????????????
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //??????cms?????????????????????????????????????????????????????????
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//??????id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//????????????url
        cmsPage.setPageName(id+".html");//????????????
        cmsPage.setPageAliase(courseBaseById.getName());//?????????????????????????????????
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//??????????????????
        cmsPage.setPageWebPath(publish_page_webpath);//??????webpath
        cmsPage.setTemplateId(publish_templateId);//????????????id
        //??????cms?????????????????????????????????????????????????????????
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //??????????????????
        //??????????????????????????????"?????????"
        CourseBase courseBase = this.saveCoursePubState(id);
        if(courseBase == null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }


        //???????????????????????????
        //???????????????coursePub??????
        CoursePub coursePub = this.createCoursePub(id);
        //???coursePub??????????????????
        CoursePub saveCoursePub = this.saveCoursePub(id, coursePub);
        //?????????????????????
        //...


        //???????????????url
        String pageUrl = cmsPostPageResult.getPageUrl();
        //???teachplanMediaPub??????????????????
        this.saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }
    //???teachplanMediaPub??????????????????
    private void saveTeachplanMediaPub(String courseId){
        //??????????????????
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        //???teachplanMedia?????????
        List<TeachplanMedia> teachplanMedias = teachplanMediaRepository.findByCourseId(courseId);
        //???teachplanMediaList?????????teachplanMediaPub
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        //???teachplanMediaList????????????teachplanMediaPubs???
        for (TeachplanMedia teachplanMedia: teachplanMedias){
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //???????????????
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubs.add(teachplanMediaPub);
        }
        //???teachplanMediaList?????????teachplanMediaPub
        teachplanMediaPubRepository.saveAll(teachplanMediaPubs);
    }


    //???coursePub????????????????????????
    private CoursePub saveCoursePub(String id,CoursePub coursePub){
        CoursePub coursePubNew = null;
        //????????????id??????coursePub
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if(coursePubOptional.isPresent()){
            coursePubNew = coursePubOptional.get();
        }else {
            coursePubNew = new CoursePub();
        }

        //???coursePub???????????????????????????coursePubNew?????????
        BeanUtils.copyProperties(coursePub,coursePubNew);
        coursePubNew.setId(id);
        //?????????
        coursePubNew.setTimestamp(new Date());
        //????????????
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    //??????coursePub??????
    private CoursePub createCoursePub(String id){
        CoursePub coursePub = new CoursePub();
        //????????????id??????course_base
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(id);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            //???courseBase???????????????CoursePub???
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        //??????????????????
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //??????????????????
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if(courseMarketOptional.isPresent()){
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //??????????????????
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        //?????????????????????json????????????course_pub???
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    //?????????????????????????????? 202002
    private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }

    //??????????????????????????????????????????
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia)
    {
         if(teachplanMedia==null||StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
             ExceptionCast.cast(CommonCode.INVALID_PARAM);
         }
         //???????????????????????????3???
        //????????????
        String teachplanId = teachplanMedia.getTeachplanId();
        //?????????????????????
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //?????????????????????
        Teachplan teachplan = optional.get();
        //????????????
        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade)||!grade.equals("3")){
            //???????????????????????????????????????????????????
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_TEACHPLAN_GRADEERROR);
        }
        //??????teachplanMedia
        Optional<TeachplanMedia> optional1 = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia one = null;
        if(optional1.isPresent()){
            one = optional1.get();
        }else {
            one = new TeachplanMedia();
        }
        //???TeachplanMedia??????????????????
        one.setCourseId(teachplan.getCourseid());//??????id
        one.setMediaId(teachplanMedia.getMediaId());//???????????????id
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//???????????????????????????
        one.setMediaUrl(teachplanMedia.getMediaUrl());//???????????????url
        one.setTeachplanId(teachplanId);
        teachplanMediaRepository.save(one);

        return new ResponseResult(CommonCode.SUCCESS);
    }
}
