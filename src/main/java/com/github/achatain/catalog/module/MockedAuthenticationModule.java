/*
 * https://github.com/achatain/catalog
 *
 * Copyright (C) 2017 Antoine Chatain (achatain [at] outlook [dot] com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.achatain.catalog.module;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.github.achatain.javawebappauthentication.service.impl.GoogleAuthenticationServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public class MockedAuthenticationModule extends AbstractModule {

    private static final String ISSUER = "accounts.google.com";
    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(GoogleAuthenticationServiceImpl.class);
        bind(SessionService.class).to(MockedSessionService.class);
    }

    @Provides
    @Singleton
    private Gson provideGson() {
        return GSON_BUILDER.create();
    }

    @Provides
    private GoogleIdTokenVerifier provideGoogleIdTokenVerifier() {
        try {
            final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            return new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setIssuer(ISSUER).build();
        }
        catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not create a GoogleIdTokenVerifier", e);
        }
    }

    private static class MockedSessionService implements SessionService {

        @Override
        public boolean isUserLoggedIn(final HttpSession session, final String redirectUrl) {
            return true;
        }

        @Override
        public Optional<AuthenticatedUser> getUserFromSession(final HttpSession session) {
            return Optional.of(
                    AuthenticatedUser.create()
                            .withId("goog-118064819047911511899")
                            .withName("Mocky")
                            .withEmail("mocked.user@gmail.com")
                            .withFamilyName("Mc Mock Face")
                            .withGivenName("Mocky")
                            .build()
            );
        }

        @Override
        public void putUserInSession(final HttpSession session, final AuthenticatedUser authenticatedUser) {
            // no-op
        }

        @Override
        public void invalidateSession(final HttpSession session) {
            // no-op
        }

        @Override
        public String popOriginalRequestUrl(final HttpSession session) {
            return "http://localhost:8080/catalog";
        }
    }
}
