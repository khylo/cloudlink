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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(HelloController.class)
@SpringBootTest
public class ClouLinkIntTests {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private TermRepo termRepo;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

		termRepo.deleteAll();
		Term li = new Term("S3", "Simple Storage Store", "AWS");
		termRepo.save(li);
		termRepo.save(new Term("EBS", "Elastic Block Storage", "AWS"));
		termRepo.save(new Term("EFS", "Elastic File Storage", "AWS"));
		termRepo.save(new Term("Glacier", "Archived storage. Pay per retrieval", "AWS"));
    }

    
    	

    @Test
    public void testCreate() throws Exception {
		
		ResultActions res = mvc.perform(get("/termRepo").accept(MediaType.TEXT_PLAIN));
        res.andExpect(status().isOk())
           .andExpect(content().string("Hello World"));
		System.out.println("ListItems found with findAll():");
		System.out.println("-------------------------------");
		for(Term t:termRepo.findAll()) {
			System.out.println(t);
		}		
		System.out.println();
	}
}
