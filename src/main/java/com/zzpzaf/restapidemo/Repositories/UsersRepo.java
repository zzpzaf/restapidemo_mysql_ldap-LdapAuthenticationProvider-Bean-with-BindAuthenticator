package com.zzpzaf.restapidemo.Repositories;

import java.util.List;

import com.zzpzaf.restapidemo.dataObjects.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${USERS_TABLE}")
    private String USERS_TABLE; // = "USERS";

    @Value("${ROLES_TABLE}")
    private String ROLES_TABLE; // = "AUTHORITIES";

    public User findById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM " + USERS_TABLE + " WHERE ID=?",
                       BeanPropertyRowMapper.newInstance(User.class), id);
            return user;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public User findByName(String username) {

        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM " + USERS_TABLE + " WHERE USERNAME=?",
                       BeanPropertyRowMapper.newInstance(User.class), username);
            user.grantAuthorities(this.getUserRoles(user.getUSERNAME()));
            return user;

        } catch (IncorrectResultSizeDataAccessException e) {

            return null;
        }
    }

    private List<String> getUserRoles(String userName) {

        String querySQL = "SELECT ROLE FROM " + ROLES_TABLE + " WHERE USERNAME = '" + userName + "'";
        //dataSource.
        List<String> userRoles = jdbcTemplate.queryForList(querySQL, String.class);
        return userRoles;
    }

}
