package com.wejuai.app.service;

import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.RewardDemandInfo;
import com.wejuai.dto.response.RewardDemandListInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.GiveType;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class RewardDemandService {

    private final WejuaiCoreClient wejuaiCoreClient;

    public RewardDemandService(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    public RewardDemandInfo getRewardDemandInfo(String id, String watchUserId) {
        return wejuaiCoreClient.getRewardDemandInfo(id, watchUserId);
    }

    public Slice<RewardDemandListInfo> getByStar(String userId, int page, int size) {
        return wejuaiCoreClient.getRewardDemandsByGiveType(userId, GiveType.STAR, page, size);
    }

    public Slice<RewardDemandListInfo> getByCollection(String userId, int page, int size) {
        return wejuaiCoreClient.getRewardDemandsByGiveType(userId, GiveType.COLLECT, page, size);
    }

}
