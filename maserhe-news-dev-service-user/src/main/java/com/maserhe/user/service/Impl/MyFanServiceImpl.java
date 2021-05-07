package com.maserhe.user.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maserhe.entity.FansDo;
import com.maserhe.entity.UserDo;
import com.maserhe.entity.VO.RegionRatioVO;
import com.maserhe.enums.Sex;
import com.maserhe.mapper.FansMapper;
import com.maserhe.user.service.MyFanService;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.PagedGridResult;
import com.maserhe.utils.RedisOperator;
import com.tencentcloudapi.iot.v20180123.models.AppUser;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

import static com.maserhe.api.BaseController.REDIS_MY_FOLLOW_COUNTS;
import static com.maserhe.api.BaseController.REDIS_WRITER_FANS_COUNTS;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 17:39
 */
@Service
public class MyFanServiceImpl implements MyFanService {
    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Autowired
    private RedisOperator redis;

    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {

        FansDo fan = new FansDo();
        fan.setFanId(fanId);
        fan.setWriterId(writerId);

        int count = fansMapper.selectCount(fan);

        return count > 0 ? true : false;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {
        // 获得粉丝用户的信息

        UserDo fanInfo = userService.getUserById(fanId);

        // AppUser fanInfo = userService.getUser(fanId);

        String fanPkId = sid.nextShort();

        FansDo fans = new FansDo();
        fans.setId(fanPkId);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);

        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());

        fansMapper.insert(fans);

        // redis 作家粉丝数累加
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        // redis 当前用户的（我的）关注数累加
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {

        FansDo fans = new FansDo();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);

        fansMapper.delete(fans);

        // redis 作家粉丝数累减
        redis.decrement(REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        // redis 当前用户的（我的）关注数累减
        redis.decrement(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Override
    public PagedGridResult queryMyFansList(String writerId,
                                           Integer page,
                                           Integer pageSize) {
        FansDo fans = new FansDo();
        fans.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<FansDo> list = fansMapper.select(fans);
        return setterPagedGrid(list, page);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        FansDo fans = new FansDo();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);

        Integer count = fansMapper.selectCount(fans);
        return count;
    }

    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        FansDo fans = new FansDo();
        fans.setWriterId(writerId);

        List<RegionRatioVO> list = new ArrayList<>();
        for (String r : regions) {
            fans.setProvince(r);
            Integer count = fansMapper.selectCount(fans);

            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(r);
            regionRatioVO.setValue(count);

            list.add(regionRatioVO);
        }

        return list;
    }


    @Override
    public Integer queryAllFansCounts(String writerId) {
        Example example = new Example(FansDo.class);
        example.createCriteria().andEqualTo("writerId", writerId);
        return fansMapper.selectCountByExample(example);
    }

    private PagedGridResult setterPagedGrid(List<?> adminUserList, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(adminUserList);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRecords(pageList.getPages());
        result.setTotal(pageList.getTotal());
        result.setRows(pageList.getList());
        return result;
    }

    @Override
    public Integer queryAllStarCounts(String userId) {
        Example example = new Example(FansDo.class);
        example.createCriteria().andEqualTo("fanId", userId);
        return fansMapper.selectCountByExample(example);
    }
}
