package com.maserhe.user.service.Impl;

import com.maserhe.entity.VO.RegionRatioVO;
import com.maserhe.enums.Sex;
import com.maserhe.user.service.MyFanService;
import com.maserhe.utils.PagedGridResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 17:39
 */
@Service
public class MyFanServiceImpl implements MyFanService {
    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        return false;
    }

    @Override
    public void follow(String writerId, String fanId) {

    }

    @Override
    public void unfollow(String writerId, String fanId) {

    }

    @Override
    public PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        return null;
    }

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        return null;
    }
}
