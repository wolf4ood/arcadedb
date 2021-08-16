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
package com.arcadedb.server.ha.message;

import com.arcadedb.database.Binary;
import com.arcadedb.server.ha.HAServer;

import java.util.logging.Level;

public class UpdateClusterConfiguration extends HAAbstractCommand {
  private String servers;
  private String replicaServersHTTPAddresses;

  public UpdateClusterConfiguration() {
  }

  public UpdateClusterConfiguration(final String servers, final String replicaServersHTTPAddresses) {
    this.servers = servers;
    this.replicaServersHTTPAddresses = replicaServersHTTPAddresses;
  }

  @Override
  public HACommand execute(final HAServer server, final String remoteServerName, final long messageNumber) {
    server.getServer().log(this, Level.FINE, "Updating server list=%s replicaHTTPs=%s", servers, replicaServersHTTPAddresses);
    server.setServerAddresses(servers);
    server.setReplicasHTTPAddresses(replicaServersHTTPAddresses);
    return null;
  }

  @Override
  public void toStream(final Binary stream) {
    stream.putString(servers);
    stream.putString(replicaServersHTTPAddresses);
  }

  @Override
  public void fromStream(final Binary stream) {
    servers = stream.getString();
    replicaServersHTTPAddresses = stream.getString();
  }

  @Override
  public String toString() {
    return "updateClusterConfig(servers=" + servers + ")";
  }
}