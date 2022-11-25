package splat.parser.elements.statements;

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
import splat.parser.elements.expressions.LabelExpr;
import splat.semanticanalyzer.SemanticAnalysisException;
import utils.Utils;

public class CallStmt extends Statement {
	private LabelExpr label;
	private List<Expression> args;

	public CallStmt(Token tok, LabelExpr label, List<Expression> args) {
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
		return String.format("%s(%s);", label, Utils.formatArray(args, ", "));
	}

	@Override
	public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
			throws SemanticAnalysisException {
		FunctionDecl func = funcMap.get(label.getTok().getValue());
		if (func == null) {
			throw new SemanticAnalysisException(String.format("Function %s is not declared", label.getTok().getValue()),
					label.getTok().getLine(), label.getTok().getColumn());
		}

		if (func.getParams().size() != args.size()) {
			throw new SemanticAnalysisException(
					String.format("Function %s expects %d arguments, but %d were given", label.getTok().getValue(),
							func.getParams().size(), args.size()),
					label.getTok().getLine(), label.getTok().getColumn());
		}

		for (int i = 0; i < args.size(); i++) {
			Type argType = args.get(i).analyzeAndGetType(funcMap, varAndParamMap);
			Type paramType = func.getParams().get(i).getType();
			if (!argType.equalsTo(paramType)) {
				throw new SemanticAnalysisException(
						String.format("Function %s expects argument %d to be of type %s, but %s was given",
								label.getTok().getValue(), i + 1, paramType, argType),
						label.getTok().getLine(), label.getTok().getColumn());
			}
		}
	}

	@Override
	public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap)
			throws ReturnFromCall, ExecutionException {
		FunctionDecl func = funcMap.get(label.getTok().getValue());

		Map<String, Value> newVarAndParamMap = new HashMap<String, Value>();

		for (VariableDecl varDecl : func.getLocVarDecls()) {
			newVarAndParamMap.put(varDecl.getLabel().getTok().getValue(), ValueFac.createValue(varDecl.getType()));
		}

		for (int i = 0; i < args.size(); i++) {
			Value val = args.get(i).evaluate(funcMap, varAndParamMap);
			newVarAndParamMap.put(func.getParams().get(i).getLabel().getTok().getValue(), val);
		}

		for (Statement stmt : func.getStmts()) {
			try {
				stmt.execute(funcMap, newVarAndParamMap);
			} catch (ReturnFromCall e) {
				break;
			}
		}
	}

}
