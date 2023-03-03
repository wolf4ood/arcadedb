/*
 * Copyright © 2021-present Arcade Data Ltd (info@arcadedata.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-FileCopyrightText: 2021-present Arcade Data Ltd (info@arcadedata.com)
 * SPDX-License-Identifier: Apache-2.0
 */
/* Generated By:JJTree: Do not edit this line. OBinaryCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Identifiable;
import com.arcadedb.database.Record;
import com.arcadedb.query.sql.executor.CommandContext;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.schema.DocumentType;

import java.util.*;

public class BinaryCondition extends BooleanExpression {
  protected Expression            left;
  protected BinaryCompareOperator operator;
  protected Expression            right;

  public BinaryCondition(final int id) {
    super(id);
  }

  @Override
  public boolean evaluate(final Identifiable currentRecord, final CommandContext context) {
    return operator.execute(context.getDatabase(), left.execute(currentRecord, context), right.execute(currentRecord, context));
  }

  @Override
  public boolean evaluate(final Result currentRecord, final CommandContext context) {
    final Object leftVal = left.execute(currentRecord, context);
    final Object rightVal = right.execute(currentRecord, context);
    return operator.execute(context.getDatabase(), leftVal, rightVal);
  }

  public void toString(final Map<String, Object> params, final StringBuilder builder) {
    left.toString(params, builder);
    builder.append(" ");
    builder.append(operator.toString());
    builder.append(" ");
    right.toString(params, builder);
  }

  public long estimateIndexed(final FromClause target, final CommandContext context) {
    return left.estimateIndexedFunction(target, context, operator, right.execute((Result) null, context));
  }

  public Iterable<Record> executeIndexedFunction(final FromClause target, final CommandContext context) {
    return left.executeIndexedFunction(target, context, operator, right.execute((Result) null, context));
  }

  /**
   * tests if current expression involves an indexed function AND that function can also be executed without using the index
   *
   * @param target  the query target
   * @param context the execution context
   *
   * @return true if current expression involves an indexed function AND that function can be used on this target, false otherwise
   */
  public boolean canExecuteIndexedFunctionWithoutIndex(final FromClause target, final CommandContext context) {
    return left.canExecuteIndexedFunctionWithoutIndex(target, context, operator, right.execute((Result) null, context));
  }

  /**
   * tests if current expression involves an indexed function AND that function can be used on this target
   *
   * @param target  the query target
   * @param context the execution context
   *
   * @return true if current expression involves an indexed function AND that function can be used on this target, false otherwise
   */
  public boolean allowsIndexedFunctionExecutionOnTarget(final FromClause target, final CommandContext context) {
    return left.allowsIndexedFunctionExecutionOnTarget(target, context, operator, right.execute((Result) null, context));
  }

  /**
   * tests if current expression involves an indexed function AND the function has also to be executed after the index search. In
   * some cases, the index search is accurate, so this condition can be excluded from further evaluation. In other cases the result
   * from the index is a superset of the expected result, so the function has to be executed anyway for further filtering
   *
   * @param target  the query target
   * @param context the execution context
   *
   * @return true if current expression involves an indexed function AND the function has also to be executed after the index
   * search.
   */
  public boolean executeIndexedFunctionAfterIndexSearch(final FromClause target, final CommandContext context) {
    return left.executeIndexedFunctionAfterIndexSearch(target, context, operator, right.execute((Result) null, context));
  }

  public List<BinaryCondition> getIndexedFunctionConditions(final DocumentType iSchemaClass, final CommandContext context) {
    if (left.isIndexedFunctionCal(context)) {
      return Collections.singletonList(this);
    }
    return null;
  }

  @Override
  public BinaryCondition copy() {
    final BinaryCondition result = new BinaryCondition(-1);
    result.left = left.copy();
    result.operator = operator.copy();
    result.right = right.copy();
    return result;
  }

  @Override
  public void extractSubQueries(final SubQueryCollector collector) {
    left.extractSubQueries(collector);
    right.extractSubQueries(collector);
  }

  @Override
  public Optional<UpdateItem> transformToUpdateItem() {
    if (!checkCanTransformToUpdate()) {
      return Optional.empty();
    }
    if (operator instanceof EqualsCompareOperator) {
      final UpdateItem result = new UpdateItem(-1);
      result.operator = UpdateItem.OPERATOR_EQ;
      final BaseExpression baseExp = ((BaseExpression) left.mathExpression);
      result.left = baseExp.identifier.suffix.identifier.copy();
      result.leftModifier = baseExp.modifier == null ? null : baseExp.modifier.copy();
      result.right = right.copy();
      return Optional.of(result);
    }
    return super.transformToUpdateItem();
  }

  private boolean checkCanTransformToUpdate() {
    if (left == null || left.mathExpression == null || !(left.mathExpression instanceof BaseExpression)) {
      return false;
    }
    final BaseExpression base = (BaseExpression) left.mathExpression;
    return base.identifier != null && base.identifier.suffix != null && base.identifier.suffix.identifier != null;
  }

  public Expression getLeft() {
    return left;
  }

  public BinaryCompareOperator getOperator() {
    return operator;
  }

  public Expression getRight() {
    return right;
  }

  public void setLeft(final Expression left) {
    this.left = left;
  }

  public void setOperator(final BinaryCompareOperator operator) {
    this.operator = operator;
  }

  public void setRight(final Expression right) {
    this.right = right;
  }

  @Override
  protected Object[] getIdentityElements() {
    return new Object[] { left, operator, right };
  }

  @Override
  public List<String> getMatchPatternInvolvedAliases() {
    final List<String> leftX = left.getMatchPatternInvolvedAliases();
    final List<String> rightX = right.getMatchPatternInvolvedAliases();
    if (leftX == null) {
      return rightX;
    }
    if (rightX == null) {
      return leftX;
    }

    final List<String> result = new ArrayList<>(leftX.size() + rightX.size());
    result.addAll(leftX);
    result.addAll(rightX);
    return result;
  }

  @Override
  protected SimpleNode[] getCacheableElements() {
    return new SimpleNode[] { left, right };
  }

  @Override
  public boolean createRangeWith(final BooleanExpression match) {
    if (!(match instanceof BinaryCondition))
      return false;

    final BinaryCondition metchingCondition = (BinaryCondition) match;
    if (!metchingCondition.getLeft().equals(this.getLeft()))
      return false;

    final BinaryCompareOperator leftOperator = metchingCondition.getOperator();
    final BinaryCompareOperator rightOperator = this.getOperator();
    if (leftOperator instanceof GeOperator || leftOperator instanceof GtOperator)
      return rightOperator instanceof LeOperator || rightOperator instanceof LtOperator;

    if (leftOperator instanceof LeOperator || leftOperator instanceof LtOperator)
      return rightOperator instanceof GeOperator || rightOperator instanceof GtOperator;

    return false;
  }

  @Override
  public Expression resolveKeyFrom(final BinaryCondition additional) {
    BinaryCompareOperator operator = getOperator();
    if ((operator instanceof EqualsCompareOperator) || (operator instanceof GtOperator) || (operator instanceof GeOperator)
        || (operator instanceof ContainsKeyOperator) || (operator instanceof ContainsValueOperator)) {
      return getRight();
    } else if (additional != null) {
      return additional.getRight();
    }
    return null;
  }

  @Override
  public Expression resolveKeyTo(final BinaryCondition additional) {
    BinaryCompareOperator operator = this.getOperator();
    if ((operator instanceof EqualsCompareOperator) || (operator instanceof LtOperator) || (operator instanceof LeOperator)
        || (operator instanceof ContainsKeyOperator) || (operator instanceof ContainsValueOperator)) {
      return getRight();
    } else if (additional != null) {
      return additional.getRight();
    }
    return null;
  }
}
/* JavaCC - OriginalChecksum=99ed1dd2812eb730de8e1931b1764da5 (do not edit this line) */
