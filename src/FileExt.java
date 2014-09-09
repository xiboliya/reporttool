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

/**
 * 用于标识文件扩展名类型的枚举
 * 
 * @author 冰原
 * 
 */
public enum FileExt {
  /**
   * Excel Sheet(Excel表格文件)
   */
  XLS;

  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case XLS:
      return ".xls";
    default:
      return ".xls";
    }
  }

  /**
   * 获取文件扩展名的文字描述
   * 
   * @return 文件扩展名的文字描述
   */
  public String getDescription() {
    switch (this) {
    case XLS:
      return "Excel电子表格(.xls)";
    default:
      return "Excel电子表格(.xls)";
    }
  }

}
