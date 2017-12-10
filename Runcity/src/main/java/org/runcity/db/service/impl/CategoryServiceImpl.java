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
	public List<Category> selectAll() {
		return categoryRepository.findAll();
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Category addOrUpdate(Category c) throws DBException {
		try {
			if (c.getId() != null) {
				Category prev = selectById(c.getId());
				prev.update(c);
				return categoryRepository.save(prev);
			} else {
				Category n = c.cloneForAdd();
				n = categoryRepository.save(n); 
				n.update(c);
				return categoryRepository.save(n); 
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}
	
	private void delete(Long id) throws DBException {
		try {
			Category c = selectById(id);
			categoryRepository.delete(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id) throws DBException {
		for (Long i : id) {
			delete(i);
		}
	}
}
