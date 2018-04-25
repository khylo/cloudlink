package com.khylo.cloudlink.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString(exclude="id")

@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Term {

    @Id
    public String id;

    private String name;
    private String description;
    private String category;
    private List<String> links;
    private Set<String> labels;
    /*
     *
      @CreatedBy
	  private User user;
	
	  @CreatedDate
	  private DateTime createdDate;
     */

}