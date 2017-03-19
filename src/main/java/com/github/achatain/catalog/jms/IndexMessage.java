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

package com.github.achatain.catalog.jms;

import java.io.Serializable;

public class IndexMessage implements Serializable {

    private String userId;
    private String colId;
    private String indexId;

    private IndexMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public String getColId() {
        return colId;
    }

    public String getIndexId() {
        return indexId;
    }

    @Override
    public String toString() {
        return "IndexMessage{" + "userId='" + userId + '\'' + ", colId='" + colId + '\'' + ", indexId='" + indexId + '\'' + '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String userId;
        private String colId;
        private String fieldName;

        private Builder() {
        }

        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withColId(final String colId) {
            this.colId = colId;
            return this;
        }

        public Builder withFieldName(final String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public IndexMessage build() {
            final IndexMessage indexMessage = new IndexMessage();
            indexMessage.userId = userId;
            indexMessage.colId = colId;
            indexMessage.indexId = fieldName;
            return indexMessage;
        }
    }
}
