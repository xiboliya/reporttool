/**
 * Copyright (C) 2014 冰原
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.reporttool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 * "搜索表格数据"对话框
 * 
 * @author 冰原
 * 
 */
public class SearchItemDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlContent = new JPanel();

  private JLabel lblFactoryName = new JLabel("单位名称:");
  private JLabel lblFactoryAddress = new JLabel("单位地址:");
  private JLabel lblMainBusinessSphere = new JLabel("经营范围:");
  private JLabel lblFactoryProperty = new JLabel("企业性质:");
  private JLabel lblTradeType = new JLabel("行业类型:");
  private JLabel lblCountry = new JLabel("国别:");
  private JLabel lblExpositionNeeds = new JLabel("展会需求:");
  private JLabel lblId = new JLabel("序号:");

  private BaseTextField txtFactoryName = new BaseTextField();
  private BaseTextField txtFactoryAddress = new BaseTextField();
  private BaseTextField txtMainBusinessSphere = new BaseTextField();
  private BaseTextField txtFactoryProperty = new BaseTextField();
  private BaseTextField txtTradeType = new BaseTextField();
  private BaseTextField txtCountry = new BaseTextField();
  private BaseTextField txtExpositionNeeds = new BaseTextField();
  private BaseTextField txtId = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字

  private JButton btnSearch = new JButton("搜索");
  private JButton btnReset = new JButton("重填");
  private JButton btnCancel = new JButton("取消");

  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Connection connection = null; // 与数据库的连接对象
  private Statement statement = null; // 用于执行静态SQL语句并返回它所生成结果的对象
  private ResultSet resultSet = null; // 数据库结果集对象
  private String sql = null;

  public SearchItemDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
    this.setSize(480, 260);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("搜索表格数据");
    this.pnlMain.setLayout(null);
    this.pnlContent.setLayout(null);
    this.pnlContent.setBorder(new TitledBorder("搜索条件"));
    this.pnlContent.setBounds(0, 0, 480, 170);
    this.lblFactoryName.setBounds(10, 20, 60, Util.VIEW_HEIGHT);
    this.txtFactoryName.setBounds(70, 20, 150, Util.INPUT_HEIGHT);
    this.lblFactoryAddress.setBounds(230, 20, 60, Util.VIEW_HEIGHT);
    this.txtFactoryAddress.setBounds(290, 20, 150, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblFactoryName);
    this.pnlContent.add(this.txtFactoryName);
    this.pnlContent.add(this.lblFactoryAddress);
    this.pnlContent.add(this.txtFactoryAddress);

    this.lblMainBusinessSphere.setBounds(10, 55, 60, Util.VIEW_HEIGHT);
    this.txtMainBusinessSphere.setBounds(70, 55, 150, Util.INPUT_HEIGHT);
    this.lblFactoryProperty.setBounds(230, 55, 60, Util.VIEW_HEIGHT);
    this.txtFactoryProperty.setBounds(290, 55, 150, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblMainBusinessSphere);
    this.pnlContent.add(this.txtMainBusinessSphere);
    this.pnlContent.add(this.lblFactoryProperty);
    this.pnlContent.add(this.txtFactoryProperty);

    this.lblTradeType.setBounds(10, 90, 60, Util.VIEW_HEIGHT);
    this.txtTradeType.setBounds(70, 90, 150, Util.INPUT_HEIGHT);
    this.lblCountry.setBounds(230, 90, 60, Util.VIEW_HEIGHT);
    this.txtCountry.setBounds(290, 90, 150, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblTradeType);
    this.pnlContent.add(this.txtTradeType);
    this.pnlContent.add(this.lblCountry);
    this.pnlContent.add(this.txtCountry);

    this.lblExpositionNeeds.setBounds(10, 125, 60, Util.VIEW_HEIGHT);
    this.txtExpositionNeeds.setBounds(70, 125, 150, Util.INPUT_HEIGHT);
    this.lblId.setBounds(230, 125, 60, Util.VIEW_HEIGHT);
    this.txtId.setBounds(290, 125, 150, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblExpositionNeeds);
    this.pnlContent.add(this.txtExpositionNeeds);
    this.pnlContent.add(this.lblId);
    this.pnlContent.add(this.txtId);

    this.pnlMain.add(this.pnlContent);

    this.btnSearch.setBounds(45, 180, 100, Util.BUTTON_HEIGHT);
    this.btnReset.setBounds(190, 180, 100, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(335, 180, 100, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnSearch);
    this.pnlMain.add(this.btnReset);
    this.pnlMain.add(this.btnCancel);

  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtFactoryName.addKeyListener(this.keyAdapter);
    this.txtFactoryAddress.addKeyListener(this.keyAdapter);
    this.txtMainBusinessSphere.addKeyListener(this.keyAdapter);
    this.txtFactoryProperty.addKeyListener(this.keyAdapter);
    this.txtTradeType.addKeyListener(this.keyAdapter);
    this.txtCountry.addKeyListener(this.keyAdapter);
    this.txtExpositionNeeds.addKeyListener(this.keyAdapter);
    this.txtId.addKeyListener(this.keyAdapter);

    this.btnSearch.addKeyListener(this.buttonKeyAdapter);
    this.btnReset.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);

    this.btnSearch.addActionListener(this);
    this.btnReset.addActionListener(this);
    this.btnCancel.addActionListener(this);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnSearch.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnReset.equals(e.getSource())) {
      this.reset();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * “重填”的处理方法
   */
  private void reset() {
    this.txtFactoryName.setText("");
    this.txtFactoryAddress.setText("");
    this.txtMainBusinessSphere.setText("");
    this.txtFactoryProperty.setText("");
    this.txtTradeType.setText("");
    this.txtCountry.setText("");
    this.txtExpositionNeeds.setText("");
    this.txtId.setText("");
    this.txtFactoryName.requestFocus();
  }

  /**
   * “搜索”的处理方法
   */
  private void searchItem() {
    String strFactoryName = this.txtFactoryName.getText().trim();
    String strFactoryAddress = this.txtFactoryAddress.getText().trim();
    String strMainBusinessSphere = this.txtMainBusinessSphere.getText().trim();
    String strFactoryProperty = this.txtFactoryProperty.getText().trim();
    String strTradeType = this.txtTradeType.getText().trim();
    String strCountry = this.txtCountry.getText().trim();
    String strExpositionNeeds = this.txtExpositionNeeds.getText().trim();
    String strId = this.txtId.getText().trim();
    String strWhere = "";
    if (!strFactoryName.trim().isEmpty()) {
      strWhere += " factoryName like '%" + strFactoryName + "%'";
    }
    if (!strFactoryAddress.trim().isEmpty()) {
      strWhere += "and factoryAddress like '%" + strFactoryAddress + "%'";
    }
    if (!strMainBusinessSphere.trim().isEmpty()) {
      strWhere += "and mainBusinessSphere like '%" + strMainBusinessSphere
          + "%'";
    }
    if (!strFactoryProperty.trim().isEmpty()) {
      strWhere += "and factoryProperty like '%" + strFactoryProperty + "%'";
    }
    if (!strTradeType.trim().isEmpty()) {
      strWhere += "and tradeType like '%" + strTradeType + "%'";
    }
    if (!strCountry.trim().isEmpty()) {
      strWhere += "and country like '%" + strCountry + "%'";
    }
    if (!strExpositionNeeds.trim().isEmpty()) {
      strWhere += "and expositionNeeds like '%" + strExpositionNeeds + "%'";
    }
    if (!strId.trim().isEmpty()) {
      strWhere += "and id=" + strId;
    }

    if (strWhere.trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "搜索条件不能为空，请填写！", "搜索失败！",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (strWhere.startsWith("and")) {
      strWhere = strWhere.substring(3);
    }
    this.sql = "select * from " + Util.TABLE_NAME + " where " + strWhere;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(this.sql);
      if (!this.resultSet.next()) {
        JOptionPane.showMessageDialog(this, "未能搜索到符合条件的数据项，请修改搜索条件！", "搜索失败！",
            JOptionPane.ERROR_MESSAGE);
      } else {
        ((ReportToolFrame) this.getOwner()).setSql(this.sql);
        ((ReportToolFrame) this.getOwner()).refresh();
        this.onCancel();
      }
    } catch (SQLException x) {
      x.printStackTrace();
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.searchItem();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
