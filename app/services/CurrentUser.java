package services;

import com.google.inject.Inject;
import models.JPAUserRepository;
import models.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.sql.profile.DbProfile;
import play.db.jpa.Transactional;

import static play.mvc.Controller.ctx;

public class CurrentUser {

    @Inject
    private PlaySessionStore playSessionStore;
    @Inject
    private JPAUserRepository jpaUserRepository;

    @Transactional
    public User get() {
        return jpaUserRepository.findByEmail(getEmail());
    }

    public String getEmail() {
        return profileManager().get(true).get().getId();
    }

    public boolean isLoggedIn() {
        return profileManager().isAuthenticated();
    }

    private ProfileManager<DbProfile> profileManager() {
        PlayWebContext webContext = new PlayWebContext(ctx(), this.playSessionStore);
        return new ProfileManager<DbProfile>(webContext);
    }
}
