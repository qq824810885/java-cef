// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.detailed.ui;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cef.OS;
import org.cef.browser.CefBrowser;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

  private final JButton backButton_;
  private final JButton forwardButton_;
  private final JButton reloadButton_;
  private final JTextField address_field_;
  private final JLabel zoom_label_;
  private double zoomLevel_ = 0;
  private final CefBrowser browser_;

  public ControlPanel(CefBrowser browser) {
    browser_ = browser;
    setEnabled(browser_ != null);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    add(Box.createHorizontalStrut(5));
    add(Box.createHorizontalStrut(5));

    backButton_ = new JButton("Back");
    backButton_.setAlignmentX(LEFT_ALIGNMENT);
    backButton_.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.goBack();
      }
    });
    add(backButton_);
    add(Box.createHorizontalStrut(5));

    forwardButton_ = new JButton("Forward");
    forwardButton_.setAlignmentX(LEFT_ALIGNMENT);
    forwardButton_.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.goForward();
      }
    });
    add(forwardButton_);
    add(Box.createHorizontalStrut(5));

    reloadButton_ = new JButton("Reload");
    reloadButton_.setAlignmentX(LEFT_ALIGNMENT);
    reloadButton_.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (reloadButton_.getText().equalsIgnoreCase("reload")) {
          int mask = OS.isMacintosh()
                     ? ActionEvent.META_MASK
                     : ActionEvent.CTRL_MASK;
          if ((e.getModifiers() & mask) != 0) {
            System.out.println("Reloading - ignoring cached values");
            browser_.reloadIgnoreCache();
          } else {
            System.out.println("Reloading - using cached values");
            browser_.reload();
          }
        } else {
          browser_.stopLoad();
        }
      }
    });
    add(reloadButton_);
    add(Box.createHorizontalStrut(5));

    JLabel addressLabel = new JLabel("Address:");
    addressLabel.setAlignmentX(LEFT_ALIGNMENT);
    add(addressLabel);
    add(Box.createHorizontalStrut(5));

    address_field_ = new JTextField(100);
    address_field_.setAlignmentX(LEFT_ALIGNMENT);
    address_field_.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.loadURL(address_field_.getText());
      }
    });
    address_field_.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .clearGlobalFocusOwner();
        address_field_.requestFocus();
      }
    });
    add(address_field_);
    add(Box.createHorizontalStrut(5));

    JButton goButton = new JButton("Go");
    goButton.setAlignmentX(LEFT_ALIGNMENT);
    goButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.loadURL(address_field_.getText());
      }
    });
    add(goButton);
    add(Box.createHorizontalStrut(5));

    JButton minusButton = new JButton("-");
    minusButton.setAlignmentX(CENTER_ALIGNMENT);
    minusButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.setZoomLevel(--zoomLevel_);
        zoom_label_.setText(new Double(zoomLevel_).toString());
      }
    });
    add(minusButton);

    zoom_label_ = new JLabel("0.0");
    add(zoom_label_);

    JButton plusButton = new JButton("+");
    plusButton.setAlignmentX(CENTER_ALIGNMENT);
    plusButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        browser_.setZoomLevel(++zoomLevel_);
        zoom_label_.setText(new Double(zoomLevel_).toString());
      }
    });
    add(plusButton);
  }

  public void update(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
    if (browser == browser_) {
      backButton_.setEnabled(canGoBack);
      forwardButton_.setEnabled(canGoForward);
      reloadButton_.setText( isLoading ? "Abort" : "Reload");
    }
  }

  public String getAddress() {
    return address_field_.getText();
  }

  public void setAddress(CefBrowser browser, String address) {
    if (browser == browser_)
      address_field_.setText(address);
  }
}
