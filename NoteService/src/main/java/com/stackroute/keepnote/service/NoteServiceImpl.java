package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final NoteRepository noteRepository;

	public NoteServiceImpl(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		boolean flag = Boolean.TRUE;
		List<Note> notes = new ArrayList<>();
		notes.add(note);
		NoteUser noteUser = new NoteUser();
		noteUser.setUserId(note.getNoteCreatedBy());
		noteUser.setNotes(notes);
		try {
			Optional<?> updNoteUser = Optional.ofNullable(this.noteRepository.insert(noteUser));
			if(!updNoteUser.isPresent()) {
				flag = Boolean.FALSE;
			}
		} catch (Exception e) {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(String userId, int noteId) {
		boolean flag = Boolean.FALSE;
		Optional<NoteUser> noteUser = this.noteRepository.findById(userId);
		if (noteUser.isPresent()) {
			this.noteRepository.delete(noteUser.get());
			flag = Boolean.TRUE;
		}
		return flag;
	}

	/* This method should be used to delete all notes with specific userId. */

	public boolean deleteAllNotes(String userId) {
		boolean flag = Boolean.FALSE;
		try {
			Optional<NoteUser> noteUserOptional = this.noteRepository.findById(userId);
			if (noteUserOptional.isPresent()) {
				NoteUser noteUser = noteUserOptional.get();
				List<Note> filteredNotes = new ArrayList<>();
				noteUser.getNotes().forEach(note -> {
					if (!note.getNoteCreatedBy().equals(userId)) {
						// notes that have different created by user id
						filteredNotes.add(note);
					}
				});
				noteUserOptional.get().setNotes(filteredNotes);
				this.noteRepository.save(noteUserOptional.get());
				flag = Boolean.TRUE;
			}
		} catch (Exception e) {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUserOptional = Optional.ofNullable(this.noteRepository.findById(userId))
					.orElseThrow(() -> (new NoteNotFoundExeption("note not found exception")));
			if (noteUserOptional.isPresent()) {
				List<Note> updateNotes = new ArrayList<>();
				noteUserOptional.get().getNotes().forEach(currNote -> {
					// updating list of notes replacing note with specified id only,
					if (currNote.getNoteId() == id) {
						updateNotes.add(note);
					} else {
						updateNotes.add(currNote);
					}
				});
				noteUserOptional.get().setNotes(updateNotes);
				this.noteRepository.save(noteUserOptional.get());
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption(e.getClass().getName() + ":" + e.getMessage());
		}

		return note;
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		Note note = null;
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId))
					.orElseThrow(() -> new NoteNotFoundExeption("note not found exception -- userid"));
			if (noteUser.isPresent()) {
				note = noteUser.get().getNotes().stream().filter(currNote -> currNote.getNoteId() == noteId).findAny()
						.orElseThrow(() -> new NoteNotFoundExeption("note not found exception -- note id"));
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption(e.getClass().getName() + ":" + e.getMessage());
		}
		return note;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		
		return this.noteRepository.findById(userId).get().getNotes();
	}

}
