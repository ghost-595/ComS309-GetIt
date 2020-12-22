package com.iastate.edu.coms309.sb4.Getit.Server.repository;

import org.springframework.data.repository.CrudRepository;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.Note;

public interface NoteRepository extends CrudRepository<Note, Long> {

	Note findNoteById (int id);
	
}
