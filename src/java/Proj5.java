import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class Proj5 {

	public static void main(String[] args) throws RecognitionException,
			IOException {
		// try {
		new Proj5(args);
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.exit(1);
		// }
	}

	LogoASTAdaptor adaptor = new LogoASTAdaptor();
	ANTLRStringStream input;
	LogoJVM1Lexer lexer;
	Logger log = Logger.getLogger("Proj5");
	CommonTreeNodeStream nodes;
	LogoJVM1Parser parser;
	LogoJVM1Parser.program_return parseReturn;
	StringTemplateGroup templates;
	TokenRewriteStream tokens;
	ScopedTree tree;
	LogoTree walker;
	StringTemplate walkerOutput;
	LogoTree.program_return walkerReturn;

	Proj5(String[] args) throws RecognitionException, IOException {
		// -- Logging
		LogoLogger.configure();

		// -- Get the input
		if (args.length > 0)
			input = new ANTLRStringStream(args[0]);
		else
			input = new ANTLRInputStream(System.in);

		lexer = new LogoJVM1Lexer(input);
		tokens = new TokenRewriteStream(lexer);
		parser = new LogoJVM1Parser(tokens);
		parser.setTreeAdaptor(adaptor);

		// -- Parse the input
		parseReturn = parser.program();

		// -- Create a memory space for the global scope
		parseReturn.tree.memorySpace = new HashMap<String, Symbol>();

		// -- Output the whole tree
		log.info(parseReturn.tree.toStringTree());

		// -- Dotfile of the tree
		if (LogoProperties.GENERATE_DOTFILE) {
			String dotfile = DotfileUtil
					.writeDotfile(parseReturn.tree, adaptor);
			DotfileUtil.openDotfile(dotfile);
		}

		// -- Interpret
		if (LogoProperties.INTERPRET_INPUT) {
			new Interpreter(parseReturn.tree);
		}

		// -- Generate bytecode
		if (LogoProperties.GENERATE_BYTECODE) {
			// -- Load the string templates
			templates = loadStringTemplates(LogoProperties.STRING_TEMPLATES_FILE);

			nodes = new CommonTreeNodeStream(adaptor, parseReturn.tree);
			nodes.setTokenStream(tokens);
			walker = new LogoTree(nodes);
			walker.file = null;
			walker.setTemplateLib(templates);
			walkerReturn = walker.program();
			walkerOutput = walkerReturn.st;
			System.out.println(walkerOutput);
		}

	}

	StringTemplateGroup loadStringTemplates(String filename)
			throws FileNotFoundException {
		log.info("Loading string templates from " + filename);
		return new StringTemplateGroup(new FileReader(filename));
	}
}
