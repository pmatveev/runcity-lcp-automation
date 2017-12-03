package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface CategoryService {
	@Secured("ROLE_ADMIN")
	public Category selectById(Long id);
	
	@Secured("ROLE_ADMIN")
	public List<Category> selectAll();
	
	@Secured("ROLE_ADMIN")
	public Category addOrUpdate(Category c) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void delete(Category c) throws DBException;
}
