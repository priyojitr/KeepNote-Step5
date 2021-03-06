package com.stackroute.keepnote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;

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
public class CategoryServiceImpl implements CategoryService {

	/*
	 * Autowiring should be implemented for the CategoryRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	/*
	 * This method should be used to save a new category.Call the corresponding
	 * method of Respository interface.
	 */
	public Category createCategory(Category category) throws CategoryNotCreatedException {
		Category categoryCreated = null;
		try {
			categoryCreated = this.categoryRepository.insert(category);
			if(null==categoryCreated) {
				throw new CategoryNotCreatedException("category not created exception");	
			}
		} catch (Exception e) {
			throw new CategoryNotCreatedException("category not created exception");
		}
		return categoryCreated;
	}

	/*
	 * This method should be used to delete an existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public boolean deleteCategory(String categoryId) throws CategoryDoesNoteExistsException {
		boolean flag = Boolean.FALSE;
		try {
			this.categoryRepository.deleteById(categoryId);
			flag = Boolean.TRUE;
		} catch (Exception e) {
			throw new CategoryDoesNoteExistsException("category does not exists exception");
		}
		return flag;
	}

	/*
	 * This method should be used to update a existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public Category updateCategory(Category category, String categoryId) {
		this.categoryRepository.save(category);
		return this.categoryRepository.findById(categoryId).get();
	}

	/*
	 * This method should be used to get a category by categoryId.Call the
	 * corresponding method of Respository interface.
	 */
	public Category getCategoryById(String categoryId) throws CategoryNotFoundException {
		Category category = null;
		try {
			Optional<Category> categoryExisting = Optional.ofNullable(this.categoryRepository.findById(categoryId))
					.orElseThrow(() -> new CategoryNotFoundException("category not found exception"));
			if (categoryExisting.isPresent()) {
				category = categoryExisting.get();
			}
		} catch (Exception e) {
			throw new CategoryNotFoundException("category not found exception");
		}
		return category;
	}

	/*
	 * This method should be used to get a category by userId.Call the corresponding
	 * method of Respository interface.
	 */
	public List<Category> getAllCategoryByUserId(String userId) {

		return this.categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
	}

}
