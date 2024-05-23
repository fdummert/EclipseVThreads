# EclipseVThreads
Reproducer for https://github.com/eclipse-jdt/eclipse.jdt.debug/issues/345

Place a breakpoint in Start, line 50 and start debugging the Start class. In a first round, you might want to disable the breakpoint after the first hit to isolate debugging one or two virtual threads, and later more threads simultaneously. Step into the fibonacci function, wait a few seconds and try to slowly step into the recursion. 
You can observe all error behaviors described in the ticket that make stepping further impossible:
* collapse of the stack view of the suspended thread
* suspended thread line isn't displayed any longer
* suspended thread line is displayed as an empty line