/*
 * https://github.com/achatain/catalog
 *
 * Copyright (C) 2016 Antoine Chatain (achatain [at] outlook [dot] com)
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

import com.google.common.base.Objects;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class HateoasResponseTest {

    @Test
    public void shouldCreateHateoasResponse() throws Exception {
        final TestObject testObject = new TestObject("the name");
        final Link link =  Link.create().withRel("the rel").withHref("the href").withMethod(Link.Method.GET).build();

        final HateoasResponse hateoasResponse = HateoasResponse.create()
                .withObject(testObject)
                .withLink(link)
                .build();

        assertThat(hateoasResponse.getObject(), equalTo(testObject));
        assertThat(hateoasResponse.getLinks().size(), is(1));
        assertThat(hateoasResponse.getLinks().iterator().next(), equalTo(link));
    }

    private static class TestObject {
        private String name;

        private TestObject(final String name) {
            this.name = name;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final TestObject that = (TestObject) o;
            return Objects.equal(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

}
