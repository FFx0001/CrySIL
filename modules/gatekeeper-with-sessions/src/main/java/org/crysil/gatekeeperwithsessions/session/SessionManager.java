package org.crysil.gatekeeperwithsessions.session;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    protected static SessionManager instance;
    protected List<SessionTokenBean> sessions = new ArrayList<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    public SessionTokenBean createSession() {
        SessionTokenBean session = new SessionTokenBean();
        sessions.add(session);

        return session;
    }

    public SessionTokenBean getSession(String id) {
        for (SessionTokenBean session : sessions) {
            if (session.getSessionID().equals(id)) {
                return session;
            }
        }
        return null;
    }

    public void removeOutdatedEntries() {
        List<SessionTokenBean> toBeRemoved = new ArrayList<>();

        for (SessionTokenBean session : sessions) {
            if (session.isExpired()) {
                toBeRemoved.add(session);
            }
        }

        for (SessionTokenBean session : toBeRemoved) {
            sessions.remove(session);
        }
    }
}