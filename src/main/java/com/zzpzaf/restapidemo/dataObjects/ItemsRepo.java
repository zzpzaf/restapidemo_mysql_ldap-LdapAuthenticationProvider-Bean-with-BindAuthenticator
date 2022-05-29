package com.zzpzaf.restapidemo.dataObjects;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ItemsRepo {
    
    //private Item item = new Item();
   
    @Autowired
    private JdbcTemplate jdbcTemplate; 

    
    public List<Item> getItems() {
        //List<Item> itemsList =  new ArrayList<>();

        return jdbcTemplate.query("SELECT * FROM items", BeanPropertyRowMapper.newInstance(Item.class));

    }  
}
