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

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.JSONMarshaler;
import coyote.dataframe.marshal.ParseException;


/**
 * 
 */
public class Main extends JFrame {

  private static final long serialVersionUID = -1349395178230143708L;
  private static final String TITLE = "Explore Request Marshaler";
  private DataFrame model = null;
  private File currentFile = null;
  private boolean modified = false;

  private JPanel contentPane;
  private JButton btnConvert;
  private JScrollPane scrollPane;
  private JTextArea textContent;
  private JMenuBar menuBar;
  private JMenu mnFile;
  private JMenuItem mntmNew;
  private JMenuItem mntmExit;
  private JMenuItem mntmOpen;
  private JMenuItem mntmSave;
  private JMenuItem mntmSaveAs;
  private JPanel statusPanel;
  private JLabel statusLabel;
  private Main PARENT;




  /**
   * Launch the application.
   */
  public static void main( String[] args ) {
    try {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    } catch ( Throwable e ) {
      e.printStackTrace();
    }
    EventQueue.invokeLater( new Runnable() {
      public void run() {
        try {
          Main frame = new Main();
          frame.setVisible( true );
        } catch ( Exception e ) {
          e.printStackTrace();
        }
      }
    } );
  }




  /**
   * Create the frame.
   */
  public Main() {
    PARENT = this;
    initComponents();
    createEvents();
  }




  private void refreshTitle() {
    StringBuffer b = new StringBuffer( TITLE );
    b.append( " " );
    if ( currentFile != null ) {
      b.append( "(" );
      b.append( currentFile.getName() );
      b.append( ")" );
    }
    b.append( modified ? "*" : "" );
    setTitle( b.toString() );
  }




  private File createJFileChooser( boolean open ) {
    int option;
    JFileChooser jf = new JFileChooser();

    if ( open ) {
      option = jf.showOpenDialog( this );
    } else {
      jf.setSelectedFile( currentFile );
      option = jf.showSaveDialog( this );
    }

    if ( option == JFileChooser.APPROVE_OPTION ) {
      currentFile = jf.getSelectedFile();
      return currentFile;
    }

    return null;
  }




  // call either saveCurrent or saveNew depending if we have a current file 
  private boolean save() {
    if ( currentFile != null ) {
      return saveCurrent();
    } else {
      return saveNew();
    }
  }




  private boolean saveNew() {
    try {
      File newFile = createJFileChooser( false );
      if ( newFile != null ) {
        textContent.write( new FileWriter( newFile ) );
        statusLabel.setText( "Saved " + currentFile.getAbsolutePath() );
        modified = false;
        currentFile = newFile;
        refreshTitle();
        return true;
      }
    } catch ( Exception ex ) {
      statusLabel.setText( "Error: " + ex.getMessage() );
      return false;
    }
    return false;
  }




  private boolean saveCurrent() {
    try {
      textContent.write( new FileWriter( currentFile ) );
      modified = false;
      statusLabel.setText( "Saved " + currentFile.getAbsolutePath() );
      refreshTitle();
      return true;
    } catch ( Exception ex ) {
      statusLabel.setText( "Error: " + ex.getMessage() );
      return false;
    }
  }




  /**
   * Create all the events
   */
  private void createEvents() {

    // Handle the Convert button press
    btnConvert.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        try {
          model = IapRequestMarshaler.marshal( textContent.getText() );
          textContent.setText( JSONMarshaler.toFormattedString( model ) );
          statusLabel.setText( "Successfully converted" );
          modified = true;
        } catch ( Exception ex ) {
          if ( ex instanceof ParseException ) {
            textContent.setCaretPosition( ( (ParseException)ex ).getOffset() );
            textContent.requestFocusInWindow();
          }
          statusLabel.setText( ex.getMessage() );
        }
      }
    } );

    // New file (clear everything out) 
    mntmNew.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        if ( modified ) {
          int option = JOptionPane.showConfirmDialog( PARENT, "Save before clearing the buffer?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION );
          if ( JOptionPane.CANCEL_OPTION == option ) {
            return;
          } else if ( JOptionPane.YES_OPTION == option ) {
            save();
          }
        }
        statusLabel.setText( "Ready" );
        setTitle( TITLE );
        textContent.setText( null );
        textContent.getDocument().addDocumentListener( new MyDocumentListener() );
        currentFile = null;
        modified = false;
      }
    } );

    // Open (read) a new file
    mntmOpen.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        try {
          File newFile = createJFileChooser( true );
          if ( newFile != null ) {
            textContent.read( new FileReader( newFile ), null );
            textContent.getDocument().addDocumentListener( new MyDocumentListener() );
            modified = false;
            statusLabel.setText( "Opened " + currentFile.getAbsolutePath() );
            currentFile = newFile;
            refreshTitle();
          }
        } catch ( Exception ex ) {
          statusLabel.setText( "Error: " + ex.getMessage() );
        }
      }
    } );

    // Save a file
    mntmSave.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        save();
      }
    } );

    // Save the current file with a different name
    mntmSaveAs.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        saveNew();
      }
    } );

    // Exit the application
    mntmExit.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        System.exit( JFrame.EXIT_ON_CLOSE );
      }
    } );

  }




  /**
   * Create and initialize components
   */
  private void initComponents() {

    List<Image> icons = new ArrayList<Image>();
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler16x16.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler24x24.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler32x32.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler64x64.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler128x128.png" ) ) );
    icons.add( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler256x256.png" ) ) );
    setIconImages( icons );

    setTitle( TITLE );
    setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
    addWindowListener( new WindowAdapter() {
      @Override
      public void windowClosing( WindowEvent we ) {
        if ( modified ) {
          int option = JOptionPane.showConfirmDialog( PARENT, "Save before exiting?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION );
          if ( option == JOptionPane.CANCEL_OPTION ) {
            return;
          } else if ( option == JOptionPane.YES_OPTION ) {
            if ( !save() ) {
              return;
            }
          }
        }
        System.exit( 0 );
      }
    } );
    setBounds( 100, 100, 600, 600 );

    menuBar = new JMenuBar();
    menuBar.setBackground( SystemColor.menu );
    setJMenuBar( menuBar );

    mnFile = new JMenu( "File" );
    mnFile.setMnemonic( KeyEvent.VK_F );
    menuBar.add( mnFile );

    mntmNew = new JMenuItem( "New" );
    mntmNew.setMnemonic( KeyEvent.VK_N );
    mntmNew.setToolTipText( "Clear the buffer and start fresh" );
    mnFile.add( mntmNew );

    mntmOpen = new JMenuItem( "Open" );
    mntmOpen.setMnemonic( KeyEvent.VK_O );
    mntmOpen.setToolTipText( "Open a text file to convert" );
    mnFile.add( mntmOpen );

    mntmSave = new JMenuItem( "Save" );
    mntmSave.setMnemonic( KeyEvent.VK_S );
    mntmSave.setToolTipText( "Save the current text file" );
    mnFile.add( mntmSave );

    mntmSaveAs = new JMenuItem( "Save As" );
    mntmSaveAs.setMnemonic( KeyEvent.VK_A );
    mntmSaveAs.setToolTipText( "Save the current text file as a different name" );
    mnFile.add( mntmSaveAs );

    JSeparator separator = new JSeparator();
    mnFile.add( separator );

    mntmExit = new JMenuItem( "Exit" );
    mntmExit.setMnemonic( KeyEvent.VK_X );
    mntmExit.setToolTipText( "Close the tool" );
    mnFile.add( mntmExit );

    contentPane = new JPanel();
    contentPane.setBorder( new EmptyBorder(0, 0, 0, 0) );
    setContentPane( contentPane );

    scrollPane = new JScrollPane();
    textContent = new JTextArea();
    textContent.setFont( new Font( "Consolas", textContent.getFont().getStyle(), textContent.getFont().getSize() + 1 ) );
    textContent.getDocument().addDocumentListener( new MyDocumentListener() );
    DropTarget target = new DropTarget( textContent, new MyDropTargetListener() );
    scrollPane.setViewportView( textContent );

    statusPanel = new JPanel();
    statusPanel.setLayout( new BoxLayout( statusPanel, BoxLayout.X_AXIS ) );
    statusPanel.setBorder( new BevelBorder( BevelBorder.LOWERED ) );

    btnConvert = new JButton( "Convert" );
    btnConvert.setToolTipText( "Convert the buffer into the selected format" );

    GroupLayout gl_contentPane = new GroupLayout( contentPane );
    gl_contentPane.setHorizontalGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING ).addGroup( gl_contentPane.createSequentialGroup().addComponent( statusPanel, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE ).addPreferredGap( ComponentPlacement.RELATED ).addComponent( btnConvert, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE ) ).addComponent( scrollPane, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE ) );
    gl_contentPane.setVerticalGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING ).addGroup( gl_contentPane.createSequentialGroup().addComponent( scrollPane, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE ).addPreferredGap( ComponentPlacement.RELATED ).addGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING, false ).addComponent( statusPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ).addComponent( btnConvert, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) ) ) );

    statusLabel = new JLabel( "Ready" );
    statusLabel.setToolTipText( "Current status of the buffer" );
    statusLabel.setHorizontalAlignment( SwingConstants.LEFT );
    statusPanel.add( statusLabel );

    contentPane.setLayout( gl_contentPane );
  }

  /**
   * This listens for changes in the document
   */
  class MyDocumentListener implements DocumentListener {

    public void insertUpdate( DocumentEvent e ) {
      statusLabel.setText( "inserted into: " + e.getOffset() + "-" + e.getLength() );
      if ( !modified ) {
        modified = true;
        refreshTitle();
      }
    }




    public void removeUpdate( DocumentEvent e ) {
      statusLabel.setText( "removed from: " + e.getOffset() + "-" + e.getLength() );
      if ( !modified ) {
        modified = true;
        refreshTitle();
      }
    }




    public void changedUpdate( DocumentEvent e ) {
      //Plain text components do not fire these events
    }
  }

  //
  class MyDropTargetListener implements DropTargetListener {
    public void dragEnter( DropTargetDragEvent e ) {}




    public void dragExit( DropTargetEvent e ) {}




    public void dragOver( DropTargetDragEvent e ) {}




    public void dropActionChanged( DropTargetDragEvent e ) {

    }




    public void drop( DropTargetDropEvent e ) {
      try {
        // Accept the drop first, important!
        if ( !modified ) {
          e.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE );
        }

        // Get the files that are dropped as java.util.List
        List list = (List)e.getTransferable().getTransferData( DataFlavor.javaFileListFlavor );

        // Now get the first file from the list,
        currentFile = (File)list.get( 0 );
        textContent.read( new FileReader( currentFile ), null );
        textContent.getDocument().addDocumentListener( new MyDocumentListener() );
        modified = false;
        refreshTitle();
        statusLabel.setText( "Opened " + currentFile.getAbsolutePath() );
      } catch ( Exception ex ) {
        statusLabel.setText( "Error: " + ex.getMessage() );
      }
    }
  }

}
