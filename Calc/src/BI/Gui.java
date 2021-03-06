/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * calcGui.java
 *
 * Created on Oct 20, 2009, 2:04:45 PM
 */

package BI;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;

/**
 *
 * @author dsb
 */
public class Gui extends JFrame{
    Calc calc;
    /** Creates new form calcGui */
    public Gui(String name) {
        super(name);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    void initComponents() {
        calc = new Calc();
        Result = new JTextField();
        Enter = new JButton();
        Clear = new JButton();
        Plus = new JButton();
        Minus = new JButton();
        Times = new JButton();
        Div = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Result.setHorizontalAlignment(JTextField.RIGHT);
        Result.setText("0");

        Enter.setText("Enter");
        Enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                EnterActionPerformed(evt);
            }
        });

        Clear.setText("Clear");
        Clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });

        Plus.setText("+");
        Plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                PlusActionPerformed(evt);
            }
        });

        Minus.setText("-");
        Minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MinusActionPerformed(evt);
            }
        });

        Times.setText("*");
        Times.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TimesActionPerformed(evt);
            }
        });

        Div.setText("/");
        Div.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DivActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Result, GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Plus)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Minus)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Times)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Div))
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Enter, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Clear)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Result, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Enter)
                    .addComponent(Clear))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Plus)
                    .addComponent(Minus)
                    .addComponent(Times)
                    .addComponent(Div)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //------------------- MSP 2 new methods and the rest become public : START HERE
    
    public void ResultSetText(String x) {
        Result.setText(x);
    }
    
    public String ResultGetText() {
        return Result.getText();
    }
    
    public void EnterActionPerformed(ActionEvent evt) {//GEN-FIRST:event_EnterActionPerformed
        calc.set(Result.getText());
    }//GEN-LAST:event_EnterActionPerformed

    public void ClearActionPerformed(ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        calc.clear();
        Result.setText("0");
    }//GEN-LAST:event_ClearActionPerformed

    public void PlusActionPerformed(ActionEvent evt) {//GEN-FIRST:event_PlusActionPerformed
        calc.add( Result.getText());
        Result.setText(calc.get());
    }//GEN-LAST:event_PlusActionPerformed

    public void MinusActionPerformed(ActionEvent evt) {//GEN-FIRST:event_MinusActionPerformed
        calc.sub( Result.getText());
        Result.setText(calc.get());
    }//GEN-LAST:event_MinusActionPerformed

    public void TimesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_TimesActionPerformed
        calc.mul( Result.getText());
        Result.setText(calc.get());
    }//GEN-LAST:event_TimesActionPerformed

    public void DivActionPerformed(ActionEvent evt) {//GEN-FIRST:event_DivActionPerformed
        calc.div( Result.getText());
        Result.setText(calc.get());
    }//GEN-LAST:event_DivActionPerformed
    
    //------------------- MSP END HERE

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JButton Clear;
    JButton Div;
    JButton Enter;
    JButton Minus;
    JButton Plus;
    JTextField Result;
    JButton Times;
    // End of variables declaration//GEN-END:variables

    public void display( ) {
        Gui cg = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                cg.setVisible(true);
            }
        });
    }
}
