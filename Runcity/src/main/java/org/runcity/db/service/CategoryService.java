package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface CategoryService {
	@Secured("ROLE_ADMIN")
	public Category selectById(Long id, boolean games);
	
	@Secured("ROLE_ADMIN")
	public Iterable<Category> selectById(Iterable<Long> id, boolean games);
	
	public List<Category> selectAll(boolean games);
	
	@Secured("ROLE_ADMIN")
	public Category addOrUpdate(Category c) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);
}
