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

package com.github.achatain.catalog.service.impl;

import com.github.achatain.catalog.dao.UserDao;
import com.github.achatain.catalog.service.UserService;
import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;

import javax.inject.Inject;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final transient UserDao userDao;

    @Inject
    UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void saveIfNotFound(final AuthenticatedUser user) {
        final Optional<AuthenticatedUser> foundUser = userDao.findUser(user.getId());
    }
}
