import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.stringtemplate.StringTemplate;

/**
 * Writes and opens dotfiles.
 * 
 * @author zach
 * 
 */
public class DotfileUtil {

	public static String writeDotfile(Object tree, TreeAdaptor adaptor) {
		DOTTreeGenerator dtg = new DOTTreeGenerator();
		
		StringTemplate st = dtg.toDOT(tree,adaptor);

		try {
			File tempfile = File.createTempFile("antlr", ".dot");
			FileWriter f = new FileWriter(tempfile);
			f.write(st.toString());
			f.close();
			return tempfile.getAbsolutePath();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void openDotfile(String filename) {
		if(filename == null) return;
		
		try {
			File tempPng = File.createTempFile("antlr", ".svg");
			String tempPngPath = tempPng.getAbsolutePath();
			String[] dotCommand = new String[] { "dot", "-T", "svg", "-o",
					tempPngPath, filename };
			String[] openCommand = new String[] { "open", tempPngPath };

			Process dot = Runtime.getRuntime().exec(dotCommand);
			dot.waitFor();
			Process open = Runtime.getRuntime().exec(openCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
