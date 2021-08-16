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

import com.arcadedb.database.Binary;
import com.arcadedb.database.Database;
import com.arcadedb.database.Identifiable;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class BinaryComparator {

  private final          BinarySerializer serializer;
  protected static final long             MILLISEC_PER_DAY = 86400000;

  public BinaryComparator(final BinarySerializer serializer) {
    this.serializer = serializer;
  }

  /**
   * Compares if two values are the same.
   *
   * @param buffer1 First value to compare
   * @param type1   Type of first value
   * @param buffer2 Second value to compare
   * @param type2   Type of second value
   *
   * @return true if they match, otherwise false
   */
  public boolean equals(final Database database, final Binary buffer1, final byte type1, final Binary buffer2, final byte type2) {
    final Object value1 = serializer.deserializeValue(database, buffer1, type1, null);
    final Object value2 = serializer.deserializeValue(database, buffer2, type2, null);

    return equals(value1, type1, value2, type2);
  }

  public boolean equals(final Object value1, final byte type1, final Object value2, final byte type2) {
    switch (type1) {
    case BinaryTypes.TYPE_INT: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).intValue() == ((Number) value2).intValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).intValue() == Integer.parseInt((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_LONG: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).longValue() == ((Number) value2).longValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).longValue() == Long.parseLong((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_SHORT: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).shortValue() == ((Number) value2).shortValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).shortValue() == Short.parseShort((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_STRING: {
      return value1.equals(value2.toString());
    }

    case BinaryTypes.TYPE_DOUBLE: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).doubleValue() == ((Number) value2).doubleValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).doubleValue() == Double.parseDouble((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_FLOAT: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).floatValue() == ((Number) value2).floatValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).floatValue() == Float.parseFloat((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_BYTE: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Number) value1).byteValue() == ((Number) value2).byteValue();

      case BinaryTypes.TYPE_STRING:
        return ((Number) value1).byteValue() == Byte.parseByte((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_BOOLEAN: {
      switch (type2) {
      case BinaryTypes.TYPE_BOOLEAN:
        return value1.equals(value2);

      case BinaryTypes.TYPE_STRING:
        return value1.equals(Boolean.parseBoolean((String) value2));

      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        if ((boolean) value1)
          return ((Number) value2).intValue() == 1;

        return ((Number) value2).intValue() == 0;
      }
      break;
    }

    case BinaryTypes.TYPE_DATE:
    case BinaryTypes.TYPE_DATETIME: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        return ((Date) value1).getTime() == ((Number) value2).longValue();

      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_DATETIME:
        return ((Date) value1).getTime() == ((Date) value2).getTime();

      case BinaryTypes.TYPE_STRING:
        return ((Date) value1).getTime() == Long.parseLong((String) value2);
      }
      break;
    }

    case BinaryTypes.TYPE_BINARY: {
      switch (type2) {
      case BinaryTypes.TYPE_BINARY: {
      }
      }
      throw new UnsupportedOperationException("Comparing binary types");
    }

    case BinaryTypes.TYPE_DECIMAL: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
        return value1.equals(new BigDecimal((Integer) value2));
      case BinaryTypes.TYPE_SHORT:
        return value1.equals(new BigDecimal((Short) value2));
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
        return value1.equals(new BigDecimal((Long) value2));
      case BinaryTypes.TYPE_BYTE:
        return value1.equals(new BigDecimal((Byte) value2));
      case BinaryTypes.TYPE_DECIMAL:
        return value1.equals(value2);
      case BinaryTypes.TYPE_FLOAT:
        return value1.equals(new BigDecimal((Float) value2));
      case BinaryTypes.TYPE_DOUBLE:
        return value1.equals(new BigDecimal((Double) value2));
      case BinaryTypes.TYPE_STRING:
        return value1.equals(new BigDecimal((String) value2));
      }
      break;
    }
    }

    return false;
  }

  public static int compareStrings(final byte[] buffer1, final byte[] buffer2) {
    return UnsignedBytesComparator.BEST_COMPARATOR.compare(buffer1, buffer2);
  }

  public int compareStrings(final byte[] buffer1, final Binary buffer2) {
    final long b1Size = buffer1.length;
    final long b2Size = buffer2.getUnsignedNumber();

    final int minSize = (int) Math.min(b1Size, b2Size);

    for (int i = 0; i < minSize; ++i) {
      final byte b1 = buffer1[i];
      final byte b2 = buffer2.getByte();

      if (b1 > b2)
        return 1;
      else if (b1 < b2)
        return -1;
    }

    if (b1Size == b2Size)
      return 0;

    if (b1Size > b2Size)
      return 1;

    return -1;
  }

  public boolean equalsStrings(final Binary buffer1, final Binary buffer2) {
    final long b1Size = buffer1.getUnsignedNumber();
    final long b2Size = buffer2.getUnsignedNumber();

    if (b1Size != b2Size)
      return false;

    for (int i = 0; i < b1Size; ++i) {
      final byte b1 = buffer1.getByte();
      final byte b2 = buffer2.getByte();

      if (b1 != b2)
        return false;
    }
    return true;
  }

  /**
   * Compares if two values are the same.
   *
   * @param buffer1 First value to compare
   * @param type1   Type of first value
   * @param buffer2 Second value to compare
   * @param type2   Type of second value
   *
   * @return 0 if they matches, >0 if first value is major than second, <0 in case is minor
   */
  public int compare(final Database database, final Binary buffer1, final byte type1, final Binary buffer2, final byte type2) {
    final Object value1 = serializer.deserializeValue(database, buffer1, type1, null);
    final Object value2 = serializer.deserializeValue(database, buffer2, type2, null);

    return compare(value1, type1, value2, type2);
  }

  public int compare(final Object value1, final byte type1, final Object value2, final byte type2) {

    switch (type1) {
    case BinaryTypes.TYPE_INT: {
      final int v1 = ((Number) value1).intValue();
      final int v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).intValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = ((Boolean) value2) ? 1 : 0;
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Integer.parseInt((String) value2);
        break;

      default:
        return -1;
      }

      return Integer.compare(v1, v2);
    }

    case BinaryTypes.TYPE_LONG: {
      final long v1 = ((Number) value1).longValue();
      final long v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).longValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = (long) (((Boolean) value2) ? 1 : 0);
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Long.parseLong((String) value2);
        break;

      default:
        return -1;
      }

      return Long.compare(v1, v2);
    }

    case BinaryTypes.TYPE_SHORT: {
      final short v1 = ((Number) value1).shortValue();
      final short v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).shortValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = (short) (((Boolean) value2) ? 1 : 0);
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Short.parseShort((String) value2);
        break;

      default:
        return -1;
      }

      return Short.compare(v1, v2);
    }

    case BinaryTypes.TYPE_STRING: {
      if (value1 instanceof byte[]) {
        if (value2 instanceof byte[])
          return UnsignedBytesComparator.BEST_COMPARATOR.compare((byte[]) value1, (byte[]) value2);
        else
          return UnsignedBytesComparator.BEST_COMPARATOR.compare((byte[]) value1, ((String) value2).getBytes());
      }

      return ((String) value1).compareTo(value2.toString());
    }

    case BinaryTypes.TYPE_DOUBLE: {
      final double v1 = ((Number) value1).doubleValue();
      final double v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).doubleValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = (double) (((Boolean) value2) ? 1 : 0);
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Double.parseDouble((String) value2);
        break;

      default:
        return -1;
      }

      return Double.compare(v1, v2);
    }

    case BinaryTypes.TYPE_FLOAT: {
      final float v1 = ((Number) value1).floatValue();
      final float v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).floatValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = (float) (((Boolean) value2) ? 1 : 0);
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Float.parseFloat((String) value2);
        break;

      default:
        return -1;
      }

      return Float.compare(v1, v2);
    }

    case BinaryTypes.TYPE_BYTE: {
      final byte v1 = ((Number) value1).byteValue();
      final byte v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).byteValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = (byte) (((Boolean) value2) ? 1 : 0);
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Byte.parseByte((String) value2);
        break;

      default:
        return -1;
      }

      return Byte.compare(v1, v2);
    }

    case BinaryTypes.TYPE_BOOLEAN: {
      final int v1 = ((Boolean) value1) ? 1 : 0;
      final int v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).byteValue();
        break;

      case BinaryTypes.TYPE_BOOLEAN:
        v2 = ((Boolean) value2) ? 1 : 0;
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Boolean.parseBoolean((String) value2) ? 1 : 0;
        break;

      default:
        return -1;
      }

      return Integer.compare(v1, v2);
    }

    case BinaryTypes.TYPE_DATE:
    case BinaryTypes.TYPE_DATETIME: {
      final long v1;
      if (value1 instanceof Date)
        v1 = ((Date) value1).getTime();
      else if (value1 instanceof Calendar)
        v1 = ((Calendar) value1).getTimeInMillis();
      else if (value1 instanceof Number)
        v1 = ((Number) value1).longValue();
      else if (value1 instanceof String)
        v1 = Long.parseLong(value1.toString());
      else
        throw new IllegalArgumentException("Type '" + value1 + "' not supported in comparison for dates");

      final long v2;

      switch (type2) {
      case BinaryTypes.TYPE_INT:
      case BinaryTypes.TYPE_SHORT:
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_BYTE:
      case BinaryTypes.TYPE_DECIMAL:
      case BinaryTypes.TYPE_FLOAT:
      case BinaryTypes.TYPE_DOUBLE:
        v2 = ((Number) value2).longValue();
        break;

      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
        if (value2 instanceof Date)
          v2 = ((Date) value2).getTime();
        else if (value2 instanceof Calendar)
          v2 = ((Calendar) value2).getTimeInMillis();
        else if (value2 instanceof Number)
          v2 = ((Number) value2).longValue();
        else if (value2 instanceof String)
          v2 = Long.parseLong(value2.toString());
        else
          throw new IllegalArgumentException("Type '" + value2 + "' not supported in comparison for dates");
        break;

      case BinaryTypes.TYPE_STRING:
        v2 = Long.parseLong(value2.toString());
        break;

      default:
        return -1;
      }

      return Long.compare(v1, v2);
    }

    case BinaryTypes.TYPE_BINARY: {
      switch (type2) {
      case BinaryTypes.TYPE_BINARY: {
        return ((Binary) value1).compareTo((Binary) value2);
      }
      }
      throw new UnsupportedOperationException("Comparing binary types");
    }

    case BinaryTypes.TYPE_DECIMAL: {
      switch (type2) {
      case BinaryTypes.TYPE_INT:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Integer) value2));
      case BinaryTypes.TYPE_SHORT:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Short) value2));
      case BinaryTypes.TYPE_LONG:
      case BinaryTypes.TYPE_DATETIME:
      case BinaryTypes.TYPE_DATE:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Long) value2));
      case BinaryTypes.TYPE_BYTE:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Byte) value2));
      case BinaryTypes.TYPE_DECIMAL:
        return ((BigDecimal) value1).compareTo((BigDecimal) value2);
      case BinaryTypes.TYPE_FLOAT:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Float) value2));
      case BinaryTypes.TYPE_DOUBLE:
        return ((BigDecimal) value1).compareTo(new BigDecimal((Double) value2));
      case BinaryTypes.TYPE_STRING:
        return ((BigDecimal) value1).compareTo(new BigDecimal((String) value2));
      }
      break;
    }

    case BinaryTypes.TYPE_COMPRESSED_RID:
    case BinaryTypes.TYPE_RID: {
      switch (type2) {
      case BinaryTypes.TYPE_COMPRESSED_RID:
      case BinaryTypes.TYPE_RID:
        return ((Identifiable) value1).getIdentity().compareTo((Identifiable) value2);
      }
    }
    }

    // NO COMPARE SUPPORTED, RETURN NON EQUALS
    throw new IllegalArgumentException("Comparison between type " + type1 + " and " + type2 + " not supported");
  }
}