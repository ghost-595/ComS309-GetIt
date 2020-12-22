package com.iastate.edu.coms309.sb4.Getit.Server.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iastate.edu.coms309.sb4.Getit.Server.RequestStatus;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.Note;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.NoteRepository;

/**
 * 
 * @author nathan
 *
 */
@Controller
@RequestMapping(path = "/notes")
public class NotesController {

	@Autowired
	private NoteRepository repository;
	
	@PostMapping(path = "/find")
	public @ResponseBody RequestStatus getNote (@RequestBody Map<String, Object> payload) {
		//Get the note ID from the incoming request
		int noteId = (int)payload.get ("id");
		
		//Retrieve the note data from the repository
		Note associatedNote = repository.findNoteById (new Integer (noteId));
		System.out.println ("NOTE REQUEST RECIEVED");
		System.out.println (associatedNote);
		System.out.println (noteId);
		
		//Send an appropriate response
		if (associatedNote == null) {
			return new RequestStatus (false, "Note does not exist");
		} else {
			return new RequestStatus (true, associatedNote);
		}
	}
	
	@PostMapping(path = "/save")
	public @ResponseBody RequestStatus saveNote (@RequestBody Map<String, Object> payload) {
		//Get the note ID
		int noteId = (Integer)payload.get ("id");
		Map note = (Map)payload.get ("note");
		Note toSave;
		
		//Save the note to the repository
		try {
			String course = (String)note.get ("course");
			String date = (String)note.get("date");
			String text = (String)note.get ("text");
			int pages = (Integer)note.get ("pages");
			String data = (String)note.get ("data");
			toSave = new Note (noteId, course, date, text, pages, data);
		} catch (Exception e) {
			System.out.println (e);
			e.printStackTrace ();
			return new RequestStatus (false, "Malformed request");
		}
		repository.save (toSave);
		return new RequestStatus (true, toSave);
	}
	
}
