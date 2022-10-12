package com.wejuai.app.web;

import com.wejuai.app.service.RewardDemandService;
import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.request.SortType;
import com.wejuai.dto.response.RewardDemandInfo;
import com.wejuai.dto.response.RewardDemandListInfo;
import com.wejuai.dto.response.RewardSubmissionInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.RewardDemandStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "悬赏相关")
@RestController
@RequestMapping("/rewardDemand")
public class RewardDemandController {

    private final RewardDemandService rewardDemandService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public RewardDemandController(RewardDemandService rewardDemandService, WejuaiCoreClient wejuaiCoreClient) {
        this.rewardDemandService = rewardDemandService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取悬赏列表")
    @GetMapping
    public Slice<RewardDemandListInfo> getRewardDemands(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                        @RequestParam(required = false, defaultValue = "") String hobbyType,
                                                        @ApiParam("查看其他用户的列表的用户id") @RequestParam(required = false, defaultValue = "") String userId,
                                                        @RequestParam(required = false, defaultValue = "") String titleStr,
                                                        @RequestParam(required = false, defaultValue = "") RewardDemandStatus status,
                                                        @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
                                                        @RequestParam(required = false, defaultValue = "TIME") SortType sortType,
                                                        @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getRewardDemands(titleStr, userId, watchUserId, hobbyType.toLowerCase(), status, page, size, direction, sortType);
    }

    @ApiOperation("获取悬赏详细信息")
    @GetMapping("/{id}")
    public RewardDemandInfo getRewardDemandInfo(@PathVariable String id,
                                                @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return rewardDemandService.getRewardDemandInfo(id, watchUserId);
    }

    @ApiOperation("悬赏回答列表")
    @GetMapping("/{id}/rewardSubmission")
    public Slice<RewardSubmissionInfo> getRewardSubmissions(@ApiParam("悬赏id") @PathVariable String id,
                                                            @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId,
                                                            @RequestParam(required = false, defaultValue = "false") boolean self,
                                                            @RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getRewardSubmissions(id, watchUserId, self, page, size);
    }

    @ApiOperation("获取用户点赞的悬赏列表")
    @GetMapping("/star")
    public Slice<RewardDemandListInfo> getRewardDemandsByStar(HttpSession session,
                                                              @RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "10") int size) {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return rewardDemandService.getByStar(watchUserId, page, size);
    }

    @ApiOperation("获取用户收藏的悬赏列表")
    @GetMapping("/collection")
    public Slice<RewardDemandListInfo> getRewardDemandsByCollection(HttpSession session,
                                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return rewardDemandService.getByCollection(watchUserId, page, size);
    }
}
