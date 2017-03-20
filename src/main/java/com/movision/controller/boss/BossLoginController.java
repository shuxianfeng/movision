package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.common.constant.SessionConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.user.BossUserFacade;
import com.movision.facade.user.MenuFacade;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.shiro.realm.BossRealm;
import com.movision.utils.JsonUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 20:52
 */
@RestController
public class BossLoginController {

    private static Logger log = LoggerFactory.getLogger(BossLoginController.class);

    @Autowired
    private BossUserFacade bossUserFacade;

    @Autowired
    private MenuFacade menuFacade;

    @RequestMapping(value = "/boss/login", method = RequestMethod.POST)
    @ApiOperation(value = "运营登录", notes = "运营登录", response = Response.class)
    public Response login(@ApiParam(value = "用户名") @RequestParam String username,
                          @ApiParam(value = "密码") @RequestParam String password) throws IOException {
        log.info("boss login post 登录校验");
        Response jsonResult = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = null;
        try {
            //核心代码
            token = new UsernamePasswordToken(username, password.toCharArray());
            currentUser.login(token);
            jsonResult.setData(username);
            jsonResult.setMessage("登录成功");

        } catch (UnknownAccountException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("用户名不存在");
        } catch (LockedAccountException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("帐户状态异常");
        } catch (AuthenticationException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("用户名或密码错误");
        }

        if (currentUser.isAuthenticated()) {
            Session session = currentUser.getSession();
            BossUser record = new BossUser();
            //当前principle
            BossRealm.ShiroBossUser shiroBossUser = (BossRealm.ShiroBossUser) currentUser.getPrincipal();
            record.setId(shiroBossUser.getId());
            //更新Boss用户信息
            if (bossUserFacade.updateLoginInfo(record)) {
                log.info("更新Boss用户登录信息成功");
            } else {
                log.warn("更新Boss用户登录信息失败");
            }

            //session中存入当前用户信息
            session.setAttribute(SessionConstant.BOSS_USER, currentUser.getPrincipal());
            session.removeAttribute(SessionConstant.APP_USER);

            //session中存入该用户所属的角色所对应的菜单信息
            List<Map<String, Object>> menuList = menuFacade.getAuthroizeMenu(shiroBossUser.getRole());
            session.setAttribute(SessionConstant.ACCESS_MENU, menuList);

            System.out.println("boss 用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        } else {
            token.clear();
        }

        return jsonResult;
    }

    @RequestMapping(value = "/boss/logout", method = RequestMethod.GET)
    @ApiOperation(value = "登出", notes = "登出", response = Response.class)
    public void logout(HttpServletResponse response) throws IOException {
        SecurityUtils.getSubject().logout();
        Response jsonResult = new Response();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

}
