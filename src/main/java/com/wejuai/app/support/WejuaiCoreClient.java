package com.wejuai.app.support;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.dto.request.SortType;
import com.wejuai.dto.response.*;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.GiveType;
import com.wejuai.entity.mysql.RewardDemandStatus;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wejuai.app.config.Constant.MAPPER;

/**
 * Created by ZM.Wang
 */
public class WejuaiCoreClient {

    private final RestTemplate restTemplate;
    private final String url;

    public WejuaiCoreClient(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(MAPPER));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        this.restTemplate.setInterceptors(interceptors);
    }

    public ArticleInfo getArticle(String id, String userId) {
        try {
            return restTemplate.getForObject(url + "/app/article/{id}?watchUserId={userId}", ArticleInfo.class, id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ArticleListInfo> getArticles(String titleStr, String userId, String watchUserId, String hobbyId, int page, int size,
                                              Sort.Direction direction, SortType sortType) {
        try {
            return restTemplate.exchange(url + "/app/article?titleStr={titleStr}&userId={userId}&watchUserId={watchUserId}&hobby={hobby}&page={page}&size={size}&direction={direction}&sortType={sortType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ArticleListInfo>>() {
                    }, titleStr, userId, watchUserId, hobbyId, page, size, direction, sortType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public RewardDemandInfo getRewardDemandInfo(String id, String watchUserId) {
        try {
            return restTemplate.getForObject(url + "/app/rewardDemand/{id}?watchUserId={watchUserId}", RewardDemandInfo.class, id, watchUserId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RewardDemandListInfo> getRewardDemands(String titleStr, String userId, String watchUserId, String hobbyId, RewardDemandStatus status, int page, int size,
                                                        Sort.Direction direction, SortType sortType) {
        try {
            return restTemplate.exchange(url + "/app/rewardDemand?titleStr={titleStr}&userId={userId}&watchUserId={watchUserId}&hobby={hobby}&status={status}&page={page}&size={size}&direction={direction}&sortType={sortType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<RewardDemandListInfo>>() {
                    }, titleStr, userId, watchUserId, hobbyId, status, page, size, direction, sortType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public UserInfo getUser(String userId, String watchUserId) {
        try {
            return restTemplate.getForObject(url + "/base/user/" + userId + "?watchUserId=" + watchUserId, UserInfo.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<KeyValue> getUsersByNickName(String chars, int page, int size) {
        try {
            return restTemplate.exchange(url + "/base/user/nickName?chars={chars}&page={page}&size={size}",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<Slice<KeyValue>>() {
                    }, "%" + chars + "%", page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<CommentInfo> getComments(AppType appType, String appId, String userId, String watchUserId, long page, long size) {
        try {
            return restTemplate.exchange(url + "/comment?appType={appType}&appId={appId}&userId={userId}&watchUserId={watchUserId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<CommentInfo>>() {
                    }, appType, appId, userId, watchUserId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<SubCommentInfo> getSubComments(String commentId, String userId, String watchUserId, long page, long size) {
        try {
            return restTemplate.exchange(url + "/comment/sub?commentId={commentId}&userId={userId}&watchUserId={watchUserId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<SubCommentInfo>>() {
                    }, commentId, userId, watchUserId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RemindInfo> getReminds(String userId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/comment/remind?recipient={userId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<RemindInfo>>() {
                    }, userId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<SystemMessageInfo> getSystemMessages(String userId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/comment/systemMessage?userId={userId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<SystemMessageInfo>>() {
                    }, userId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<EvaluateInfo> getEvaluates(String appId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/evaluate?appId={appId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<EvaluateInfo>>() {
                    }, appId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public HobbyInfo getHobbyName(String id) {
        try {
            return restTemplate.getForObject(url + "/hobby/{id}/name", HobbyInfo.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public List<HobbyInfo> getRecommendHobbies() {
        try {
            return restTemplate.exchange(url + "/hobby/recommend", HttpMethod.GET, null, new ParameterizedTypeReference<List<HobbyInfo>>() {
            }).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ArticleListInfo> getArticleByGiveType(String userId, GiveType giveType, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/article/{userId}/give?page={page}&size={size}&giveType={giveType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ArticleListInfo>>() {
                    }, userId, page, size, giveType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RewardDemandListInfo> getRewardDemandsByGiveType(String userId, GiveType giveType, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/rewardDemand/{userId}/give?page={page}&size={size}&giveType={giveType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<RewardDemandListInfo>>() {
                    }, userId, page, size, giveType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public List<HobbyInfo> getUserHobbies(String userId, String watchUserId) {
        try {
            return restTemplate.exchange(url + "/hobby/{userId}/user?watchUserId={watchUserId}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<HobbyInfo>>() {
                    }, userId, watchUserId).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public List<CelestialBodyInfo> getStarDomain(double minX, double maxX, double minY, double maxY, String watchUserId) {
        try {
            return restTemplate.exchange(url + "/celestialBody/starDomain?minX={minX}&maxX={maxX}&minY={minY}&maxY={maxY}&watchUserId={watchUserId}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<CelestialBodyInfo>>() {
                    }, minX, maxX, minY, maxY, watchUserId).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public CelestialBodyInfo getCelestialBodyByUser(String userId, boolean own) {
        try {
            return restTemplate.exchange(url + "/celestialBody/{userId}/user?own={own}", HttpMethod.GET, null,
                    CelestialBodyInfo.class, userId, own).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public CelestialBodyImageOssKey getCelestialBodyImageOssKey(String id) {
        try {
            return restTemplate.exchange(url + "/celestialBody/{id}/image", HttpMethod.GET, null,
                    CelestialBodyImageOssKey.class, id).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public CelestialBodyInfo getCelestialBody(String id) {
        try {
            return restTemplate.exchange(url + "/celestialBody/{id}", HttpMethod.GET, null,
                    CelestialBodyInfo.class, id).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public CelestialBodyInfo getCelestialBodyByHobby(String userId) {
        try {
            return restTemplate.exchange(url + "/celestialBody/{hobbyId}/hobby", HttpMethod.GET, null,
                    CelestialBodyInfo.class, userId).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public List<HobbyInfo> geUserCelestialBodyHobbies(String id, String watchUserId) {
        try {
            return restTemplate.exchange(url + "/celestialBody/{id}/user/hobby?watchUserId={watchUserId}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<HobbyInfo>>() {
                    }, id, watchUserId).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public GetHobbyDomain getHobbyDomainByTab(String userId, String tab) {
        try {
            return restTemplate.getForObject(url + "/hobby/tab?userId={userId}&tab={tab}", GetHobbyDomain.class, userId, tab);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RewardSubmissionInfo> getRewardSubmissions(String id, String watchUserId, boolean self, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/rewardDemand/{id}/rewardSubmission?watchUserId={watchUserId}&self={self}&page={page}&size={size}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<Slice<RewardSubmissionInfo>>() {
                    }, id, watchUserId, self, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }


    public static class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String headerName;
        private final String headerValue;

        HeaderRequestInterceptor(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpRequest wrapper = new HttpRequestWrapper(request);
            wrapper.getHeaders().set(headerName, headerValue);
            return execution.execute(wrapper, body);
        }
    }
}
