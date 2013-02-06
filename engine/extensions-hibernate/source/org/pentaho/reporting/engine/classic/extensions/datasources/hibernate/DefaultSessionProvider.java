/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2001 - 2009 Object Refinery Ltd, Pentaho Corporation and Contributors.  All rights reserved.
 */

package org.pentaho.reporting.engine.classic.extensions.datasources.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * A simple default implementation that opens the default configuration to create a default session factory to get a
 * default session. (A lot of defaults, so it may or may not fit into real world applications. This provider does not
 * deal with transactions or optimizes caching, loading or what else could be optimized in Hibernate-Queries.)
 *
 * @author Thomas Morgner
 */
public class DefaultSessionProvider implements SessionProvider
{
  private SessionFactory sessionFactory;

  public DefaultSessionProvider()
  {
    // Create the SessionFactory from hibernate.cfg.xml

  }

  public Session getSession() throws HibernateException
  {
    if (sessionFactory == null)
    {
      sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    return sessionFactory.openSession();
  }
}