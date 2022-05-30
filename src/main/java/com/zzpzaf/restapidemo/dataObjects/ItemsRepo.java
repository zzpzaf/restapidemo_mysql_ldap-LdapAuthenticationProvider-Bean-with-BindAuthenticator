package com.zzpzaf.restapidemo.dataObjects;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ItemsRepo {
    
    @Autowired
    private JdbcTemplate jdbcTemplate; 

    
    public List<Item> getItems() {

        return jdbcTemplate.query("SELECT * FROM items", BeanPropertyRowMapper.newInstance(Item.class));

    }  
}
