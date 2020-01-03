package com.baimo.view;

import javax.swing.*;

/**
 * 本来想整个窗体程序的...懒癌发作，告辞
 */
public class HPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HPage");
        frame.setContentPane(new HPage().jp1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel jp1;
}
