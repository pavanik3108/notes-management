package com.speer.notes.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.speer.notes.model.Notes;
import com.speer.notes.service.NotesService;

@RestController
public class NotesController {

	@Autowired
	private NotesService notesService;

	@GetMapping("/notes")
	public ResponseEntity<?> getAllNotes(Principal principal) throws Exception {
		try {
			List<Notes> list = notesService.getAllNotes(principal.getName());
			if (list.size() != 0) {
				return ResponseEntity.status(HttpStatus.OK).body(list);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("No notes existed of this user ");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong , please try after sometime");
		}
	}

	@GetMapping("/notes/{id}")
	public ResponseEntity<?> getNotesById(@PathVariable String id) throws Exception {
		try {
			Optional<Notes> notes = notesService.getNotesById(id);
			return notes.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong , please try after sometime");
		}
	}

	@PostMapping("/notes")
	public ResponseEntity<?> createNotes(@RequestBody Notes notes, Principal principal) throws Exception {
		try {
			notes.setCreatedBy(principal.getName());
			Notes createdNotes = notesService.createNotes(notes);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdNotes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong creating notes, please try after sometime");
		}
	}

	@PutMapping("/notes/{id}")
	public ResponseEntity<?> updateNotes(@PathVariable String id, @RequestBody Notes notes) throws Exception {
		try {
			notesService.updateNotes(id, notes);
			return ResponseEntity.status(HttpStatus.OK).body("Given notes updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong while updating notes, please try after sometime");
		}
	}

	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable String id) throws Exception {
		try {
			notesService.deleteNotes(id);
			return ResponseEntity.status(HttpStatus.OK).body("Given Note deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong while deleting notes, please try after sometime");
		}
	}

	@PostMapping("/notes/{id}/share")
	public ResponseEntity<?> shareNotes(@PathVariable String id, @RequestBody Notes notes) throws Exception {
		try {
			Notes sharedNotes = notesService.shareNotes(id, notes);
			return ResponseEntity.status(HttpStatus.OK).body(sharedNotes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong while sharing notes, please try after sometime");
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchNotes(@RequestParam(required = false) String q) throws Exception {
		try {
			List<Notes> notes = notesService.searchNotes(q);
			return ResponseEntity.status(HttpStatus.OK).body(notes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Seems something went wrong while searching  notes, please try after sometime");
		}
	}

}
