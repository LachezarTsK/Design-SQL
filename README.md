# Design-SQL
Challenge at LeetCode.com. Tags: Design, Hash Table, String, Sorting.

-----------------------------------------------------------------------------------------------------------------------------------------------

One of the methods given for implementation by Leetcode, namely “exp(String name)” - standing for export table with the requested name (this not very descriptive name is given by Leetcode, so it cannot be changed) - requires the return of the table with rows sorted by rowIDs. This influences the two approaches to solving<br/> the problem, presented here: 

- mapping the rowIDs to columns in a Hash Map and sorting the rows when this method is called.
- mapping the rowIDs to columns in a Sorted Map, so there would be no need to sort the rows when<br/> this method is called.


The solutions with Sorting are implemented in Java, JavaScript, TypeScript, C++, C#, Kotlin and Go.

The solutions with Sorted Map are implemented in Java, C++ and Kotlin. As of September 2025,<br/> of the programming languages I know, only these three languages have an inbuilt Sorted Map.

---------------------------------------------------------------------------------------------

**Additional comments!**

**While solving the problem, I did not notice that when method “exp(String name)” is called, there is no requirement to return the table with rows sorted in increasing order by rowIDs. So, in this case, a simple Hash Map without additional sorting is enough.** 

**Anyway, I leave these two alternative approaches, for the imaginary scenario that there is such a requirement. It works both for sorted and unsorted scenario, though for the latter, it is a little bit slower than a Hash Map without sorting, due to the additional cost of sorting the keys of the Hash Map or using a Sorted Map.**
