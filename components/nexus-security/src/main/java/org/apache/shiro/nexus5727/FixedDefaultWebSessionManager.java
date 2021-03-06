/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.apache.shiro.nexus5727;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fixed {@link DefaultWebSessionManager} for issue SHIRO-443. This subclass is put into package of Shiro to have
 * shiro-guice's TypeListener applied to it, and result in same behavior as for other Shiro classes.
 *
 * @author cstamas
 * @see <a href="https://issues.apache.org/jira/browse/SHIRO-443">SHIRO-443 SessionValidationScheduler created multiple
 *      times, enabling it is not thread safe</a>
 * @see <a href="https://issues.sonatype.org/browse/NEXUS-5727>NEXUS-5727 Shiro's session validating thread created
 *      multiple times</a>
 */
public class FixedDefaultWebSessionManager
    extends DefaultWebSessionManager
{
  /**
   * Default HTTP session cookie name that Nexus uses - NXSESSIONID
   */
  public static final String DEFAULT_NEXUS_SESSION_COOKIE_NAME = "NXSESSIONID";

  private static final Logger log = LoggerFactory.getLogger(FixedDefaultWebSessionManager.class);

  @Inject
  public void configureProperties(
      final @Named("${shiro.globalSessionTimeout:-" + DEFAULT_GLOBAL_SESSION_TIMEOUT + "}") long globalSessionTimeout,
      final @Named("${nexus.sessionCookieName:-" + DEFAULT_NEXUS_SESSION_COOKIE_NAME + "}") String sessionCookieName)
  {
    setGlobalSessionTimeout(globalSessionTimeout);
    getSessionIdCookie().setName(sessionCookieName);
  }

  @Inject
  public void configureSessionListeners(final Collection<SessionListener> listeners) {
    setSessionListeners(listeners);
  }

  @Override
  protected synchronized void enableSessionValidation() {
    log.info("Global session timeout: {} ms", getGlobalSessionTimeout());
    final SessionValidationScheduler scheduler = getSessionValidationScheduler();
    if (scheduler == null) {
      super.enableSessionValidation();
    }
  }
}
