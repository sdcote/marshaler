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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


/**
 * 
 */
public class Main extends JFrame {

  private static final long serialVersionUID = -1349395178230143708L;

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
  private JTextField statusBar;
  private JMenuItem mntmBoo;




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
    initComponents();
    creatEvents();
  }




  /**
   * Create all the events
   */
  private void creatEvents() {

    // Handle the Convert button press
    btnConvert.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {}
    } );

    // New file (clear everything out) 
    mntmNew.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {}
    } );

    // Open (read) a new file
    mntmOpen.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {}
    } );

    // Save a file
    mntmSave.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {}
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

    setIconImage( Toolkit.getDefaultToolkit().getImage( Main.class.getResource( "/marshaler.png" ) ) );
    setTitle( "IAP Explore Request Marshaler" );
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    setBounds( 100, 100, 800, 800 );

    menuBar = new JMenuBar();
    menuBar.setBackground( SystemColor.menu );
    setJMenuBar( menuBar );

    mnFile = new JMenu( "File" );
    mnFile.setMnemonic( KeyEvent.VK_F );
    menuBar.add( mnFile );

    mntmNew = new JMenuItem( "New" );
    mntmNew.setMnemonic( KeyEvent.VK_N );
    mnFile.add( mntmNew );

    mntmOpen = new JMenuItem( "Open" );
    mntmOpen.setMnemonic( KeyEvent.VK_O );
    mnFile.add( mntmOpen );

    mntmSave = new JMenuItem( "Save" );
    mntmSave.setMnemonic( KeyEvent.VK_S );
    mnFile.add( mntmSave );

    JSeparator separator = new JSeparator();
    mnFile.add( separator );

    mntmExit = new JMenuItem( "Exit" );
    mntmExit.setMnemonic( KeyEvent.VK_X );
    mnFile.add( mntmExit );

    contentPane = new JPanel();
    contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
    setContentPane( contentPane );

    scrollPane = new JScrollPane();
    textContent = new JTextArea();
    textContent.setFont( new Font( "Consolas", textContent.getFont().getStyle(), textContent.getFont().getSize() + 1 ) );
    scrollPane.setViewportView( textContent );

    statusBar = new JTextField();
    statusBar.setForeground( Color.GRAY );
    statusBar.setText( "Ready" );
    statusBar.setEditable( false );
    statusBar.setBackground( UIManager.getColor( "Button.light" ) );
    statusBar.setColumns( 10 );

    btnConvert = new JButton( "Convert" );

    GroupLayout gl_contentPane = new GroupLayout( contentPane );
    gl_contentPane.setHorizontalGroup( gl_contentPane.createParallelGroup( Alignment.LEADING ).addGroup( gl_contentPane.createSequentialGroup().addGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING, false ).addGroup( gl_contentPane.createSequentialGroup().addComponent( statusBar ).addPreferredGap( ComponentPlacement.RELATED ).addComponent( btnConvert, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE ) ).addComponent( scrollPane, GroupLayout.PREFERRED_SIZE, 760, GroupLayout.PREFERRED_SIZE ) ).addGap( 4 ) ) );
    gl_contentPane.setVerticalGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING ).addGroup( gl_contentPane.createSequentialGroup().addComponent( scrollPane, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE ).addGap( 21 ).addGroup( gl_contentPane.createParallelGroup( Alignment.TRAILING ).addComponent( btnConvert ).addComponent( statusBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE ) ) ) );

    contentPane.setLayout( gl_contentPane );
  }
}
