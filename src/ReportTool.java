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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * �����������
 * 
 * @author ��ԭ
 * 
 */
public class ReportTool {

  /**
   * ����������
   */
  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME);
        } catch (Exception x) {
          x.printStackTrace();
        }
        Util.setDefaultFont();
        System.setProperty("java.awt.im.style", "on-the-spot"); // ȥ���ı�����������ʱ�����������봰��
        new ReportToolFrame();
      }
    });
  }
}
