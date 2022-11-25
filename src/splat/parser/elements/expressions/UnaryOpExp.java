package splat.parser.elements.expressions;

import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.executor.values.ValueFac;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.constants.operators.UnaryOperator;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

public class UnaryOpExp extends Expression {
	private Expression expr;
	private UnaryOperator op;

	public UnaryOpExp(Token tok, Expression expr, UnaryOperator op) {
		super(tok);
		this.expr = expr;
		this.op = op;
	}

	public Expression getExpr() {
		return expr;
	}

	public UnaryOperator getOp() {
		return op;
	}

	public String toString() {
		return String.format("(%s %s)", op, expr);
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		Type type = expr.analyzeAndGetType(funcMap, varAndParamMap);

		if (op == UnaryOperator.NOT) {
			if (type.equalsTo(Type.BOOLEAN)) {
				return new Type(Type.BOOLEAN);
			} else {
				throw new SemanticAnalysisException("Unary operator 'not' can only be applied to boolean expressions",
						this);
			}
		}

		if (op == UnaryOperator.MINUS) {
			if (type.equalsTo(Type.INTEGER)) {
				return new Type(Type.INTEGER);
			} else {
				throw new SemanticAnalysisException("Unary operator '-' can only be applied to integer expressions",
						this);
			}
		}

		return null;
	}

	@Override
	public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ExecutionException {
		Value val = expr.evaluate(funcMap, varAndParamMap);

		switch (val.getType().getType()) {
			case Type.BOOLEAN: {
				boolean b = (boolean) val.getValue();
				return ValueFac.createValue(Type.BOOLEAN, !b);
			}
			case Type.INTEGER: {
				int i = (int) val.getValue();
				return ValueFac.createValue(Type.INTEGER, -i);
			}
			default:
				throw new ExecutionException("Invalid type for unary operator", this);
		}
	}
}
