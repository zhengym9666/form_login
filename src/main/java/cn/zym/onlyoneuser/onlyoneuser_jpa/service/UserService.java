package cn.zym.onlyoneuser.onlyoneuser_jpa.service;

import cn.zym.onlyoneuser.onlyoneuser_jpa.dao.UserDao;
import cn.zym.onlyoneuser.onlyoneuser_jpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/6 16:47
 * @Version 1.0
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(username);
        if (user==null) {
            throw  new RuntimeException("用户不存在");
        }
        return user;
    }
}
