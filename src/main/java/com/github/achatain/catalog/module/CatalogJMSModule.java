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

import com.github.achatain.catalog.listener.IndexCreateListener;
import com.github.achatain.catalog.listener.IndexDropListener;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CatalogJMSModule extends AbstractModule {

    private static final String CONNECTION_FACTORY = "ConnectionFactory";
    private static final String INDEX_CREATE_DESTINATION = "jms/queue/IndexCreate";
    private static final String INDEX_DROP_DESTINATION = "jms/queue/IndexDrop";

    private final JMSContext jmsContext;
    private final Destination indexCreateDestination;
    private final Destination indexDropDestination;
    private final IndexCreateListener indexCreateListener;
    private final IndexDropListener indexDropListener;

    CatalogJMSModule() {
        try {
            final Context context = new InitialContext();
            final ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY);

            indexCreateDestination = (Destination) context.lookup(INDEX_CREATE_DESTINATION);
            indexDropDestination = (Destination) context.lookup(INDEX_DROP_DESTINATION);

            jmsContext = connectionFactory.createContext();

            final Connection connection = connectionFactory.createConnection();
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            indexCreateListener = new IndexCreateListener();
            session.createConsumer(indexCreateDestination).setMessageListener(indexCreateListener);

            indexDropListener = new IndexDropListener();
            session.createConsumer(indexDropDestination).setMessageListener(indexDropListener);

            connection.start();
        } catch (final NamingException | JMSException e) {
            throw new RuntimeException("Unable to initiate the JMS context", e);
        }
    }

    @Override
    protected void configure() {
        requestInjection(indexCreateListener);
        requestInjection(indexDropListener);
        bind(JMSContext.class).toInstance(jmsContext);
        bind(Destination.class).annotatedWith(Names.named("indexCreate")).toInstance(indexCreateDestination);
        bind(Destination.class).annotatedWith(Names.named("indexDrop")).toInstance(indexDropDestination);
    }
}
