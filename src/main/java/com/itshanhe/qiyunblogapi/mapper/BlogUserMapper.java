package com.itshanhe.qiyunblogapi.mapper;


import com.itshanhe.qiyunblogapi.entity.BlogUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BlogUserMapper {
    /**
     * 用户注册
     * @param username 账号
     * @param password 密码
     * @param nickname 昵称
     * @param email 邮件
     * @return
     */
    @Insert("INSERT INTO qiyun_blog_user(user_username, user_password, user_nick_name,user_email)VALUES(#{username},#{password},#{nickname},#{email}) ")
    int registerUser(String username, String password, String nickname,String email);
    
    /**
     * 查询用户姓名
     * @param username 用户账号
     * @return
     */
    @Select("SELECT user_username, user_password FROM qiyun_blog_user WHERE user_username = #{username}")
    BlogUser selectUserDataName(String username);
    
    /**
     * 查询用户id
     * @param userUsername 用户账号
     * @return
     */
    @Select("SELECT user_id FROM qiyun_blog_user WHERE user_username = #{userUsername}")
    String userGetId(String userUsername);
    
    /**
     * 删除用户
     * @param userUsername 用户账号
     * @return
     */
    @Delete("DELETE FROM qiyun_blog_user WHERE user_username = #{userUsername}")
    int userDeleteName(String userUsername);
    
    /**
     * 查看用户是否锁定
     * @param id
     * @return
     */
    @Select("SELECT user_locked FROM qiyun_blog_user WHERE user_id = #{id}")
    int userGetLocked(String id);
    
    /**
     * 更改用户锁定状态
     * @param id 用户id
     * @param setLock 锁定状态
     * @return
     */
    @Update("UPDATE qiyun_blog_user SET user_update_date =DEFAULT,user_locked = #{setLock} WHERE user_id = #{id}")
    int setLocked(String id, int setLock);
}
