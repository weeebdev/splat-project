package splat.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
	private File progFile;
	private List<Token> tokens = new ArrayList<Token>();
	private int line = 1, col = 0;

	// expandable list of symbolic keywords
	private final List<String> whitelist = Arrays.asList("+", "-", "*", "/", "%", ";", ",", "(", ")",
			"[", "]", "{", "}", "=", "<", ">", ":", "==", "<=", ">=", ":=");
	// symbols which are part of the complex operators, but invalid individually.
	// blackList must be in whitelist
	private final List<String> blacklist = Arrays.asList("=");
	// there is a problem with TAB (\t), because some editors treat them like 4
	// spaces, others as 1 character. For now \t takes 1 column.
	private final List<Character> delimiters = Arrays.asList(' ', '\t', '\n', '\r');

	private String lexeme = "";

	public Lexer(File progFile) {
		this.progFile = progFile;
	}

	// helper function that tokenizes symbols in a greedy approach
	private void tokenizeSymbols() throws LexException {
		// first, identify the longest symbol that is in the whitelist
		whitelist.sort((a, b) -> b.length() - a.length());
		int maxSymbolLen = whitelist.get(0).length();

		main_loop: while (!lexeme.isEmpty()) {
			// then generate substrings from the lexeme (from longest to shortest) and check
			// if they are in the whitelist and not in a blacklist
			for (int i = Math.min(lexeme.length(), maxSymbolLen); i > 0; i--) {
				String substr = lexeme.substring(0, i);
				if (whitelist.contains(substr) && !blacklist.contains(substr)) {
					tokens.add(new Token(substr, line, col - lexeme.length()));
					lexeme = lexeme.substring(i);
					continue main_loop;
				} else if (i == 1) {
					throw new LexException(lexeme, line, col - lexeme.length());
				}
			}
		}
	}

	private Boolean isLabelChar(Character c) {
		return Character.isLetterOrDigit(c) || c == '_';
	}

	private Boolean isSymbolChar(Character c) {
		return whitelist.contains(c.toString());
	}

	public List<Token> tokenize() throws LexException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(this.progFile),
							Charset.forName("UTF-8")));
			int c;
			while ((c = reader.read()) != -1) {
				char character = (char) c;
				col++;

				// if this is a String scope
				if (lexeme.contains("\"")) {
					// then add anything until other quote
					lexeme += character;

					if (character == '\"') {
						tokens.add(new Token(lexeme, line, col));
						lexeme = "";
					}
					// and update col and line if necessary
					if (character == '\n') {
						col = 0;
						line++;
					}

					// skip to the next iteration
					continue;
				}

				if (delimiters.contains(character)) {
					if (!lexeme.isEmpty()) {
						// in case lexeme is not empty and symbolic (hopefully) proceed to
						// tokenizeSymbols
						if (isSymbolChar(lexeme.charAt(lexeme.length() - 1))) {
							this.tokenizeSymbols();
						} else {
							// otherwise just add it (because valid label case is going to be handled later)
							// note, col is already incremented by delimiter, that's why I decrement it
							tokens.add(new Token(lexeme, line, col - 1));
							lexeme = "";
						}
					}

					// if char is delimiter but no cr or whitespace, then update col and line
					if (character == '\n') {
						col = 0;
						line++;
					}

					// skip to the next iteration
					continue;
				}

				// if lexeme is empty then add any valid character
				if (lexeme.isEmpty()) {
					if (isLabelChar(character) || isSymbolChar(character) || character == '\"') {
						lexeme += character;
						continue;
					} else {
						throw new LexException(lexeme + character, line, col);
					}
				}

				if (character == '\"') {
					if (isLabelChar(lexeme.charAt(lexeme.length() - 1))) {
						tokens.add(new Token(lexeme, line, col - 1));
					} else {
						this.tokenizeSymbols();
					}
					lexeme = "\"";
					continue;
				}

				// if current character is a valid label character, then
				if (isLabelChar(character)) {
					// if lexeme is a label, then
					if (isLabelChar(lexeme.charAt(lexeme.length() - 1))) {
						// check for label validity (not starting with a digit)
						if (Character.isLetter(lexeme.charAt(0)) || lexeme.charAt(0) == '_') {
							lexeme += character;
						}
						// if label starts with a digit, then it's just a number
						else if (Character.isDigit(lexeme.charAt(0)) && Character.isDigit(character)) {
							lexeme += character;
						} else {
							throw new LexException(lexeme, line, col);
						}
					} else {
						// otherwise mean that the current lexeme is full of symbols, proceed to
						// tokenize them
						this.tokenizeSymbols();
						lexeme = "" + character;
					}
				}
				// if current character is non label, i.e. symbolic
				else {
					// if lexeme is a label, then add it
					if (isLabelChar(lexeme.charAt(lexeme.length() - 1))) {
						tokens.add(new Token(lexeme, line, col - 1));
						lexeme = "";
					}

					// otherwise check for symbol validity
					if (isSymbolChar(character)) {
						lexeme += character;
					} else {
						throw new LexException(lexeme + character, line, col);
					}

				}
			}

			// if after all the processes lexeme is not empty, then
			if (!lexeme.isEmpty()) {
				// check if it has an unclosed quote
				if (lexeme.contains("\"")) {
					throw new LexException(lexeme, line, col);
				} else {
					// otherwise just add it
					if (isSymbolChar(lexeme.charAt(lexeme.length() - 1))) {
						this.tokenizeSymbols();
					} else {
						tokens.add(new Token(lexeme, line, col));
					}
				}
			}

			return tokens;
		} catch (IOException e) {
			throw new LexException("File not found: " + this.progFile, -1, -1);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new LexException("Could not close the file: " + this.progFile, -1, -1);
				}
			}
		}
	}

}
