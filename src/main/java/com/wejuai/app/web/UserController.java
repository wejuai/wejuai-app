package com.wejuai.app.web;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.HobbyInfo;
import com.wejuai.dto.response.KeyValue;
import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "用户信息相关")
@RestController
@RequestMapping("/user")
public class UserController {

    private final WejuaiCoreClient wejuaiCoreClient;

    public UserController(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/{id}")
    public UserInfo getUserInfo(@PathVariable String id, HttpSession session) {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getUser(id, watchUserId);
    }

    @ApiOperation("昵称模糊搜索用户列表")
    @GetMapping("/nickName")
    public Slice<KeyValue> getUserInfo(@RequestParam(required = false, defaultValue = "") String chars,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "15") int size) {
        return wejuaiCoreClient.getUsersByNickName(chars, page, size);
    }

    @ApiOperation("获取爱好列表")
    @GetMapping("/hobby")
    public List<HobbyInfo> getUserHobbies(@RequestParam(required = false, defaultValue = "") String userId, HttpSession session,
                                          @ApiParam("是否查看自己,默认false,true的话userId不传")
                                          @RequestParam(required = false, defaultValue = "false") boolean own) {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        userId = own ? watchUserId : userId;
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(watchUserId)) {
            throw new BadRequestException("查看的user和查看user都为空");
        }
        return wejuaiCoreClient.getUserHobbies(userId, watchUserId);
    }

}
