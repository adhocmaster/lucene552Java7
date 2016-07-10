package com.tutorialspoint.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	private IndexWriter writer;
	
	private Analyzer analyzer;
	
	private IndexWriterConfig iwc;
	
	public Indexer( String indexDirPath ) throws IOException {
		
		Directory indexDir = FSDirectory.open( Paths.get( indexDirPath ) ); //file replaced by path (nio package)
		
		analyzer = new StandardAnalyzer();
		
		iwc = new IndexWriterConfig( analyzer );
		
		//default configuration for index. This cannot be changed after index is created using this object. For any changes we will need to getConfig from the index writer.
		
		iwc.setOpenMode( OpenMode.CREATE_OR_APPEND ); // only create removes previous index. create or append adds to
		
		iwc.setRAMBufferSizeMB( 256.0 ); //
		
		writer = new IndexWriter( indexDir, iwc );
		
		
	}
	
	public void close() throws IOException {
		
		writer.close();
		
	}
	
	private Document getDocument( Path path ) throws IOException {
		
		Document doc = new Document();
		
		Charset charset = Charset.forName("UTF-8");
		
		Field contentField = new TextField ( LuceneConstants.CONTENTS, Files.newBufferedReader( path, charset ) ); //readers dont store the contents, use for tokenizing and indexing
		
		Field filenameField = new StringField ( LuceneConstants.FILE_NAME, path.getFileName().toString(), Field.Store.YES ); //just store, not indexed

		Field filePathField = new StringField( LuceneConstants.FILE_PATH, path.toString(), Field.Store.YES );
		
//		Field filePathField = new StoredField( LuceneConstants.FILE_PATH, path.toString());

		doc.add( contentField );
		doc.add( filenameField );
		doc.add( filePathField );
		
		return doc;
		
	}
	
	private void indexFile ( Path path ) throws IOException {
		
		System.out.println( "Indexing file " + path.toString());
		
		Document doc = getDocument( path );
		
		if ( writer.getConfig().getOpenMode() == OpenMode.CREATE ) {

			System.out.println( "Adding file " + path.toString());
			
			writer.addDocument(doc);
			
		} else {

			System.out.println( "Updating file " + path.toString());
			
			writer.updateDocument( new Term( LuceneConstants.FILE_PATH, path.toString() ) , doc );
		}
		
		
	}
	
	public int createIndex ( String dataDirPath, FileFilter filter ) {
		
		Path path = Paths.get( dataDirPath );
		
		if( Files.isDirectory( path )) {
			
			//try-with-resource closes autoclosable resources. Don't need finally block to close streams
			
			try ( DirectoryStream<Path> dirStream = Files.newDirectoryStream( path ) ) {
				
				for ( Path file: dirStream) {
					
					if ( ! Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS) 
						
							&& !Files.isHidden(file)
							&& Files.exists(file, LinkOption.NOFOLLOW_LINKS)
							&& Files.isReadable( file )
							&& filter.accept( file.toFile())
							
					) {
						
						indexFile(file);
						
					}
						
					
				}
				
				
			} catch ( IOException | DirectoryIteratorException e) {
				
				System.err.println( e );
				
			}
			
		} else {
			
			System.out.println( "Invalid directory ");
			
		}
		
		return writer.numDocs();
		
	}

}
