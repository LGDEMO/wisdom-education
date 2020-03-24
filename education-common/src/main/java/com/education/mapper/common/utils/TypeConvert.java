/**
 * Copyright (c) 2017-2018, zengjintao (1913188966@qq.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.education.mapper.common.utils;

public final class TypeConvert {


    /**
     * 数据转化
     * @param target
     * @return
     */
    public static final Object convert(Object target) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        if (String.class == type) {
            return (String)target;
        } else if (int.class == type || Integer.class == type) {
            return Integer.parseInt(String.valueOf(target));
        }
        else if (type == Long.class || type == long.class) {
            return Long.parseLong(String.valueOf(target));
        }
        else if (type == Double.class || type == double.class) {
            return Double.parseDouble(String.valueOf(target));
        }
        else if (type == Boolean.class || type == boolean.class) {
            String value = String.valueOf(target).toLowerCase();
            if ("1".equals(value) || "true".equals(value)) {
                return Boolean.TRUE;
            } else if ("0".equals(value) || "false".equals(value)) {
                return Boolean.FALSE;
            } else {
                throw new RuntimeException("Can not parse to boolean type of value: " + target);
            }
        } else if (type == java.math.BigInteger.class) {
            return new java.math.BigInteger(String.valueOf(target));
        } else if (type == byte[].class) {
            return String.valueOf(target).getBytes();
        } else{
            throw new RuntimeException(type.getName() + " can not be converted, please use other type in your config class!");
        }
    }
}
