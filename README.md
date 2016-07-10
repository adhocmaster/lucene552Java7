Lucene 5.5.2 has java 7 support. I learn a few basics of Apache Lucene engine today and rewrote the sample application found on tutorialspoint to make it work with lucene 5.5.2

Original tutorial can be found here: http://www.tutorialspoint.com/lucene/lucene_first_application.htm

##Build requirements

You need to add 3 libraries to the buildpath. Download and extract lucene 5.5.2 and add these to your project buildpath:

*lucene-5.5.2/core/lucene-core-5.5.2.jar*
*lucene-5.5.2/analysis/common/lucene-analyzers-common-5.5.2.jar*
*lucene-5.5.2/queryparser/lucene-queryparser-5.5.2.jar*

##Necessary changes:

1. IndexReader needed in IndexSearcher object now. (Searcher.java)
2. Removed deprecated field constructors in Indexer.java and used new child classes of Field's.

##Unnecessary changes:

1. Used nio package instead of io package for file manipulation.