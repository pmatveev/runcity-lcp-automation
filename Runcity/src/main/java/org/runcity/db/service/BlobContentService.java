package org.runcity.db.service;

import org.runcity.exception.DBException;

public interface BlobContentService {
	public Long handleBlobContent(Long prevId, byte[] blobData) throws DBException;
}
