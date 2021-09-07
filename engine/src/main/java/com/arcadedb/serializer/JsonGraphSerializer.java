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

package com.arcadedb.serializer;

import com.arcadedb.database.Document;
import com.arcadedb.graph.Edge;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonGraphSerializer {

  private boolean expandVertexEdges = false;

  public JSONObject serializeRecord(final Document document) {
    final JSONObject object = new JSONObject();

    object.put("r", document.getIdentity().toString());
    object.put("t", document.getTypeName());

    final JSONObject properties = new JSONObject();
    object.put("p", properties);

    for (String p : document.getPropertyNames()) {
      Object value = document.get(p);

      if (value instanceof Document)
        value = serializeRecord((Document) value);
      else if (value instanceof Collection) {
        final List<Object> list = new ArrayList<>();
        for (Object o : (Collection) value) {
          if (o instanceof Document)
            o = serializeRecord((Document) o);
          list.add(o);
        }
        value = list;
      }
      properties.put(p, value);
    }

    setMetadata(document, object);

    return object;
  }

  public JSONObject serializeResult(final Result record) {
    final JSONObject object = new JSONObject();

    if (record.isElement()) {
      final Document document = record.toElement();
      object.put("r", document.getIdentity().toString());
      object.put("t", document.getTypeName());
      setMetadata(document, object);
    }

    final JSONObject properties = new JSONObject();
    object.put("p", properties);

    for (String p : record.getPropertyNames()) {
      Object value = record.getProperty(p);

      if (value instanceof Document)
        value = serializeRecord((Document) value);
      else if (value instanceof Result)
        value = serializeResult((Result) value);
      else if (value instanceof Collection) {
        final List<Object> list = new ArrayList<>();
        for (Object o : (Collection) value) {
          if (o instanceof Document)
            o = serializeRecord((Document) o);
          else if (o instanceof Result)
            o = serializeResult((Result) o);
          list.add(o);
        }
        value = list;
      }
      properties.put(p, value);
    }

    return object;
  }

  public boolean isExpandVertexEdges() {
    return expandVertexEdges;
  }

  public JsonGraphSerializer setExpandVertexEdges(final boolean expandVertexEdges) {
    this.expandVertexEdges = expandVertexEdges;
    return this;
  }

  private void setMetadata(final Document document, final JSONObject object) {
    if (document instanceof Vertex) {
      final Vertex vertex = ((Vertex) document);

      if (expandVertexEdges) {
        final JSONArray outEdges = new JSONArray();
        for (Edge e : vertex.getEdges(Vertex.DIRECTION.OUT))
          outEdges.put(e.getIdentity().toString());
        object.put("o", outEdges);

        final JSONArray inEdges = new JSONArray();
        for (Edge e : vertex.getEdges(Vertex.DIRECTION.IN))
          inEdges.put(e.getIdentity().toString());
        object.put("i", inEdges);
      } else {
        object.put("i", vertex.countEdges(Vertex.DIRECTION.IN, null));
        object.put("o", vertex.countEdges(Vertex.DIRECTION.OUT, null));
      }

    } else if (document instanceof Edge) {
      final Edge edge = ((Edge) document);
      object.put("i", edge.getIn());
      object.put("o", edge.getOut());
    }
  }
}