package com.tutorialspoint.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	
	IndexSearcher indexSearcher;
	
	IndexReader reader;
	
	QueryParser queryParser;
	
	Query query;
	
	public Searcher ( String indexDirPath ) throws IOException {
		
		Directory indexDir = FSDirectory.open( Paths.get( indexDirPath ) );
		
		reader = DirectoryReader.open( indexDir );
		
		indexSearcher = new IndexSearcher( reader );
		
		queryParser = new QueryParser( LuceneConstants.CONTENTS, new StandardAnalyzer() );
		
	}
	
	public TopDocs search ( String searchQuery ) throws ParseException, IOException {
		
		query = queryParser.parse( searchQuery );
		
		return indexSearcher.search( query, LuceneConstants.MAX_SEARCH );
		
	}
	
	public Document getDocument ( ScoreDoc scoreDoc ) throws IOException {
		
		return indexSearcher.doc( scoreDoc.doc );
		
	}
	
	public void close() throws IOException {
		
		reader.close();
	    
	}

}
