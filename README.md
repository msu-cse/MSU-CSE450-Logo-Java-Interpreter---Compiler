# [CSE450 Spring 2011 Project](https://github.com/msu-cse450-ss2010/project)

Project Members

- Brandon Overall
- Kole Reece
- Zach Riggle

# Group Member Contributions

- Zach contributed the most.  He set up the repository that the group used to share files and work on the project remotely.  He also solved a lot of problems with the grammar.  
- Brandon and Kole contributed their share of the work.  Both members showed up to the group meeting where we set up the repository and created a rough draft of the grammar and test file.  

All three group members communicated well and did at least all that was asked of them.  There were no issues with any of the group members.

# Project Definitions

Please see the [wiki](msu-cse450-ss11/wiki).

# Usage

*Note to the TA: The source is in the `edu.msu.cse.cse450` package.  Also, if you want to see how we build everything, please download a [zipball][zipball] of the project.*

-   Build everything: `ant` *(or build from within Eclipse with the Ant pane)*
-   Run the grammar: 
    
        ant run < test/complex_test.txt
    *(or `java -jar build/grammar.jar < test/complex_test.txt`)*
-   Run the grammar, ***and*** verify against expected results:

        diff test/test_X_results.txt <(java -jar build/grammar.jar < test/X_test.txt) && echo "OK"
        
[zipball]: https://github.com/msu-cse450-ss2010/project/zipball/master

