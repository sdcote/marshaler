
/*
 * Copyright (c) 2017 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 *      - Initial concept and implementation
 */
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.ParseException;
import coyote.marshaler.IapRequestMarshaler;


/**
 * 
 */
public class IapRequestMarshalerTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}




  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {}




  @Test
  public void simple() throws ParseException, IOException {
    String text = "entity {\n  entityName: \"substance\"\n  identifier: \"ALL-SUBSTANCE-ENTITY-ID\"\n}\n";
    DataFrame frame = IapRequestMarshaler.marshal( text );
  }




  @Test
  public void nested() throws ParseException, IOException {
    String text = "entity {\n  entityName: \"substance\"\n  constraint {\n    type: SEARCH\n    search {\n      searchName: \"solr\"\n      queryId: \"reactionTextQueryId\"\n    }\n  }\n  identifier: \"substanceEntityIdForReactionTextQuery\"\n}\n";
    DataFrame frame = IapRequestMarshaler.marshal( text );

  }

}
