package com.wejuai.app.web;

import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.CommentInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.SubCommentInfo;
import com.wejuai.dto.response.SystemMessageInfo;
import com.wejuai.entity.mongo.AppType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "评论相关")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final WejuaiCoreClient wejuaiCoreClient;

    public CommentController(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取一级评论分页")
    @GetMapping
    public Slice<CommentInfo> getComments(@RequestParam(required = false, defaultValue = "") AppType appType,
                                          @RequestParam(required = false, defaultValue = "") String appId,
                                          @RequestParam(required = false, defaultValue = "") String userId,
                                          @RequestParam(required = false, defaultValue = "0") long page,
                                          @RequestParam(required = false, defaultValue = "10") long size,
                                          @SessionAttribute(required = false, value = SESSION_LOGIN) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getComments(appType, appId, userId, watchUserId, page, size);
    }

    @ApiOperation("子评论分页列表")
    @GetMapping("/sub")
    public Slice<SubCommentInfo> getSubComments(@RequestParam(required = false, defaultValue = "") String commentId,
                                                @RequestParam(required = false, defaultValue = "") String userId,
                                                @RequestParam(required = false, defaultValue = "0") long page,
                                                @RequestParam(required = false, defaultValue = "10") long size,
                                                HttpSession session) {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getSubComments(commentId, userId, watchUserId, page, size);
    }

    @ApiOperation("当前用户艾特列表")
    @GetMapping("/remind")
    public Slice<?> getReminds(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "10") int size,
                               HttpSession session, HttpServletResponse response) throws IOException {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return wejuaiCoreClient.getReminds(watchUserId, page, size);
    }

    @ApiOperation("系统消息列表")
    @GetMapping("/systemMessage")
    public Slice<SystemMessageInfo> getSystemMessages(@RequestParam(required = false, defaultValue = "") int page,
                                                      @RequestParam(required = false, defaultValue = "") int size,
                                                      HttpSession session, HttpServletResponse response) throws IOException {
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return wejuaiCoreClient.getSystemMessages(watchUserId, page, size);
    }
}
