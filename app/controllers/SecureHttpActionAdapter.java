package controllers;

import play.mvc.*;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import views.html.defaultpages.*;

import static play.mvc.Results.*;

public class SecureHttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Result adapt(int code, PlayWebContext context) {

        if (code == HttpConstants.UNAUTHORIZED) {
            return unauthorized(unauthorized.render());
        }
        else if (code == HttpConstants.FORBIDDEN) {
            return forbidden(unauthorized.render());
        }
        else {
            return super.adapt(code, context);
        }
    }
}
