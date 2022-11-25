package splat.parser.elements.expressions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueFac;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.VariableDecl;
import splat.parser.elements.constants.types.Type;
import splat.semanticanalyzer.SemanticAnalysisException;
import utils.Utils;

public class CallExpr extends Expression {
	private LabelExpr label;
	private List<Expression> args;

	public CallExpr(Token tok, LabelExpr label, List<Expression> args) {
		super(tok);
		this.label = label;
		this.args = args;
	}

	public LabelExpr getLabel() {
		return label;
	}

	public List<Expression> getArgs() {
		return args;
	}

	public String toString() {
		return String.format("%s(%s)", label, Utils.formatArray(args, ", "));
	}

	@Override
	public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		FunctionDecl func = funcMap.get(label.getTok().getValue());
		if (func == null) {
			throw new SemanticAnalysisException("Function " + label.getTok().getValue() + " is not defined", this);
		}

		if (func.getParams().size() != args.size()) {
			throw new SemanticAnalysisException(
					"Function " + label.getTok().getValue() + " expects " + func.getParams().size()
							+ " arguments, but " + args.size() + " were given",
					this);
		}

		for (int i = 0; i < args.size(); i++) {
			Type argType = args.get(i).analyzeAndGetType(funcMap, varAndParamMap);
			Type paramType = func.getParams().get(i).getType();
			if (!argType.equalsTo(paramType)) {
				throw new SemanticAnalysisException(
						"Function " + label.getTok().getValue() + " expects argument " + (i + 1) + " to be of type "
								+ paramType + ", but " + argType + " was given",
						this);
			}
		}

		return func.getRetType();
	}

	@Override
	public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ExecutionException {
		FunctionDecl func = funcMap.get(label.getTok().getValue());
		Map<String, Value> newVarAndParamMap = new HashMap<String, Value>();

		for (VariableDecl varDecl : func.getLocVarDecls()) {
			newVarAndParamMap.put(varDecl.getLabel().getTok().getValue(), ValueFac.createValue(varDecl.getType()));
		}

		for (int i = 0; i < args.size(); i++) {
			newVarAndParamMap.put(func.getParams().get(i).getLabel().getTok().getValue(),
					args.get(i).evaluate(funcMap, varAndParamMap));
		}

		for (Statement stmt : func.getStmts()) {
			try {
				stmt.execute(funcMap, newVarAndParamMap);
			} catch (ReturnFromCall e) {
				// TODO Auto-generated catch block
				return e.getReturnVal();
			}
		}

		return null;
	}

}
