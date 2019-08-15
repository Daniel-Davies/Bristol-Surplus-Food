package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.*;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.sql.profile.DbProfile;
import org.pac4j.sql.profile.service.DbProfileService;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.CurrentUser;

import java.util.*;

public class UserController extends Controller {

    @Inject
    FormFactory formFactory;
    @Inject
    JPAUserRepository jpaUserRepository;
    @Inject
    FormClient formClient;
    @Inject
    PlaySessionStore playSessionStore;
    @Inject
    JwtGenerator jwtGenerator;
    @Inject
    private CurrentUser currentUser;
    @Inject
    private Config config;


    @Transactional
    public Result jwt() {
        PlayWebContext webContext = new PlayWebContext(ctx(), playSessionStore);

        try {
            UsernamePasswordCredentials credentials = formClient.getCredentials(webContext);
            formClient.getAuthenticator().validate(credentials, webContext);
            String token = jwtGenerator.generate(credentials.getUserProfile());
            return ok(token);
        }
        catch (Exception ex) {
            return forbidden();
        }
    }

    @Transactional
    public Result create() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest("name", "email", "address").get();

        String type = request().body().asFormUrlEncoded().get("type")[0];

        if(!type.equals("Supplier") && !type.equals("Beneficiary") && !type.equals("Deliverer")){
            return  badRequest("User type must Supplier, Beneficiary or Deliverer");
        }

        DynamicForm formData = formFactory.form().bindFromRequest("password", "password_confirmation");
        String pw = formData.rawData().get("password");
        String pwConf = formData.rawData().get("password_confirmation");

        if (pw != null && pwConf != null && pw.equals(pwConf) && !userForm.hasErrors()) {

            jpaUserRepository.create(user, type, pw);
            // check if it succeeds
            flash("message", "You have been registered. Please, try to login now.");
            return redirect(routes.HomeController.login());
        }

        return badRequest(views.html.user.register.render(userForm, user));
    }

    @Transactional
    public Result edit() {
        User u = currentUser.get();


        Form<User> form = formFactory.form(User.class).fill(u);
        return ok(views.html.user.settings.render(u, form));
    }

    @Transactional
    public  Result update(){
        User user = currentUser.get();

        Form<User> form = formFactory.form(User.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(views.html.user.settings.render(user, form));
        }

        Map<String, String> attrs = new HashMap<>();
        attrs.put("name", form.rawData().get("name"));
        attrs.put("address", form.rawData().get("address"));

        jpaUserRepository.updateAttrs(user.getId(), attrs);
        return redirect(routes.HomeController.index());
    }

    @Transactional
    public  Result updateJson(){
        User user = currentUser.get();
        int id = user.getId();

        JsonNode reqBody = request().body().asJson();
        if(reqBody == null){
            return  badRequest("No Json body\n");
        }
        Map<String, String> attrs = new HashMap<>();
        attrs.put("name", reqBody.get("name").toString());
        attrs.put("address", reqBody.get("address").toString());

        jpaUserRepository.updateAttrs(id, attrs);
        return ok();
    }
}
