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
import org.apache.shiro.crypto.hash.Md5Hash;
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
        UsernamePasswordToken token;
        try {
            //核心代码
            token = new UsernamePasswordToken(username, password.toCharArray());
            currentUser.login(token);

        } catch (UnknownAccountException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("用户名不存在");
            return jsonResult;
        } catch (LockedAccountException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("帐户状态异常");
            return jsonResult;
        } catch (AuthenticationException e) {
            jsonResult.setCode(400);
            jsonResult.setMessage("用户名或密码错误");
            return jsonResult;
        }

        if (currentUser.isAuthenticated()) {
            Session session = currentUser.getSession();
            BossUser record = new BossUser();
            //当前principle
            BossRealm.ShiroBossUser shiroBossUser = (BossRealm.ShiroBossUser) currentUser.getPrincipal();
            record.setId(shiroBossUser.getId());
            //更新Boss用户信息
            if (bossUserFacade.updateLoginTime(record)) {
                log.info("更新boss用户登录时间成功");
            } else {
                log.warn("更新boss用户登录时间失败");
            }

            //session中存入当前用户信息
            session.setAttribute(SessionConstant.BOSS_USER, shiroBossUser);
            session.removeAttribute(SessionConstant.APP_USER);

            log.debug("session中的boss用户信息：" + ShiroUtil.getBossUser());

            //session中存入该用户所属的角色所对应的菜单信息
            List<Map<String, Object>> menuList = menuFacade.getAuthroizeMenu(shiroBossUser.getRole());
            session.setAttribute(SessionConstant.ACCESS_MENU, menuList);
            log.debug("session中的菜单信息：" + session.getAttribute(SessionConstant.ACCESS_MENU));

            jsonResult.setData(shiroBossUser);
            jsonResult.setMessage("登录成功");

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

    @RequestMapping(value = "/boss/modify_password", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", notes = "修改密码", response = Response.class)
    public Response modPassword(@ApiParam("用户id") @RequestParam Integer id,
                                @ApiParam("旧密码") @RequestParam String oldPassword,
                                @ApiParam("新密码") @RequestParam String newPassword,
                                @ApiParam("校验的新密码") @RequestParam String validPassword) {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        //1 先检验是否登录
        if (null == session) {
            log.error("修改密码需要先登录！");
            response.setCode(400);
            response.setMessage("请登录");
            return response;
        } else {
            Integer userid = ShiroUtil.getBossUserID();
            //2 再检验传参id是否是当前登录人的id
            if (null != userid && userid.intValue() == id.intValue()) {
                BossRealm.ShiroBossUser bossUser = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
                String oldpwdEncryptDB = bossUser.getPassword();
                String oldPwdParam = new Md5Hash(oldPassword, null, 2).toString();
                //3 检验传参的旧密码是否正确
                if (oldpwdEncryptDB.equals(oldPwdParam)) {
                    //4 检验传参的新密码和二次输入的新密码是否相同
                    if (newPassword.equals(validPassword)) {
                        String newPwdEncrypt = new Md5Hash(newPassword, null, 2).toString();    //新密码加密后
                        //5 修改DB用户信息
                        bossUserFacade.updataBossuserByPwd(id, newPwdEncrypt);
                        //6 修改Session用户信息
                        ShiroUtil.updateBossuserPwd(newPwdEncrypt);

                        response.setData(ShiroUtil.getBossUser());
                        response.setMessage("修改密码成功！");
                    } else {
                        log.error("两次输入的新密码不一致");
                        response.setCode(400);
                        response.setMessage("两次输入的新密码不一致");
                        return response;
                    }

                } else {
                    log.error("输入的旧密码不正确，旧密码：" + oldPassword);
                    response.setCode(400);
                    response.setMessage("输入的旧密码不正确");
                    return response;
                }

            } else {
                log.error("请联系管理员：传参id与当前登录用户id不相等, param[id]=" + id + ", user[id]=" + userid);
                response.setCode(400);
                response.setMessage("请联系管理员：传参id与当前登录用户id不相等");
                response.setData("传参param[id]=" + id + ", 当前登录user[id]=" + userid);
                return response;
            }

        }

        return response;
    }

}
