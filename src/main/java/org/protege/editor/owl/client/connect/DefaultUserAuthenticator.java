package org.protege.editor.owl.client.connect;

import org.protege.owl.server.connect.RemoteLoginService;

import edu.stanford.protege.metaproject.Manager;
import edu.stanford.protege.metaproject.api.AuthToken;
import edu.stanford.protege.metaproject.api.Factory;
import edu.stanford.protege.metaproject.api.PlainPassword;
import edu.stanford.protege.metaproject.api.Salt;
import edu.stanford.protege.metaproject.api.SaltedPasswordDigest;
import edu.stanford.protege.metaproject.api.UserAuthenticator;
import edu.stanford.protege.metaproject.api.UserId;

/**
 * @author Josef Hardi <johardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public final class DefaultUserAuthenticator implements UserAuthenticator {

    private RemoteLoginService loginService;

    public DefaultUserAuthenticator(RemoteLoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public AuthToken hasValidCredentials(UserId userId, PlainPassword password) throws Exception {
        Salt userSalt = (Salt) loginService.getEncryptionKey(userId);
        Factory f = Manager.getFactory();
        SaltedPasswordDigest passwordDigest = f.getSaltedPasswordDigest(password.getPassword(), userSalt);
        AuthToken authToken = loginService.login(userId, passwordDigest);
        return authToken;
    }
}