package cn.zym.customize2.details;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/*
 *登录用户信息存放源码解读：
 * 登录后进入的拦截器UsernamePasswordAuthenticationFilter，构造UsernamePasswordAuthenticationToken对象后，调用setDetails方法存放用户的额外信息，改方法中是通过AuthenticationDetailsSource
 * 的buildDetails方法创建WebAuthenticationDetails对象，该对象封装的是ip和session信息，AuthenticationDetailsSource是一个接口，默认是使用WebAuthenticationDetailsSource实现类。
 *
 * 定制化Authentication的details可存放其他自定义数据的步骤：
 * 自定义一个detail类，继承WebAuthenticationDetails，那么自定义的detail既可存放自定义数据，又可存放原来默认的ip和session；
 * 自定义一个AuthenticationDetailsSource的实现类，实现的buildDetails方法中返回自定义的detail类
 *
 * 如何将自定义的AuthenticationDetailsSource放到登录流程中？
 * 在SecurityConfig配置文件中的configure(http)方法中加入配置 .authenticationDetailsSource(myAuthenticationDetailsSource)，其中myAuthenticationDetailsSource必须加入注解@Component注入实例
 **/

/**
 * @ClassName MyAuthenticationDetails
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/20 14:26
 * @Version 1.0
 */
public class MyAuthenticationDetails extends WebAuthenticationDetails {

    private boolean isPassed;//自定义用户额外信息，验证码验证通过标识

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyAuthenticationDetails(HttpServletRequest request) {
        super(request);//保持WebAuthenticationDetails原来的方法，即将ip和session保存
        String code = request.getParameter("code");
        String verify_code = (String) request.getSession().getAttribute("verify_code");
        if (code==null || verify_code==null || !code.equals(verify_code)){
            isPassed = false;
        } else {
            isPassed = true;
        }
    }

    public Boolean isPassed() {
        return isPassed;
    }


}
