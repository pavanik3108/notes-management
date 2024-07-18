package com.speer.notes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.speer.notes.model.Notes;

public interface NotesRepository extends MongoRepository<Notes, String>{
	
	 @Query("{ '$or': [ { 'title' : { '$regex' : ?0, '$options' : 'i' } }, { 'content' : { '$regex' : ?0, '$options' : 'i' } } ] }")
	 List<Notes> findByTitleOrContentRegexIgnoreCase(String keyword);
	 
}
