package splat.parser.elements.expressions;

import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.executor.values.ValueFac;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.constants.operators.BinaryOperator;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class BinaryOpExpr extends Expression {
	private Expression expr1;
	private Expression expr2;
	private BinaryOperator op;

	public BinaryOpExpr(Token tok, Expression expr1, Expression expr2, BinaryOperator op) {
		super(tok);
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.op = op;
	}

	public Expression getExpr1() {
		return expr1;
	}

	public Expression getExpr2() {
		return expr2;
	}

	public BinaryOperator getOp() {
		return op;
	}

	public String toString() {
		return String.format("(%s %s %s)", expr1, op, expr2);
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		Type type1 = expr1.analyzeAndGetType(funcMap, varAndParamMap);
		Type type2 = expr2.analyzeAndGetType(funcMap, varAndParamMap);

		if (!type1.equalsTo(type2)) {
			throw new SemanticAnalysisException("Type mismatch in binary operation", this);
		}

		if (op.equals(BinaryOperator.AND, BinaryOperator.OR)) {
			if (!type1.equalsTo(Type.BOOLEAN)) {
				throw new SemanticAnalysisException("Type mismatch in binary operation", this);
			}
			return new Type(Type.BOOLEAN);
		}

		if (op.equals(BinaryOperator.LT, BinaryOperator.GT, BinaryOperator.LTE, BinaryOperator.GTE)) {
			if (!type1.equalsTo(Type.INTEGER)) {
				throw new SemanticAnalysisException("Type mismatch in binary operation", this);
			}
			return new Type(Type.BOOLEAN);
		}

		if (op.equals(BinaryOperator.MOD, BinaryOperator.MINUS, BinaryOperator.MULT, BinaryOperator.DIV)) {
			if (!type1.equalsTo(Type.INTEGER)) {
				throw new SemanticAnalysisException("Type mismatch in binary operation", this);
			}
			return new Type(Type.INTEGER);
		}

		if (op.equals(BinaryOperator.PLUS)) {
			if (!type1.equalsTo(Type.INTEGER) && !type1.equalsTo(Type.STRING)) {
				throw new SemanticAnalysisException("Type mismatch in binary operation", this);
			}
			return type1;
		}

		if (op.equals(BinaryOperator.EQ)) {
			return new Type(Type.BOOLEAN);
		}

		return new Type(Type.BOOLEAN);
	}

	@Override
	public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ExecutionException {
		Value val1 = expr1.evaluate(funcMap, varAndParamMap);
		Value val2 = expr2.evaluate(funcMap, varAndParamMap);

		switch (val1.getType().getType()) {
			case Type.BOOLEAN: {
				boolean b1 = (boolean) val1.getValue();
				boolean b2 = (boolean) val2.getValue();
				switch (op) {
					case AND: {
						return ValueFac.createValue(Type.BOOLEAN, b1 && b2);
					}
					case OR: {
						return ValueFac.createValue(Type.BOOLEAN, b1 || b2);
					}
					case EQ: {
						return ValueFac.createValue(Type.BOOLEAN, b1 == b2);
					}
					default: {
						throw new ExecutionException("Invalid operation on boolean", this);
					}
				}
			}
			case Type.INTEGER: {
				int i1 = (int) val1.getValue();
				int i2 = (int) val2.getValue();
				switch (op) {
					case PLUS: {
						return ValueFac.createValue(Type.INTEGER, i1 + i2);
					}
					case MINUS: {
						return ValueFac.createValue(Type.INTEGER, i1 - i2);
					}
					case MULT: {
						return ValueFac.createValue(Type.INTEGER, i1 * i2);
					}
					case DIV: {
						if (i2 == 0) {
							throw new ExecutionException("Division by zero", this);
						}
						return ValueFac.createValue(Type.INTEGER, i1 / i2);
					}
					case MOD: {
						return ValueFac.createValue(Type.INTEGER, i1 % i2);
					}
					case EQ: {
						return ValueFac.createValue(Type.BOOLEAN, i1 == i2);
					}
					case LT: {
						return ValueFac.createValue(Type.BOOLEAN, i1 < i2);
					}
					case GT: {
						return ValueFac.createValue(Type.BOOLEAN, i1 > i2);
					}
					case LTE: {
						return ValueFac.createValue(Type.BOOLEAN, i1 <= i2);
					}
					case GTE: {
						return ValueFac.createValue(Type.BOOLEAN, i1 >= i2);
					}
					default: {
						throw new ExecutionException("Invalid operation on integer", this);
					}
				}
			}
			case Type.STRING:
				String s1 = (String) val1.getValue();
				String s2 = (String) val2.getValue();
				switch (op) {
					case PLUS: {
						return ValueFac.createValue(Type.STRING, s1 + s2);
					}
					case EQ: {
						return ValueFac.createValue(Type.BOOLEAN, s1.equals(s2));
					}
					default: {
						throw new ExecutionException("Invalid operation on string", this);
					}
				}
			default:
				throw new ExecutionException("Invalid type in binary operation", this);
		}
	}
}
