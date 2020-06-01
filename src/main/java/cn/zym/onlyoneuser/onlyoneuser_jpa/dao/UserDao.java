package cn.zym.onlyoneuser.onlyoneuser_jpa.dao;

import cn.zym.onlyoneuser.onlyoneuser_jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserDao
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/6 16:46
 * @Version 1.0
 */
@Repository
public interface UserDao extends JpaRepository<User,Long> {

    //自定义接口方法
    //方法名称必须要遵循驼峰式命名规则，findBy（关键字）+属性名称（首字母大写）+查询条件（首字母大写）
    User findUserByUsername(String username);

}
