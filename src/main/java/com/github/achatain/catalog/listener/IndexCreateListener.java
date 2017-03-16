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

package com.github.achatain.catalog.listener;

import com.github.achatain.catalog.service.CollectionService;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class IndexCreateListener implements MessageListener {

    private CollectionService collectionService;

    @Override
    public void onMessage(final Message message) {
        try {
            final String body = message.getBody(String.class);
            System.out.println("\tReceived body is " + body);
            collectionService.listCollections("bla");
        } catch (final JMSException e) {
            e.printStackTrace();
        }
    }

    @Inject
    private void setCollectionService(final CollectionService collectionService) {
        this.collectionService = collectionService;
    }
}
