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

package com.github.achatain.catalog.entity;

public final class Field {

    private String name;
    // TODO ensure the following field is not persisted
    private transient boolean indexed;

    private Field() {
    }

    public String getName() {
        return name;
    }

    public boolean getIndexed() {
        return indexed;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private boolean indexed;

        private Builder() {
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withIndexed(final boolean indexed) {
            this.indexed = indexed;
            return this;
        }

        public Field build() {
            final Field field = new Field();
            field.name = name;
            field.indexed = indexed;
            return field;
        }
    }
}
