package com.khylo.cloudlink.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.khylo.cloudlink.model.Term;
import com.khylo.cloudlink.mongorepo.TermRepo;
import com.sun.tools.javac.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(HelloController.class)
@SpringBootTest
public class ClouLinkIntTests {

    private MockMvc mvc;

    
    	

    @Test
    public void testCreate() throws Exception {
		
		ResultActions res = mvc.perform(get("/hello").accept(MediaType.TEXT_PLAIN));
        res.andExpect(status().isOk())
           .andExpect(content().string("Hello World"));
	}
}
