/*
 * Copyright (c) 2017 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 *      - Initial concept and initial implementation
 */
package coyote.marshaler;

import java.io.IOException;

import coyote.dataframe.DataField;
import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.ParseException;


/**
 *
 * TODO: try to encode the retrievalSpec to work in SHoT
 */
public class IapRequestMarshaler {

  public static DataFrame marshal( String text ) throws ParseException, IOException {
    DataFrame retval = null;
    retval = new IapRequestParser( text ).setLineDelimitsFields( true ).parse();
    return retval;
  }




  public static String marshal( DataFrame frame ) {
    StringBuffer b = new StringBuffer();
    for ( DataField field : frame.getFields() ) {
      b.append( marshal( field, 0, 2 ) );
      b.append( '\n' );
    }
    return b.toString();
  }




  private static String marshal( DataField field, int level, int indent ) {
    StringBuffer b = new StringBuffer();
    if ( level >= 0 ) {
      b.append( getIndent( level, indent ) );
    }

    b.append( field.getName() );
    b.append( ' ' );

    if ( field.isFrame() ) {
      b.append( marshal( (DataFrame)field.getObjectValue(), level, indent ) );
    } else {
      String name = field.getName().toLowerCase();
      if ( !name.equals( "retrievalspec" ) ) {
        if ( name.endsWith( "name" ) || name.endsWith( "identifier" ) || name.equals( "representation" ) || name.equals( "queryId" ) ) {
          b.append( '"' );
          b.append( field.getStringValue() );
          b.append( '"' );
        } else {
          b.append( field.getStringValue() );
        }
      }
    }

    return b.toString();
  }




  // { body }
  private static String marshal( DataFrame frame, int level, int indent ) {
    StringBuffer b = new StringBuffer();
    b.append( '{' );

    for ( DataField field : frame.getFields() ) {
      if ( level >= 0 ) {
        b.append( '\n' );
      }
      b.append( marshal( field, level >= 0 ? level + 1 : -1, indent ) );
    }

    if ( level >= 0 ) {
      b.append( '\n' );
    }

    if ( level >= 0 ) {
      b.append( getIndent( level, indent ) );
    }
    b.append( '}' );
    return b.toString();
  }




  static String getIndent( int level, int indent ) {
    StringBuffer b = new StringBuffer();
    for ( int x = 0; x < indent; x++ ) {
      b.append( ' ' );
    }
    String pad = b.toString();
    b.delete( 0, b.length() );

    for ( int x = 0; x < level; x++ ) {
      b.append( pad );
    }
    return b.toString();
  }

}
