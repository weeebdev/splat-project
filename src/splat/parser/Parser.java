package splat.parser;

import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.constants.keyword.IKeyword;
import splat.parser.elements.constants.keyword.Keyword;
import splat.parser.elements.constants.operators.BinaryOperator;
import splat.parser.elements.constants.operators.UnaryOperator;
import splat.parser.elements.constants.types.RetType;
import splat.parser.elements.constants.types.Type;
import splat.parser.elements.expressions.BinaryOpExpr;
import splat.parser.elements.expressions.CallExpr;
import splat.parser.elements.expressions.LabelExpr;
import splat.parser.elements.expressions.UnaryOpExp;
import splat.parser.elements.literals.Literal;
import splat.parser.elements.literals.LiteralFac;
import splat.parser.elements.statements.AssignmentStmt;
import splat.parser.elements.statements.CallStmt;
import splat.parser.elements.statements.IfStmt;
import splat.parser.elements.statements.PrintStmt;
import splat.parser.elements.statements.PrintLineStmt;
import splat.parser.elements.statements.ReturnStmt;
import splat.parser.elements.statements.WhileStmt;

public class Parser {

	private List<Token> tokens;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}

	
	/**
	 * Overloaded method to be able to expect an array of values
	 * 
	 * @param expected array of expected values
	 * @return
	 */
	private boolean peekNext(String[] expected) {
		for (String s : expected) {
			if (peekNext(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Overloaded method to be able to expect a keyword
	 * 
	 * @param keyword
	 * @throws ParseException
	 */
	private void checkNext(IKeyword keyword) throws ParseException {
		checkNext(keyword.toString());
	}

	/**
	 * Overloaded method to be able to expect a keyword
	 * 
	 * @param keyword
	 * @return
	 */
	private boolean peekTwoAhead(IKeyword keyword) {
		return peekTwoAhead(keyword.toString());
	}

	/**
	 * Overloaded method to be able to expect a keyword
	 * 
	 * @param keyword
	 * @return
	 */
	private boolean peekNext(IKeyword keyword) {
		return peekNext(keyword.toString());
	}

	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			
			checkNext("program");
			
			List<Declaration> decls = parseDecls();
			
			checkNext("begin");
			
			List<Statement> stmts = parseStmts();
			
			checkNext("end");
			checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		LabelExpr label = parseLabel();

		List<ParameterDecl> params = parseParamDecls();

		checkNext(Keyword.COLON);
		RetType retType = this.parseRetType();
		checkNext(Keyword.IS);

		List<VariableDecl> locVarDecls = parseVarDecls();

		checkNext(Keyword.BEGIN);
		List<Statement> stmts = parseStmts();
		checkNext(Keyword.END);
		checkNext(Keyword.SEMICOLON);

		return new FunctionDecl(label.getTok(), label, params, retType, locVarDecls, stmts);
	}

	/*
	 * <var-decls> ::= ( <var-decl> )*
	 */
	private List<VariableDecl> parseVarDecls() throws ParseException {
		List<VariableDecl> varDecls = new ArrayList<>();
		while (peekTwoAhead(Keyword.COLON)) {
			varDecls.add(parseVarDecl());
		}
		return varDecls;
	}

	/*
	 * <params> ::= <param> ( , <param> )*
	 */
	private List<ParameterDecl> parseParamDecls() throws ParseException {
		List<ParameterDecl> params = new ArrayList<>();
		checkNext(Keyword.OPEN_PAREN);
		while (!peekNext(Keyword.CLOSE_PAREN)) {
			params.add(parseParamDecl());
			if (peekNext(Keyword.COMMA)) {
				checkNext(Keyword.COMMA);
			} else {
				break;
			}
		}
		checkNext(Keyword.CLOSE_PAREN);

		return params;
	}

	/*
	 * <param> ::= <label> : <type>
	 */
	private ParameterDecl parseParamDecl() throws ParseException {
		LabelExpr label = parseLabel();
		checkNext(Keyword.COLON);
		Type type = parseType();

		return new ParameterDecl(label.getTok(), label, type);
	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		LabelExpr label = parseLabel();
		checkNext(Keyword.COLON);
		Type type = this.parseType();
		checkNext(Keyword.SEMICOLON);

		return new VariableDecl(label.getTok(), label, type);
	}

	/*
	 * <stmts> ::= ( <stmt> )*
	 */
	private List<Statement> parseStmts(String... stopWord) throws ParseException {
		List<Statement> stmts = new ArrayList<Statement>();

		while (true) {
			if (peekNext(Keyword.END) || (stopWord.length != 0 && peekNext(stopWord))) {
				break;
			}
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}
		return stmts;
	}

	/**
	 * A "Factory" method to return all types of statements
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Statement parseStmt() throws ParseException {
		if (peekTwoAhead(Keyword.ASSIGN)) {
			return parseAssignStmt();
		} else if (peekNext(Keyword.WHILE)) {
			return parseWhileStmt();
		} else if (peekNext(Keyword.IF)) {
			return parseIfStmt();
		} else if (peekNext(Keyword.RETURN)) {
			return parseReturnStmt();
		} else if (peekNext(Keyword.PRINT)) {
			return parsePrintStmt();
		} else if (peekNext(Keyword.PRINT_LINE)) {
			return parsePrintLineStmt();
		} else if (peekTwoAhead(Keyword.OPEN_PAREN)) {
			return parseCallStmt();
		} else {
			throw new ParseException("Statement expected", tokens.get(0));
		}
	}

	/*
	 * <call-stmt> ::= <label> ( <args> ) ;
	 */
	private Statement parseCallStmt() throws ParseException {
		LabelExpr label = parseLabel();
		List<Expression> args = parseArgs();
		checkNext(Keyword.SEMICOLON);
		return new CallStmt(label.getTok(), label, args);
	}

	/*
	 * <assign-stmt> ::= <label> := <expr> ;
	 */
	private AssignmentStmt parseAssignStmt() throws ParseException {
		LabelExpr label = parseLabel();
		checkNext(Keyword.ASSIGN);
		Expression expr = parseExpr();
		checkNext(Keyword.SEMICOLON);
		return new AssignmentStmt(label.getTok(), label, expr);
	}

	/*
	 * <while-stmt> ::= while <expr> do <stmts> end while ;
	 */
	private Statement parseWhileStmt() throws ParseException {
		Token whileTok = tokens.remove(0);
		Expression expr = parseExpr();
		checkNext(Keyword.DO);
		List<Statement> stmts = parseStmts();
		checkNext(Keyword.END);
		checkNext(Keyword.WHILE);
		checkNext(Keyword.SEMICOLON);
		return new WhileStmt(whileTok, expr, stmts);
	}

	/*
	 * <if-stmt> ::= if <expr> then <stmts> (else <stmts>)? end if ;
	 */
	private Statement parseIfStmt() throws ParseException {
		Token ifTok = tokens.remove(0);
		Expression expr = parseExpr();
		checkNext(Keyword.THEN);
		List<Statement> thenStmts = parseStmts(Keyword.ELSE.toString());
		List<Statement> elseStmts = null;
		if (peekNext(Keyword.ELSE)) {
			checkNext(Keyword.ELSE);
			elseStmts = parseStmts();
		}
		checkNext(Keyword.END);
		checkNext(Keyword.IF);
		checkNext(Keyword.SEMICOLON);
		return new IfStmt(ifTok, expr, thenStmts, elseStmts);
	}

	/*
	 * <return-stmt> ::= return (<expr>)? ;
	 */
	private Statement parseReturnStmt() throws ParseException {
		Token tok = tokens.remove(0);
		Expression expr = null;
		if (!peekNext(Keyword.SEMICOLON)) {
			expr = parseExpr();
		}
		checkNext(Keyword.SEMICOLON);
		return new ReturnStmt(tok, expr);
	}

	/*
	 * <print-stmt> ::= print <expr> ;
	 */
	private Statement parsePrintStmt() throws ParseException {
		Token tok = tokens.remove(0);
		Expression expr = parseExpr();
		checkNext(Keyword.SEMICOLON);
		return new PrintStmt(tok, expr);
	}

	/*
	 * <print-line-stmt> ::= print_line ;
	 */
	private Statement parsePrintLineStmt() throws ParseException {
		Token tok = tokens.remove(0);
		checkNext(Keyword.SEMICOLON);
		return new PrintLineStmt(tok);
	}

	/**
	 * A "Factory" method for parsing expressions
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Expression parseExpr() throws ParseException {
		if (peekNext(Keyword.OPEN_PAREN)) {
			if (peekTwoAhead(UnaryOperator.MINUS) || peekTwoAhead(UnaryOperator.NOT)) {
				return parseUnaryOpExpr();
			} else {
				return parseBinaryOpExpr();
			}
		} else if (peekTwoAhead(Keyword.OPEN_PAREN)) {
			return parseCallExpr();
		} else if (LabelExpr.isValid(tokens.get(0).getValue())) {
			return parseLabel();
		} else {
			return parseLiteral();
		}
	}

	/*
	 * <label-expr> ::= …sequence of alphanumeric characters and underscore, not
	 * starting with a digit... ;
	 */
	private LabelExpr parseLabel() throws ParseException {
		Token tok = tokens.remove(0);
		if (!LabelExpr.isValid(tok.getValue())) {
			throw new ParseException("Invalid label: " + tok.getValue(), tok);
		}
		return new LabelExpr(tok);
	}

	/*
	 * <call-stmt> ::= <label> ( <args> ) ;;
	 */
	private Expression parseCallExpr() throws ParseException {
		LabelExpr label = parseLabel();
		List<Expression> args = parseArgs();
		return new CallExpr(label.getTok(), label, args);
	}

	/*
	 * <args> ::= <expr> ( , <expr> )* ;
	 */
	private List<Expression> parseArgs() throws ParseException {
		checkNext(Keyword.OPEN_PAREN);
		List<Expression> args = new ArrayList<>();
		while (!peekNext(Keyword.CLOSE_PAREN)) {
			args.add(parseExpr());
			if (peekNext(Keyword.COMMA)) {
				checkNext(Keyword.COMMA);
			} else {
				break;
			}
		}
		checkNext(Keyword.CLOSE_PAREN);
		return args;
	}

	/*
	 * <paren-expr> ::=( <expr> <bin-op> <expr> )
	 */
	private Expression parseBinaryOpExpr() throws ParseException {
		Token tok = tokens.remove(0);
		Expression left = parseExpr();
		BinaryOperator op = parseBinOp();
		Expression right = parseExpr();
		checkNext(Keyword.CLOSE_PAREN);
		return new BinaryOpExpr(tok, left, right, op);
	}

	/*
	 * <bin-op> ::= and | or | > | < | == | >= | <= | + | - | * | / | %
	 */
	private BinaryOperator parseBinOp() throws ParseException {
		if (peekNext(BinaryOperator.getValues())) {
			String value = tokens.remove(0).getValue();
			return BinaryOperator.getEnum(value);
		} else {
			throw new ParseException("Binary operator expected", tokens.get(0));
		}
	}

	/*
	 * <unary-op-expr> ::= ( <unary-op> <expr> )
	 */
	private Expression parseUnaryOpExpr() throws ParseException {
		Token tok = tokens.remove(0);
		UnaryOperator op = parseUnaryOp();
		Expression expr = parseExpr();
		checkNext(Keyword.CLOSE_PAREN);
		return new UnaryOpExp(tok, expr, op);
	}

	/*
	 * <unary-op> ::= - | not
	 */
	private UnaryOperator parseUnaryOp() throws ParseException {
		if (peekNext(UnaryOperator.getValues())) {
			String value = tokens.remove(0).getValue();
			return UnaryOperator.getEnum(value);
		} else {
			throw new ParseException("Unary operator expected", tokens.get(0));
		}
	}

	/*
	 * <type> ::= Integer | Boolean | String
	 */
	private Type parseType() throws ParseException {
		if (this.peekNext(Type.getValues())) {
			String value = this.tokens.remove(0).getValue();
			return Type.getEnum(value);
		} else {
			throw new ParseException("Type expected", tokens.get(0));
		}
	}

	/*
	 * <ret-type> ::= type | void
	 */
	private RetType parseRetType() throws ParseException {
		if (this.peekNext(RetType.getValues())) {
			String value = this.tokens.remove(0).getValue();
			return RetType.getEnum(value);
		} else {
			throw new ParseException("Type expected", tokens.get(0));
		}
	}

	/*
	 * <literal> ::= <int-literal> | <bool-literal> | <string-literal>
	 */
	private Literal parseLiteral() throws ParseException {
		Literal lit = LiteralFac.createLiteral(tokens.remove(0));
		if (lit == null) {
			throw new ParseException("Literal expected", tokens.get(0));
		}
		return lit;
	}
}
