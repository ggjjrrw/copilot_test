package com.example.myhome.dao;

import com.example.myhome.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 演示MySQL数据库操作
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据状态查找用户列表
     *
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * 根据用户名或邮箱查找用户
     *
     * @param username 用户名
     * @param email    邮箱
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    List<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    /**
     * 统计激活状态的用户数量
     *
     * @return 激活用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    Long countActiveUsers();

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
}