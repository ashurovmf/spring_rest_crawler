package com.gft.backend.dao;


import com.gft.backend.entities.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by miav on 2016-10-06.
 */
@Repository
public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    private static final String GET_USERS_SQL = "SELECT u.username as name, u.password as pass,"+
            " u.email as email, u.provider as provider FROM " +
            "app_user u WHERE u.enabled =1 and u.username = ?";
    private static final String GET_USERS_BY_ID_SQL = "SELECT u.username as name, "+
            " u.email as email FROM " +
            "app_user u WHERE u.enabled =1 and u.id = ?";
    private static final String INSERT_USER_SQL = "insert into app_user (username, password,"+
            " email, provider, enabled) values (?, ?, ?, ?, ?)";
    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UserInfo getUserInfo(String username){
        try {
            UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(GET_USERS_SQL, new Object[]{username},
                    new RowMapper<UserInfo>() {
                        public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                            UserInfo user = new UserInfo();
                            user.setUsername(rs.getString("name"));
                            user.setPassword(rs.getString("pass"));
                            user.setEmail(rs.getString("email"));
                            user.setProvider(rs.getString("provider"));
                            //user.setRole(rs.getString("role"));
                            return user;
                        }
                    });
            return userInfo;
        } catch (Exception ex){
            logger.info("User find is fail",ex);
            return null;
        }
    }

    public UserInfo getUserInfoById(int id){
        try {
            UserInfo userInfo = (UserInfo)jdbcTemplate.queryForObject(GET_USERS_BY_ID_SQL, new Object[]{id},
                    new RowMapper<UserInfo>() {
                        public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                            UserInfo user = new UserInfo();
                            user.setUsername(rs.getString("name"));
                            user.setEmail(rs.getString("email"));
                            return user;
                        }
                    });
            return userInfo;
        } catch (Exception ex){
            logger.info("User find is fail",ex);
            return null;
        }
    }

    public boolean setUserInfo(String username, String password, String email, String provider){
        int row = jdbcTemplate.update(INSERT_USER_SQL, new Object[]{username, password, email, provider, 1});
        if(row > 0){
            return true;
        }
        return false;
    }
}
