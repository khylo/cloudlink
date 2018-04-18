package com.khylo.cloudlink.mongorepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.khylo.cloudlink.model.Term;

@RepositoryRestResource
public interface TermRepo  extends MongoRepository<Term, String> {//MongoRepository<Term, String> {
    public Optional<Term> findByName(@Param("name")String name);
    @RestResource(path="findByCategory", rel="findByCategoryRel")
    public List<Term> findByCategoryOrderByName(@Param("c") String category);
    @RestResource(path="findByLabel", rel="findByLabel")
    public List<Term> findByLabelsOrderByName(@Param("label") String label);
    
    
    /*
     *  Turn off deletes TODO confirm if this is still needed in later versions(non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#deleteById(java.lang.Object)
     */
    
    @Override
    @RestResource(exported = false)
    void deleteById(String id);

    @Override
    @RestResource(exported = false)
    void delete(Term entity);
    
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Term> entities);

    @Override
    @RestResource(exported = false)
	void deleteAll();

}
