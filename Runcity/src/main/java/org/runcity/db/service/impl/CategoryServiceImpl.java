package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.repository.CategoryRepository;
import org.runcity.db.service.CategoryService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	@Secured("ROLE_ADMIN")
	public Category selectById(Long id) {
		return categoryRepository.findOne(id);
	}
	
	@Override
	@Secured("ROLE_ADMIN")
	public List<Category> selectAll() {
		return categoryRepository.findAll();
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Category addOrUpdate(Category c) throws DBException {
		try {
			return categoryRepository.save(c); // TODO test
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void delete(Category c) throws DBException {
		try {
			categoryRepository.delete(c); // TODO test
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}
}
