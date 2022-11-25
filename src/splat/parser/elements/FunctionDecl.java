package splat.parser.elements;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.constants.types.RetType;
import splat.parser.elements.expressions.LabelExpr;
import utils.Utils;

/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
public class FunctionDecl extends Declaration {

	private List<ParameterDecl> params;
	private RetType retType;
	private List<VariableDecl> locVarDecls;
	private List<Statement> stmts;

	public FunctionDecl(Token tok,
			LabelExpr label, List<ParameterDecl> params,
			RetType retType,
			List<VariableDecl> locVarDecls, List<Statement> stmts) {
		super(tok);
		super.setLabel(label);
		this.params = params;
		this.retType = retType;
		this.locVarDecls = locVarDecls;
		this.stmts = stmts;
	}

	public List<ParameterDecl> getParams() {
		return params;
	}

	public RetType getRetType() {
		return retType;
	}

	public List<VariableDecl> getLocVarDecls() {
		return locVarDecls;
	}

	public List<Statement> getStmts() {
		return stmts;
	}

	public String toString() {
		String result = super.getLabel() + " (";
		result += Utils.formatArray(params, ", ");
		result += ") : " + retType + " is\n";

		if (locVarDecls.size() != 0) {
			result += Utils.TAB + Utils.TAB + Utils.formatArray(locVarDecls, "\n" + Utils.TAB
					+ Utils.TAB) + "\n";
		}

		result += Utils.TAB + "begin\n";
		result += Utils.TAB + Utils.TAB + Utils.formatArray(stmts, "\n" + Utils.TAB
				+ Utils.TAB) + "\n";
		result += Utils.TAB + "end;\n";

		return result;
	}
}
