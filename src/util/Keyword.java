package util;

import java.util.HashMap;

import lexer.Token.Kind;

public class Keyword {
	public static HashMap<String, Kind> keywords;
	static {
		keywords = new HashMap<String, Kind>();

		keywords.put("boolean", Kind.TOKEN_BOOLEAN);
		keywords.put("char", Kind.TOKEN_CHAR);
		keywords.put("class", Kind.TOKEN_CLASS);
		keywords.put("default", Kind.TOKEN_DEFAULT);
		keywords.put("else", Kind.TOKEN_ELSE);
		keywords.put("extends", Kind.TOKEN_EXTENDS);
		keywords.put("false", Kind.TOKEN_FALSE);
		keywords.put("for", Kind.TOKEN_FOR);
		keywords.put("if", Kind.TOKEN_IF);
		keywords.put("int", Kind.TOKEN_INT);
		keywords.put("length", Kind.TOKEN_LENGTH);
		keywords.put("main", Kind.TOKEN_MAIN);
		keywords.put("new", Kind.TOKEN_NEW);
		keywords.put("null", Kind.TOKEN_NULL);
		keywords.put("out", Kind.TOKEN_OUT);
		keywords.put("println", Kind.TOKEN_PRINTLN);
		keywords.put("protected", Kind.TOKEN_PROTECTED);
		keywords.put("private", Kind.TOKEN_PRIVATE);
		keywords.put("public", Kind.TOKEN_PUBLIC);
		keywords.put("return", Kind.TOKEN_RETURN);
		keywords.put("static", Kind.TOKEN_STATIC);
		keywords.put("String", Kind.TOKEN_STRING);
		keywords.put("System", Kind.TOKEN_SYSTEM);
		keywords.put("this", Kind.TOKEN_THIS);
		keywords.put("true", Kind.TOKEN_TRUE);
		keywords.put("void", Kind.TOKEN_VOID);
		keywords.put("while", Kind.TOKEN_WHILE);
	}
}
