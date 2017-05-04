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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.awt.Font;


/**
 * 
 */
public class Main extends JFrame {

  private JPanel contentPane;
  private JButton btnSave;
  private JButton btnConvert;
  private JButton btnLoad;
  private JScrollPane scrollPane;
  private JTextArea textContent;




  /**
   * Launch the application.
   */
  public static void main( String[] args ) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
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
    setIconImage( Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/marshaler.png")) );
    setTitle( "IAP Explore Request Marshaler" );
    initComponents();
    creatEvents();
  }




  /**
   * Create all the events
   */
  private void creatEvents() {
    
    // Handle the Save button press
    btnSave.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {}
    } );
    
    // Handle the Load button press
    btnLoad.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    
    // Handle the Convert button press
    btnConvert.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });

  }




  /**
   * Create and initialize components
   */
  private void initComponents() {
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    setBounds( 100, 100, 800, 800 );
    contentPane = new JPanel();
    contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
    setContentPane( contentPane );

    btnSave = new JButton( "Save" );

    btnLoad = new JButton( "Load" );

    btnConvert = new JButton( "Convert" );
    
    scrollPane = new JScrollPane();
    GroupLayout gl_contentPane = new GroupLayout( contentPane );
    gl_contentPane.setHorizontalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
          .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_contentPane.createSequentialGroup()
              .addComponent(btnLoad)
              .addPreferredGap(ComponentPlacement.RELATED, 448, Short.MAX_VALUE)
              .addComponent(btnConvert, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
              .addGap(19)
              .addComponent(btnSave))
            .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 760, GroupLayout.PREFERRED_SIZE))
          .addContainerGap())
    );
    gl_contentPane.setVerticalGroup(
      gl_contentPane.createParallelGroup(Alignment.TRAILING)
        .addGroup(gl_contentPane.createSequentialGroup()
          .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
            .addComponent(btnLoad)
            .addComponent(btnConvert)
            .addComponent(btnSave)))
    );
    
    textContent = new JTextArea();
    textContent.setFont(new Font("Consolas", textContent.getFont().getStyle(), textContent.getFont().getSize() + 1));
    scrollPane.setViewportView(textContent);
    contentPane.setLayout( gl_contentPane );
  }
}
