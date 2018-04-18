package com.khylo.cloudlink.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khylo.cloudlink.model.Term;
import com.khylo.cloudlink.mongorepo.TermRepo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
//@WebMvcTest(HelloController.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CloudLinkRepoIntegrationTests {
    @Autowired private WebApplicationContext wac;

    @Autowired private ObjectMapper mapper;
    
    @Autowired private TermRepo termRepo;

    private MockMvc mvc;
    private static String newObjUrl;
    


    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

		termRepo.deleteAll();
		Term li = Term.builder().name("S3").description("Simple Storage Store").category("AWS").labels(Set.of("storage")).build();
		termRepo.save(li);
		termRepo.save(Term.builder().name("EBS").description("Elastic Block Storage").category("AWS").labels(Set.of("storage")).build());
		termRepo.save(Term.builder().name("EFS").description("Elastic File Storage").category("AWS").labels(Set.of("storage")).build());
		termRepo.save(Term.builder().name("Glacier").description("Archived storage. Pay per retrieval").category("AWS").labels(Set.of("storage")).build());
		termRepo.save(Term.builder().name("Citus").description("Horizontal scaling of PostgresSql").category("3pty").labels(Set.of("database")).build());
		termRepo.save(Term.builder().name("CosmosDB").description("General cloud DB with relational noSql column based options").category("Azure").labels(Set.of("database")).build());
    }
    
    @Test
    public void testClean() throws Exception {
    	MvcResult result = mvc.perform(get("/terms").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andReturn();
    	Map<String, Object> newObj = mapper.readValue(result.getResponse().getContentAsString(), Map.class);
    	List<Map> terms = ((List<Map>)((Map)newObj.get("_embedded")).get("terms"));
    	for(Map term: terms) {
    		if(term.get("name").equals("TestTerm")) {
    			doDelete(((Map)((Map)term.get("_links")).get("self")).get("href").toString());
    		}
    	}
    }
    
    private void doDelete(String url) throws Exception {
    	mvc.perform(delete(url).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(204));
    }


    @Test
    public void testCreate() throws Exception {
    	// confirm size before
    	ResultActions res = mvc.perform(get("/terms").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.terms", hasSize(6)));
        //create
    	Term newTerm = Term.builder().name("TestTerm").category("TestCat").build();
    	MvcResult result = mvc.perform(post("/terms").content(mapper.writeValueAsString(newTerm)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();
    	//Confirm size after
    	res = mvc.perform(get("/terms").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.terms", hasSize(7)));
    	//Parse response
    	Map<String, Object> newObj = mapper.readValue(result.getResponse().getContentAsString(), Map.class);
    	newObjUrl = ((Map)((Map)newObj.get("_links")).get("self")).get("href").toString();
    }
    
    @Test
    public void testDelete() throws Exception {
    	//Delete blocked
        mvc.perform(delete(newObjUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(405));
    }
    

    @Test
    public void testGetAll() throws Exception {
    	ResultActions res = mvc.perform(get("/terms").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.terms", hasSize(6)))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItem("EBS")))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItem("EFS")))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItem("Glacier")))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItem("S3")))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItem("CosmosDB")))
           .andExpect(jsonPath("$._embedded.terms[*].name", hasItems("EBS", "EFS", "S3", "Glacier", "Citus", "CosmosDB")));
        
        // Sorted Order by param?
        res = mvc.perform(get("/terms").param("sort","name,asc").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
        	.andExpect(jsonPath("$._embedded.terms[0].name", is("Citus")))
           .andExpect(jsonPath("$._embedded.terms[1].name", is("CosmosDB")))
           .andExpect(jsonPath("$._embedded.terms[2].name", is("EBS")))
           .andExpect(jsonPath("$._embedded.terms[3].name", is("EFS")))
           .andExpect(jsonPath("$._embedded.terms[4].name", is("Glacier")))
           .andExpect(jsonPath("$._embedded.terms[5].name", is("S3")));

    }
    
    @Test
    public void testSearchByLabels() throws Exception {
    	List<Map<String, String>> testDataTable = List.of(Map.of("label", "storage", "ans","4", "vals", "EBS,EFS,S3,Glacier"),
    														Map.of("label", "database", "ans","2", "vals", "Citus,CosmosDB"));
    	for(Map<String, String> testData: testDataTable) {
    		String label = testData.get("label");
    		Integer ans = Integer.parseInt(testData.get("ans"));
    		String[] vals = testData.get("vals").split(",");
        	ResultActions res = mvc.perform(get("/terms/search/findByLabel").param("label", label).accept(MediaType.APPLICATION_JSON));
        	res.andExpect(status().isOk())
        	.andExpect(jsonPath("$._embedded.terms", hasSize(ans)))
        	.andExpect(jsonPath("$._embedded.terms[*].name", hasItems(vals)));
    	}
        

    }
    
    @Test
    public void testSearchByCategory() throws Exception {
    	List<Map<String, String>> testDataTable = List.of(Map.of("cat", "AWS", "ans","4", "vals", "EBS,EFS,S3,Glacier"),
    														Map.of("cat", "3pty", "ans","1", "vals", "Citus"));
    	for(Map<String, String> testData: testDataTable) {
    		String cat = testData.get("cat");
    		Integer ans = Integer.parseInt(testData.get("ans"));
    		String[] vals = testData.get("vals").split(",");
        	ResultActions res = mvc.perform(get("/terms/search/findByCategory").param("c", cat).accept(MediaType.APPLICATION_JSON));
        	res.andExpect(status().isOk())
        	.andExpect(jsonPath("$._embedded.terms", hasSize(ans)))
        	.andExpect(jsonPath("$._embedded.terms[*].name", hasItems(vals)));
    	}
        

    }
}
