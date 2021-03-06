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
package org.sonatype.nexus.bootstrap.log;

import ch.qos.logback.access.pattern.AccessConverter;
import ch.qos.logback.access.spi.IAccessEvent;

/**
 * Converter for the request attribute named by {@link #ATTR_USER_ID}
 *
 * @since 2.11.1
 */
public class NexusUserIdConverter
    extends AccessConverter
{
  /**
   * "nexus.user.id" request attribute name
   *
   * @see org.sonatype.nexus.web.internal.SecurityFilter
   */
  private static final String ATTR_USER_ID = "nexus.user.id";

  public String convert(IAccessEvent accessEvent) {
    return accessEvent.getAttribute(ATTR_USER_ID);
  }
}

