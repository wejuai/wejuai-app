package com.wejuai.app.service;

import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.ArticleListInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mysql.GiveType;
import org.springframework.stereotype.Service;

/**
 * Created by ZM.Wang
 */
@Service
public class ArticleService {

    private final WejuaiCoreClient wejuaiCoreClient;

    public ArticleService(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    public ArticleInfo getArticle(String id, String watchUserId) {
        return wejuaiCoreClient.getArticle(id, watchUserId);
    }

    public Slice<ArticleListInfo> getByStar(String userId, int page, int size) {
        return wejuaiCoreClient.getArticleByGiveType(userId, GiveType.STAR, page, size);
    }

    public Slice<ArticleListInfo> getByCollection(String userId, int page, int size) {
        return wejuaiCoreClient.getArticleByGiveType(userId, GiveType.COLLECT, page, size);
    }

}
