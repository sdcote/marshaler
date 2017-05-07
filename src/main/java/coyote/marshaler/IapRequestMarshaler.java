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
import java.util.List;

import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.ParseException;


/**
 * 
 */
public class IapRequestMarshaler {

  public static DataFrame marshal( String text ) throws ParseException, IOException {
    DataFrame retval = new DataFrame();
    List<DataFrame> frames = null;
    frames = new IapRequestParser( text ).setLineDelimitsFields( true ).parse();

    if ( frames != null ) {
      for ( DataFrame frame : frames ) {
        retval.add( frame );
      }
    }
    return retval;
  }

}
