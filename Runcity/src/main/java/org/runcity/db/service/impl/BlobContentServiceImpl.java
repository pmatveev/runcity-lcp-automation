package org.runcity.db.service.impl;

import org.runcity.db.entity.BlobContent;
import org.runcity.db.repository.BlobContentRepository;
import org.runcity.db.service.BlobContentService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class BlobContentServiceImpl implements BlobContentService {
	@Autowired
	private BlobContentRepository blobContentRepository;

	@Override
	public Long handleBlobContent(Long prevId, byte[] blobData) throws DBException {
		if (prevId == null && blobData != null && blobData.length > 0) {
			// have to add
			BlobContent bc = blobContentRepository.save(new BlobContent(null, blobData));
			if (bc == null) {
				throw new DBException("Could not insert blob data");
			}
			return bc.getId();
		} else if (blobData != null) {
			if (blobData.length == 0) {
				// blob deleted
				if (prevId != null) {
					blobContentRepository.delete(prevId);
				}
				return null;
			} else {
				BlobContent bc = blobContentRepository.save(new BlobContent(prevId, blobData));
				if (bc == null) {
					throw new DBException("Could not insert blob data");
				}
				return bc.getId();
			}
		}
		return null;
	}
}
