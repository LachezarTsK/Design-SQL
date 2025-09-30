# Design-SQL
Challenge at LeetCode.com. Tags: Design, Hash Table, String, Sorting.

-----------------------------------------------------------------------------------------------------------------------------------------------

One of the methods given by for implementation by Leetcode, namely “exp(String name)” - standing for export table with the requested name (this not very descriptive name is given by Leetcode, so it cannot be changed) - requires the return of the table with rows sorted by rowIDs. This influence the two approaches to solving<br/> the problem, presented here: 

- mapping the rowIDs to columns in a Hash Map and sorting the rows when this method is called.<br/>
- mapping the rowIDs to columns in a Sorted Map, so there would be no need to sort the rows when the method is called.


The solutions with Sorting are implemented in Java, JavaScript, TypeScript, C++, C#, Kotlin, Go.

The solutions with Sorted Map are implemented in Java, C++, Kotlin. As of September 2025, from the programming languages I know, only these three languages have an inbuilt Sorted Map.

