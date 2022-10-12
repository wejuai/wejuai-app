package com.wejuai.app.web;

import com.endofmaster.commons.aliyun.oss.AliyunOss;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.app.support.ImageUtils;
import com.wejuai.app.support.WejuaiCoreClient;
import com.wejuai.dto.response.CelestialBodyImageOssKey;
import com.wejuai.dto.response.CelestialBodyInfo;
import com.wejuai.dto.response.HobbyInfo;
import com.wejuai.entity.mongo.CelestialBodyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.wejuai.app.config.Constant.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "星球相关")
@RestController
@RequestMapping("/celestialBody")
public class CelestialBodyController {

    private final AliyunOss aliyunOss;
    private final WejuaiCoreClient wejuaiCoreClient;

    public CelestialBodyController(AliyunOss aliyunOss, WejuaiCoreClient wejuaiCoreClient) {
        this.aliyunOss = aliyunOss;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("获取星域")
    @GetMapping("/starDomain")
    public List<CelestialBodyInfo> getStarDomain(double minX, double maxX, double minY, double maxY,
                                                 @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.getStarDomain(minX, maxX, minY, maxY, watchUserId);
    }

    @ApiOperation("根据星球id获取信息")
    @GetMapping("/{id}")
    public CelestialBodyInfo getCelestialBody(@PathVariable String id) {
        return wejuaiCoreClient.getCelestialBody(id);
    }

    @ApiOperation("获取合并后图片")
    @GetMapping("/{id}/image")
    public void celestialBodyImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        CelestialBodyImageOssKey celestialBodyImageOssKey = wejuaiCoreClient.getCelestialBodyImageOssKey(id);
        InputStream back = aliyunOss.download(celestialBodyImageOssKey.getBack());
        InputStream up = aliyunOss.download(celestialBodyImageOssKey.getUp());
        response.setContentType("image/png");
        response.addHeader("Content-Disposition", "inline; filename=" + id + ".png");
        InputStream background = this.getClass().getResourceAsStream("/share-background.png");
        ImageUtils.fusionImage(background, back, up, response.getOutputStream(), celestialBodyImageOssKey.getAngle());
    }

    @ApiOperation("获取星球信息")
    @GetMapping
    public CelestialBodyInfo getCelestialBody(@ApiParam("类型的id") @RequestParam String id,
                                              @ApiParam("星球类型") @RequestParam CelestialBodyType type,
                                              @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        if (type == CelestialBodyType.USER) {
            return wejuaiCoreClient.getCelestialBodyByUser(id, StringUtils.equals(watchUserId, id));
        }
        if (type == CelestialBodyType.HOBBY) {
            return wejuaiCoreClient.getCelestialBodyByHobby(id);
        }
        throw new BadRequestException("没有该星球类型: " + type);
    }

    @ApiOperation(value = "获取用户星球的爱好列表", notes = "如果是自己的会返回全部，别人的返回公开")
    @GetMapping("/{id}/user/hobby")
    public List<HobbyInfo> geUserCelestialBodyHobbies(@PathVariable String id, @SessionAttribute(value = SESSION_LOGIN, required = false) String watchUserId) {
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = "";
        }
        return wejuaiCoreClient.geUserCelestialBodyHobbies(id, watchUserId);
    }
}
