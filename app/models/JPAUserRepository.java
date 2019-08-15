package models;

import com.google.inject.Inject;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sql.profile.DbProfile;
import org.pac4j.sql.profile.service.DbProfileService;
import play.db.jpa.JPAApi;
import java.util.HashMap;
import java.util.Map;

public class JPAUserRepository extends JPARepository<User> {

    @Inject
    private JPAApi jpaApi;
    @Inject
    private Config config;


    public void create(User user, String type, String password) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", user.getName());
        attributes.put("username", user.getEmail());
        attributes.put("address", user.getAddress());
        attributes.put("type", type);

        DbProfile profile = new DbProfile();
        profile.build(user.getEmail(), attributes);
        profile.setRemembered(true);
        dbProfileService().create(profile, password);
    }

    public User findById(int id){
        return findById(User.class, id);
    }

    public User findByEmail(String email){
        return findByAttribute(User.class, "email", email);
    }

    private DbProfileService dbProfileService() {
        return (DbProfileService) formClient().getAuthenticator();
    }

    private FormClient formClient() {
        return (FormClient) config.getClients().findClient("FormClient");
    }


    public User updateAttrs(int id, Map<String, String> attrs){

        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            if (!attr.getKey().equals("password") && !attr.getValue().isEmpty()) {
                update(User.class, id, attr.getKey(), attr.getValue());
            }
        }

        return findById(id);
    }
}
