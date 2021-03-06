package cn.niudehua.community.intercepter;

import cn.niudehua.community.mapper.UserMapper;
import cn.niudehua.community.model.User;
import cn.niudehua.community.model.UserExample;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: deng
 * @datetime: 2020/4/30 4:46 下午
 * @desc: 登录会话拦截器
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionInterceptor implements HandlerInterceptor {
    private final UserMapper userMapper;

    /**
     * 会话拦截器-处理登录问题
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  handler
     * @return 返回true -> 进入下一个拦截器或controller 返回false->中断客户端请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Cookie[] cookies = request.getCookies();
        if (!ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                // 通过cookie获取用户信息并将用户信息保存到session
                if ("token".equals(cookie.getName())) {
                    UserExample example = new UserExample();
                    example.createCriteria().andTokenEqualTo(cookie.getValue());
                    List<User> users = userMapper.selectByExample(example);
                    if (!CollectionUtils.isEmpty(users)) {
                        request.getSession().setAttribute("gitHubUser", users.get(0));
                    }
                    break;
                }
            }
        }
        return true;
    }
}
