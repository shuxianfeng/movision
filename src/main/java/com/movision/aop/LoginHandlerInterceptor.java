//package com.movision.aop;
//
//import com.movision.common.constant.SessionConstant;
//import com.movision.mybatis.bossMenu.entity.AuthMenu;
//import com.movision.shiro.realm.BossRealm;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.Subject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.Map;
//
///**
// * 登录过滤，权限验证
// * @Author zhuangyuhao
// * @Date 2017/2/20 10:09
// */
//public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {
//
//    private static Logger log = LoggerFactory.getLogger(LoginHandlerInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        //获取请求的url
//        String path = request.getServletPath();
//        log.info("path="+path);
//        //todo 这一步是什么意思？??配置不需要拦截的菜单的正则表达式
//        if (path.contains("swagger-ui") || path.contains("rest/exception")) {
//            return true;
//        } else {
//            // shiro管理的session
//            Subject currentUser = SecurityUtils.getSubject();
//            Session session = currentUser.getSession();
//            BossRealm.ShiroBossUser user = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
//            if (user != null) {
//                // 判断是否拥有当前点击菜单的权限（内部过滤,防止通过url进入跳过菜单权限）
//                /**
//                 * 根据点击的菜单中的URL去匹配，当匹配到了此菜单，判断是否有此菜单的权限，没有的话跳转到404页面
//                 * 根据按钮权限，授权按钮(当前点的菜单和角色中各按钮的权限匹对)
//                 */
//                //获取授权的菜单列表
//                List<Map<String,Object>> menuList = (List) session.getAttribute(SessionConstant.ACCESS_MENU);
//                log.info("获取授权的菜单列表："+menuList.toString());
//                path = path.substring(1, path.length());
//                //遍历父菜单
//                for (int i = 0; i < menuList.size(); i++) {
////                    AuthMenu pmenu = (AuthMenu) menuList.get(i).get("parent_menu");
//                    List<AuthMenu> childrenList =(List<AuthMenu>) menuList.get(i).get("child_menu");
//                    //遍历子菜单
//                    for (int j = 0; j < childrenList.size(); j++) {
//                        //若子菜单的url等于请求的url，并且 该菜单属于授权菜单，则通过拦截器
//                        if (childrenList.get(j).getUrl().equals(path) && childrenList.get(j).getAuthroize()) {
//
//                            return true;
//                            /*//todo isHasMenu() ???
//                            if (!childrenList.get(j).isHasMenu()) {
//                                //todo 判断有无此菜单权限，若没有，则重定向到？=登录界面
//                                response.sendRedirect(request.getContextPath() + SmartConfig.getString("smart.login"));
//                                return false;
//                            } else {
//                                //todo 获取session中的权限,在什么地方去存储？
//                                Map<String, String> map = (Map<String, String>) session
//                                        .getAttribute(Constants.USER_SESSION_RANK);// session中存储的按钮权限
//                                //先清除旧的权限记录
//
//                                //获取当前菜单id
//                                Integer MENU_ID =childrenList.get(j).getId();
//                                //todo 获取当前登录者loginname
//                                String USERNAME = user.getUsername();
//                                //判断是否是管理员
//                                Boolean isAdmin = "admin".equals(USERNAME);
//                                //如果是管理员，或者具有指定编码的权限，则保存相应的菜单权限
//                                map.put("add",
//                                        (RightsHelper.testRights(map.get("adds"), MENU_ID)) || isAdmin ? "1" : "0");
//                                map.put("del",
//                                        RightsHelper.testRights(map.get("dels"), MENU_ID) || isAdmin ? "1" : "0");
//                                map.put("edit",
//                                        RightsHelper.testRights(map.get("edits"), MENU_ID) || isAdmin ? "1" : "0");
//                                map.put("cha",
//                                        RightsHelper.testRights(map.get("chas"), MENU_ID) || isAdmin ? "1" : "0");
//                                //清除session中旧的权限记录
//                                session.removeAttribute(SessionConstant.BOSS_USER_RANK);
//                                // 重新分配按钮权限
//                                session.setAttribute(SessionConstant.BOSS_USER_RANK, map);
//                            }*/
//                        }
//                    }
//                }
//                return  false;
//            } else {
//                //跳转到登录界面
////                String str=SmartConfig.getString("smart.login");
//                // 登陆过滤
//                response.sendRedirect(request.getContextPath() +  "app/login");
//                return false;
//                // return true;
//            }
//        }
//    }
//}
