package com.tutorialspoint.lucene;

import java.io.FileFilter;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

   String indexDir = "E:\\Lucene\\Index";
   String dataDir = "E:\\Lucene\\Data"; //data.zip can be found in Data folder
   
   Indexer indexer;
   Searcher searcher;
   
   public static void main( String[] args ) {
	   
	   LuceneTester tester;
	   
	   try {
		   
		   tester = new LuceneTester();
		   
		   tester.createIndex();
		   
		   tester.search( "Mohan" );
		   
	   } catch ( Exception e ) {
		   
		   System.err.println( e.getMessage() );
		   e.printStackTrace();
		   
	   }
	   
   }
   
   private void createIndex() throws IOException {
	   
	   indexer = new Indexer( indexDir );
	   
	   int numIndexed;
	   
	   long startTime = System.currentTimeMillis();
	   
	   numIndexed = indexer.createIndex( dataDir, (FileFilter) new TextFileFilter() );
	   
	   long endTime = System.currentTimeMillis();
	   
	   indexer.close();
	   
	   System.out.println(numIndexed+" File indexed, time taken: "
		         +(endTime-startTime)+" ms");	
	   
   }
   
   public void search ( String searchQuery ) throws IOException, ParseException {
	   
	   searcher = new Searcher( indexDir );

	   long startTime = System.currentTimeMillis();
	   
	   TopDocs hits = searcher.search( searchQuery );
	   
	   long endTime = System.currentTimeMillis();
	   
	   System.out.println( hits.totalHits + " docs found in " + ( endTime + startTime ) + "ms" );
	   
	   for ( ScoreDoc scoreDoc: hits.scoreDocs ) {
		   
		   Document doc = searcher.getDocument( scoreDoc );
		   
		   System.out.println( " Doc in memory " + doc.toString() );
		   System.out.println("Score doc " + scoreDoc.toString() );
		   System.out.println( "file path in disk " + doc.get( LuceneConstants.FILE_PATH ) );
		   
	   }
	   
	   searcher.close();
	   
   }
	   
}
