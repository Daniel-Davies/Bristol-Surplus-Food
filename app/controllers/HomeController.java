package controllers;

import com.google.inject.Inject;
import models.*;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.*;
import services.CurrentUser;
import views.html.*;

import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @Inject
    private Config config;

    @Inject
    FormFactory formFactory;

    @Inject
    private CurrentUser currentUser;

    @Transactional
    public Result index() {

        if (currentUser.isLoggedIn()) {
            User u = currentUser.get();

            switch (u.getType()) {
                case "Supplier":
                    return ok(views.html.supplier.dashboard.render((Supplier) u));
                case "Beneficiary":
                    return ok(views.html.beneficiaries.dashboard.render((Beneficiary) u));
                case "Deliverer":
                    return ok(views.html.deliverer.dashboard.render((Deliverer) u));
                default:
                    return ok(index.render(currentUser.getEmail(), u));
            }
        } else {
            return ok(index.render("You are not logged in.", null));
        }
    }


    public Result login() {
        DynamicForm form = formFactory.form();
        return ok(login.render(formClient().getCallbackUrl(), form));
    }

    @Transactional
    public Result register() {
        if (currentUser.isLoggedIn()) {
            return redirect(routes.HomeController.index());
        }

        Form<User> registerForm = formFactory.form(User.class);
        return ok(views.html.user.register.render(registerForm, null));
    }

    private FormClient formClient() {
        return (FormClient) config.getClients().findClient("FormClient");
    }
}
