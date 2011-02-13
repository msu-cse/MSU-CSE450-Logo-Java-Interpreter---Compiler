# [CSE450 Spring 2011 Project](https://github.com/msu-cse450-ss2010/project)

Project Members

- Brandon Overall
- Kole Reece
- Zach Riggle

# Project Definitions

Please see the [wiki](msu-cse450-ss11/wiki).

# Usage

*Note to the TA: The source is in the `edu.msu.cse.cse450` package.  Also, if you want to see how we build everything, please download a [zipball][zipball] of the project.*

-   Build everything: `ant` *(or build from within Eclipse with the Ant pane)*
-   Run the grammar: 
    
        ant run < test/complex_test.txt
    *(or `java -jar build/grammar.jar < test/complex_test.txt`)*
    
    *(or `java -cp build/grammar.jar edu.msu.cse.cse450.InterpLogoAST2 < test/complex_test.txt`)*
-   Run the grammar, ***and*** verify against expected results:

        diff test/test_X_results.txt <(java -jar build/grammar.jar < test/X_test.txt) && echo "OK"
        
[zipball]: https://github.com/msu-cse450-ss2010/project/zipball/master