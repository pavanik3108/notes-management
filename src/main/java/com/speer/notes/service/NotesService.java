package com.speer.notes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.speer.notes.entity.User;
import com.speer.notes.model.Notes;
import com.speer.notes.repository.NotesRepository;
import com.speer.notes.repository.UserRepository;

@Service
public class NotesService {

	@Autowired
	NotesRepository notesRepository;

	@Autowired
	UserRepository userRepository;

	public List<Notes> getAllNotes(String username) throws Exception {
		try {
			return notesRepository.findAll().stream().filter(note -> note.getCreatedBy().equals(username))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw e;
		}
	}

	public Optional<Notes> getNotesById(String id) throws Exception {
		try {
			return notesRepository.findById(id);
		} catch (Exception e) {
			throw e;
		}
	}

	public Notes createNotes(Notes notes) throws Exception {
		return notesRepository.save(notes);
	}

	public void updateNotes(String id, Notes notes) throws Exception {

		if (notesRepository.existsById(id)) {
			notes.setId(id); // Ensure the ID is set for update
			notesRepository.save(notes);
		}
	}

	public void deleteNotes(String id) {
		notesRepository.deleteById(id);
	}

	public List<Notes> searchNotes(String keys) throws Exception {
		return notesRepository.findByTitleOrContentRegexIgnoreCase(".*" + keys + ".*");

	}

	public Notes shareNotes(String id, Notes notes) throws Exception {
		Optional<User> user = userRepository.findById(id);
		String userName = user.get().getUsername();
		notes.setCreatedBy(userName);
		return createNotes(notes);
	}

}
