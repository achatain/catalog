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

import com.github.achatain.catalog.jms.IndexMessage;
import com.github.achatain.catalog.service.CollectionService;

import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

public class IndexCreateListener implements MessageListener {

    private CollectionService collectionService;

    @Override
    public void onMessage(final Message message) {
        IndexMessage indexMessage = null;

        try {
            indexMessage = message.getBody(IndexMessage.class);
            collectionService.createIndex(indexMessage.getUserId(), indexMessage.getColId(), indexMessage.getFieldName());
        } catch (final Exception e) {
            throw new RuntimeException("Failed to process JMS message " + indexMessage, e);
        }
    }

    @Inject
    private void setCollectionService(final CollectionService collectionService) {
        this.collectionService = collectionService;
    }
}
