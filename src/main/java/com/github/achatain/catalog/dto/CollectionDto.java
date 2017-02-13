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

package com.github.achatain.catalog.dto;

import com.github.achatain.catalog.entity.Field;

import java.util.List;

public final class CollectionDto extends HateoasDto {

    private String id;
    private String name;
    private List<Field> fields;

    private CollectionDto() {
        super();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String name;
        private List<Field> fields;

        private Builder() {
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withFields(final List<Field> fields) {
            this.fields = fields;
            return this;
        }

        public CollectionDto build() {
            final CollectionDto collectionDto = new CollectionDto();
            collectionDto.id = id;
            collectionDto.name = name;
            collectionDto.fields = fields;
            return collectionDto;
        }
    }
}
