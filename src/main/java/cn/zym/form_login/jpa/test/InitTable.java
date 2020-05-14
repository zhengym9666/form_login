package cn.zym.form_login.jpa.test;

import cn.zym.form_login.jpa.dao.UserDao;
import cn.zym.form_login.jpa.entity.Role;
import cn.zym.form_login.jpa.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName InitTable
 * @Description TODO    JPA调用dao保存数据，若没有表时，会根据实体类自动创建表，并保存数据
 * @Author zhengym
 * @Date 2020/5/6 17:15
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitTable {

    @Autowired
    UserDao userDao;

    @Test
    public void createTable() {
        User u1 = new User();
        u1.setUsername("jpa1");
        u1.setPassword("jpa1");
        u1.setAccountNonExpired(true);
        u1.setEnabled(true);
        u1.setCredentialsNonExpired(true);
        u1.setAccountNonLocked(true);
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_admin");
        role.setNameZh("管理员角色");
        roles.add(role);
        u1.setRoles(roles);

        userDao.save(u1);

        User u2 = new User();
        u2.setUsername("jpa2");
        u2.setPassword("jpa2");
        u2.setAccountNonExpired(true);
        u2.setEnabled(true);
        u2.setCredentialsNonExpired(true);
        u2.setAccountNonLocked(true);
        List<Role> roles2 = new ArrayList<>();
        Role role2 = new Role();
        role2.setName("ROLE_user");
        role2.setNameZh("用户角色");
        roles2.add(role2);
        u2.setRoles(roles2);

        userDao.save(u2);
    }

}


