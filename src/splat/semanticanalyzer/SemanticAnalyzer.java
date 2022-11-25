package splat.semanticanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.Declaration;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.ParameterDecl;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.VariableDecl;
import splat.parser.elements.constants.types.RetType;
import splat.parser.elements.constants.types.Type;
import splat.parser.elements.statements.ReturnStmt;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, Type> progVarMap;
	
	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
		this.funcMap = new HashMap<String, FunctionDecl>();
		this.progVarMap = new HashMap<String, Type>();
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {	
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}
		
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Perform semantic analysis on the function body
		boolean returnStmtFound = false;
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(funcMap, varAndParamMap);
			if (stmt instanceof ReturnStmt) {
				returnStmtFound = true;
			}
		}

		// Had to modify the predefined function, sorry
		if (!returnStmtFound && !funcDecl.getRetType().equalsTo(RetType.VOID)) {
			throw new SemanticAnalysisException("Function does not return a value", funcDecl);
		}
	}
	
	
	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
		
		Map<String, Type> varAndParamMap = new HashMap<>();

		for (ParameterDecl param : funcDecl.getParams()) {
			varAndParamMap.put(param.getLabel().toString(), param.getType());
		}

		for (VariableDecl decl : funcDecl.getLocVarDecls()) {
			varAndParamMap.put(decl.getLabel().toString(), decl.getType());
		}

		varAndParamMap.put("return", funcDecl.getRetType());

		return varAndParamMap;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<>();

		for (FunctionDecl func : funcMap.values()) {
			if (labels.contains(func.getLabel().toString())) {
				throw new SemanticAnalysisException("Duplicate function label", func);
			}
			labels.add(func.getLabel().toString());
		}

		for (ParameterDecl param : funcDecl.getParams()) {
			if (labels.contains(param.getLabel().toString())) {
				throw new SemanticAnalysisException("Duplicate parameter label: " + param.getLabel(), param);
			}
			labels.add(param.getLabel().toString());
		}

		for (VariableDecl decl : funcDecl.getLocVarDecls()) {
			if (labels.contains(decl.getLabel().toString())) {
				throw new SemanticAnalysisException("Duplicate local variable label: " + decl.getLabel(), decl);
			}
			labels.add(decl.getLabel().toString());
		}
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
