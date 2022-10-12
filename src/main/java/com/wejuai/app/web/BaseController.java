package com.wejuai.app.web;

import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.HobbyInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZM.Wang
 */
@Api(tags = "基础接口")
@RestController
@RequestMapping("/base")
public class BaseController {

    private final WejuaiCoreClient wejuaiCoreClient;

    public BaseController(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取爱好名称")
    @GetMapping("/hobby/{id}/name")
    public HobbyInfo getHobbyName(@PathVariable String id) {
        return wejuaiCoreClient.getHobbyName(id);
    }

    @Deprecated
    @ApiOperation("获取推荐爱好列表，7条")
    @GetMapping("/hobby/recommends")
    public List<HobbyInfo> getRecommendHobbies() {
        return wejuaiCoreClient.getRecommendHobbies();
    }
}
