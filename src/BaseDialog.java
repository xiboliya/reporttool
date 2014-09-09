/**
 * Copyright (C) 2014 ��ԭ
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

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * ����ͳһ��Ϊ��JDialog������
 * 
 * @author ��ԭ
 * 
 */
public abstract class BaseDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  private JFrame owner = null;

  public BaseDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.owner = owner;
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  /**
   * "ȷ��"������Ĭ�Ϸ�������Ϊ���󷽷����������ʵ��
   */
  public abstract void onEnter();

  /**
   * "ȡ��"������Ĭ�Ϸ�������Ϊ���󷽷����������ʵ��
   */
  public abstract void onCancel();

  /**
   * ��д����ķ�������ʾ�����ص�ǰ����
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.setLocationRelativeTo(this.owner);
    }
    super.setVisible(visible);
  }

}