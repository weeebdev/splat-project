package splat.parser.elements.expressions;

import java.util.Map;

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
}
