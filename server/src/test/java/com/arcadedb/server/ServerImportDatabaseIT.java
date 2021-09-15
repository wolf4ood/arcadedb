/*
 * Copyright 2021 Arcade Data Ltd
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.arcadedb.server;

import com.arcadedb.ContextConfiguration;
import com.arcadedb.GlobalConfiguration;
import com.arcadedb.database.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServerImportDatabaseIT extends BaseGraphServerTest {

  @Override
  protected boolean isCreateDatabases() {
    return false;
  }

  @Override
  protected boolean isPopulateDatabase() {
    return false;
  }

  protected void onServerConfiguration(final ContextConfiguration config) {
    config.setValue(GlobalConfiguration.SERVER_DEFAULT_DATABASES, "Movies[elon:musk]{import:classpath://orientdb-export-small.gz}");
  }

  @Test
  public void checkDefaultDatabases() throws IOException {
    deleteAllDatabases();
    getServer(0).getSecurity().authenticate("elon", "musk");
    Database database = getServer(0).getDatabase("Movies");
    Assertions.assertEquals(500, database.countType("Person", true));
    deleteAllDatabases();
  }
}