package com.stackroute.keepnote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.repository.ReminderRepository;

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
public class ReminderServiceImpl implements ReminderService {

	/*
	 * Autowiring should be implemented for the ReminderRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final ReminderRepository reminderRepository;

	@Autowired
	public ReminderServiceImpl(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	/*
	 * This method should be used to save a new reminder.Call the corresponding
	 * method of Respository interface.
	 */
	public Reminder createReminder(Reminder reminder) throws ReminderNotCreatedException {
		try {
			Reminder reminderCreated = Optional.ofNullable(this.reminderRepository.insert(reminder))
					.orElseThrow(() -> new ReminderNotCreatedException("reminder not created exception -- optional"));
			if (null != reminderCreated) {
				return reminderCreated;
			} else {
				throw new ReminderNotCreatedException("reminder not created exception");
			}
		} catch (Exception e) {
			throw new ReminderNotCreatedException("reminder not created exception -- create");
		}
	}

	/*
	 * This method should be used to delete an existing reminder.Call the
	 * corresponding method of Respository interface.
	 */
	public boolean deleteReminder(String reminderId) throws ReminderNotFoundException {
		boolean flag = Boolean.FALSE;
		try {
			Optional<?> reminder = Optional.ofNullable(this.reminderRepository.findById(reminderId))
					.orElseThrow(() -> new ReminderNotFoundException("reminder not found exception -- optional"));
			if (reminder.isPresent()) {
				this.reminderRepository.deleteById(reminderId);
				flag = Boolean.TRUE;
			}
		} catch (Exception e) {
			throw new ReminderNotFoundException("reminder not found exception -- delete");
		}
		return flag;
	}

	/*
	 * This method should be used to update a existing reminder.Call the
	 * corresponding method of Respository interface.
	 */
	public Reminder updateReminder(Reminder reminder, String reminderId) throws ReminderNotFoundException {
		Optional<Reminder> reminderOptional = Optional.ofNullable(this.reminderRepository.findById(reminder.getReminderId()))
				.orElseThrow(() -> new ReminderNotFoundException("reminder not found exception -- optional"));
		if (reminderOptional.isPresent()) {
			this.reminderRepository.save(reminder);
			return reminderOptional.get();
		} else {
			throw new ReminderNotFoundException("reminder not found exception -- update");
		}
	}

	/*
	 * This method should be used to get a reminder by reminderId.Call the
	 * corresponding method of Respository interface.
	 */
	public Reminder getReminderById(String reminderId) throws ReminderNotFoundException {
		Reminder reminder = null;
		try {
			Optional<Reminder> reminderOptional = Optional.ofNullable(this.reminderRepository.findById(reminderId))
					.orElseThrow(() -> new ReminderNotFoundException("reminder not found exception -- reminder id"));
			if (reminderOptional.isPresent()) {
				reminder = reminderOptional.get();
			} else {
				throw new ReminderNotFoundException("reminder not found exception");
			}
		} catch (Exception e) {
			throw new ReminderNotFoundException("reminder not found exception");
		}
		return reminder;
	}

	/*
	 * This method should be used to get all reminders. Call the corresponding
	 * method of Respository interface.
	 */

	public List<Reminder> getAllReminders() {

		return this.reminderRepository.findAll();
	}

}
