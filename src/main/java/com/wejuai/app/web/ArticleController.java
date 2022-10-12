package com.wejuai.app.web;

import com.wejuai.app.service.ArticleService;
import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.request.SortType;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.ArticleListInfo;
import com.wejuai.dto.response.EvaluateInfo;
import com.wejuai.dto.response.Slice;
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

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * Created by ZM.Wang
 */
@Api(tags = "文章相关")
@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public ArticleController(ArticleService articleService, WejuaiCoreClient wejuaiCoreClient) {
        this.articleService = articleService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取单个文章详细信息")
    @GetMapping("/{id}")
    public ArticleInfo getArticle(@PathVariable String id, @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return articleService.getArticle(id, watchUserId);
    }

    @ApiOperation("获取文章分页列表")
    @GetMapping
    public Slice<ArticleListInfo> getArticles(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(required = false, defaultValue = "") String hobbyType,
                                              @ApiParam("查看其他用户的列表的用户id")
                                              @RequestParam(required = false, defaultValue = "") String userId,
                                              @RequestParam(required = false, defaultValue = "") String titleStr,
                                              @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
                                              @RequestParam(required = false, defaultValue = "TIME") SortType sortType,
                                              @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getArticles(titleStr, userId, watchUserId, hobbyType.toLowerCase(), page, size, direction, sortType);
    }

    @ApiOperation("获取文章评价分页")
    @GetMapping("/evaluate")
    public Slice<EvaluateInfo> getEvaluates(@RequestParam @ApiParam("文章id") String appId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getEvaluates(appId, page, size);
    }

    @ApiOperation("获取用户点赞的文章列表")
    @GetMapping("/star")
    public Slice<ArticleListInfo> getArticlesByStar(@SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId,
                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return articleService.getByStar(watchUserId, page, size);
    }

    @ApiOperation("获取用户收藏的文章列表")
    @GetMapping("/collection")
    public Slice<ArticleListInfo> getArticlesByCollection(@SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return articleService.getByCollection(watchUserId, page, size);
    }

}
