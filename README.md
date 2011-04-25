# CSE450 Spring 2011 Project

## Usage

-   Build everything: `ant` *(or build from within Eclipse with the Ant pane)*
-   Run the grammar: 
     - `java -jar build/grammar.jar < test/complex_test.txt`
     - `java -cp build/grammar.jar InterpLogoAST2 < test/complex_test.txt`
     - `ant run < test/complex_test.txt`
    - `./run-grammar.sh < test/complex.txt`

## Arguments

By default, this will generate Java assembly suitable for passing to Jasmin.  See `logo.properties` to control the execution parameters.

- interpret - Interpret the results and execute them directly.
- stringTemplateFile - Path to the `.stg` file used for generating Jasmin bytecode
- generateBytecode - Parse the parser tree, and generate Jasmin bytecode
- generateDotFile - Generate a DOT file of the tree in a temporary directory, and generates a PNG file.  On Mac OS X, also opens this file automatically
- enableLogging - Enable verbose logging
