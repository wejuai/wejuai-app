package com.wejuai.app.web;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.GetHobbyDomain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "爱好相关")
@RestController
@RequestMapping("/hobby")
public class HobbyController {

    private final WejuaiCoreClient wejuaiCoreClient;

    public HobbyController(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("搜索爱好")
    @GetMapping("/search")
    public GetHobbyDomain getHobbyDomainByTab(String tab, HttpSession session) {
        if (StringUtils.isBlank(tab)) {
            throw new BadRequestException("关键字必须填写哦~");
        }
        if (tab.length() > 50) {
            throw new BadRequestException("太~~太长了啦~");
        }
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = session.getId().replaceAll("-", "");
        }
        return wejuaiCoreClient.getHobbyDomainByTab(watchUserId, tab);
    }
}
