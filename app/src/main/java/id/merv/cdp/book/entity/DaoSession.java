package id.merv.cdp.book.entity;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import id.merv.cdp.book.entity.Media;
import id.merv.cdp.book.entity.Document;

import id.merv.cdp.book.entity.MediaDao;
import id.merv.cdp.book.entity.DocumentDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig mediaDaoConfig;
    private final DaoConfig documentDaoConfig;

    private final MediaDao mediaDao;
    private final DocumentDao documentDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        mediaDaoConfig = daoConfigMap.get(MediaDao.class).clone();
        mediaDaoConfig.initIdentityScope(type);

        documentDaoConfig = daoConfigMap.get(DocumentDao.class).clone();
        documentDaoConfig.initIdentityScope(type);

        mediaDao = new MediaDao(mediaDaoConfig, this);
        documentDao = new DocumentDao(documentDaoConfig, this);

        registerDao(Media.class, mediaDao);
        registerDao(Document.class, documentDao);
    }
    
    public void clear() {
        mediaDaoConfig.getIdentityScope().clear();
        documentDaoConfig.getIdentityScope().clear();
    }

    public MediaDao getMediaDao() {
        return mediaDao;
    }

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

}
