package cn.zym.form_login.jpa.entity;

import javax.persistence.*;

/**
 * @ClassName Role
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/6 15:55
 * @Version 1.0
 */
//@Entity注解项目启动时会根据属性名称创建相应表，表名为t_role
@Entity
@Table(name = "t_role")
public class Role {

    @Id
    //GenerationType.IDENTITY主键自增长
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nameZh")
    private String nameZh;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
}
