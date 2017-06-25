package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    int checkEmail(String eMail);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestion(String username);

    int checkAnswer(@Param("username")String username, @Param("question")String question,@Param("answer")String answer);

    int updatePasswordByUserName(@Param("username") String username, @Param("passWordNew") String passWordNew);

    int checkPassword(@Param(value = "password")String password, @Param(value = "id")Integer id);

    int checkEmailByUserId(@Param(value = "email") String email, @Param(value = "id") Integer id);
}