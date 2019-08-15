package modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import controllers.SecureHttpActionAdapter;
import models.User;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.authenticator.LocalCachingAuthenticator;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.config.encryption.EncryptionConfiguration;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.config.signature.SignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.CallbackController;
import org.pac4j.play.LogoutController;
import org.pac4j.play.store.PlayCacheSessionStore;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.sql.profile.service.DbProfileService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;
import play.Configuration;
import play.Environment;
import play.db.DBApi;
import play.cache.SyncCacheApi;
import java.util.concurrent.TimeUnit;

public class SecurityModule extends AbstractModule {
    private final String baseUrl;
    private PlaySessionStore playSessionStore;
    private final String secret;

    public SecurityModule(Environment environment, Configuration configuration) {
        this.baseUrl = configuration.getString("application.baseUrl", "");
        this.secret = configuration.getString("JWT_SALT");
    }

    @Override
    protected void configure() {
        this.playSessionStore = new PlayCacheSessionStore(getProvider(SyncCacheApi.class));
        //bind(PlaySessionStore.class).toInstance(playCacheSessionStore);
        bind(PlaySessionStore.class).to(PlayCacheSessionStore.class);

        // callback
        final CallbackController callbackController = new CallbackController();
        callbackController.setDefaultUrl("/");
        bind(CallbackController.class).toInstance(callbackController);

        // logout
        final LogoutController logoutController = new LogoutController();
        logoutController.setDefaultUrl("/");
        //logoutController.setDestroySession(true);
        bind(LogoutController.class).toInstance(logoutController);
    }

    @Provides
    protected SignatureConfiguration provideSigatureConfiguration() {
        SecretSignatureConfiguration config = new SecretSignatureConfiguration(secret);
        return config;
    }

    @Provides
    protected EncryptionConfiguration provideEncryptionConfiguration() {
        SecretEncryptionConfiguration config = new SecretEncryptionConfiguration(secret);
        return config;
    }

    @Provides
    protected JwtAuthenticator getJwtAuthenticator(EncryptionConfiguration encrypt,
                                                  SignatureConfiguration sign) {
        return new JwtAuthenticator(sign);
    }

    @Provides
    protected JwtGenerator getJwtGenerator(EncryptionConfiguration encrypt,
                                           SignatureConfiguration sign) {
        return new JwtGenerator(sign);
    }

    @Provides
    protected ParameterClient provideParameterClient(JwtAuthenticator authenticator, DBApi dbApi) {
        // cache result of JWT auth
        LocalCachingAuthenticator cachingAuth = new LocalCachingAuthenticator(
                authenticator, 10000, 15, TimeUnit.MINUTES);

        ParameterClient client = new ParameterClient("token", cachingAuth);
        client.setSupportGetRequest(true);
        client.setSupportPostRequest(true);
        return client;
    }

    @Provides
    protected FormClient provideFormClient(DBApi dbApi) {
        DataSource ds = dbApi.getDatabase("default").getDataSource();

        // use BCrypt for PW encryption
        PasswordEncoder passwordEncoder = new SpringSecurityPasswordEncoder(new BCryptPasswordEncoder());

        DbProfileService dbProfileService = new DbProfileService(ds, passwordEncoder);
        dbProfileService.setUsersTable("Users");
        dbProfileService.setUsernameAttribute("email");
        dbProfileService.setIdAttribute("email");
        dbProfileService.setPasswordAttribute("password");
        dbProfileService.setAttributes(User.getAttributeNames());

        FormClient formClient = new FormClient(baseUrl + "/login", dbProfileService);
        formClient.setUsernameParameter("email");
        formClient.setPasswordParameter("password");
        formClient.setCallbackUrl(baseUrl + "/callback");

        return formClient;
    }

    @Provides
    protected Config provideConfig(FormClient formClient, ParameterClient parameterClient) {
        final Config config = new Config(baseUrl + "/callback", formClient, parameterClient);
        config.setHttpActionAdapter(new SecureHttpActionAdapter());
        config.setSessionStore(playSessionStore);
        return config;
    }
}