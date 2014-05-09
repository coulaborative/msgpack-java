//
// MessagePack for Java
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.core;

import org.msgpack.core.MessagePack.Code;

/**
 * MessageTypeFamily is a group of {@link org.msgpack.core.MessageType}s
 */
public enum MessageTypeFamily {

    EOF(false, false),
    UNKNOWN(false, false),
    NIL(false, false),
    BOOLEAN(false, false),
    INTEGER(true, false),
    FLOAT(true, false),
    STRING(false, true),
    BINARY(false, true),
    ARRAY(false, false),
    MAP(false, false),
    EXTENDED(false, false);

    private boolean numberType;
    private boolean rawType;

    private MessageTypeFamily(boolean numberType, boolean rawType) {
        this.numberType = numberType;
        this.rawType = rawType;
    }

    public boolean isNilType() {
        return this == NIL;
    }

    public boolean isBooleanType() {
        return this == BOOLEAN;
    }

    public boolean isNumberType() {
        return numberType;
    }

    public boolean isIntegerType() {
        return this == INTEGER;
    }

    public boolean isFloatType() {
        return this == FLOAT;
    }

    public boolean isRawType() {
        return rawType;
    }

    public boolean isStringType() {
        return this == STRING;
    }

    public boolean isBinaryType() {
        return this == BINARY;
    }

    public boolean isArrayType() {
        return this == ARRAY;
    }

    public boolean isMapType() {
        return this == MAP;
    }

    public boolean isExtendedType() {
        return this == EXTENDED;
    }


    static MessageTypeFamily toTypeFamily(final byte b) {
        if (Code.isPosFixInt(b)) { // positive fixint
            return MessageTypeFamily.INTEGER;
        }
        if (Code.isNegFixInt(b)) { // negative fixint
            return MessageTypeFamily.INTEGER;
        }
        if (Code.isFixStr(b)) { // fixstr
            return MessageTypeFamily.STRING;
        }
        if (Code.isFixedArray(b)) { // fixarray
            return MessageTypeFamily.ARRAY;
        }
        if (Code.isFixedMap(b)) { // fixmap
            return MessageTypeFamily.MAP;
        }
        switch (b) {
            case Code.NIL: // nil
                return MessageTypeFamily.NIL;
            case Code.FALSE: // false
            case Code.TRUE: // true
                return MessageTypeFamily.BOOLEAN;
            case Code.BIN8: // bin 8
            case Code.BIN16: // bin 16
            case Code.BIN32: // bin 32
                return MessageTypeFamily.BINARY;
            case Code.EXT8: // ext 8
            case Code.EXT16: // ext 16
            case Code.EXT32: // ext 32
                return MessageTypeFamily.EXTENDED;
            case Code.FLOAT32: // float 32
            case Code.FLOAT64: // float 64
                return MessageTypeFamily.FLOAT;
            case Code.UINT8: // unsigned int 8
            case Code.UINT16: // unsigned int 16
            case Code.UINT32: // unsigned int 32
            case Code.UINT64: // unsigned int 64
            case Code.INT8: // signed int 8
            case Code.INT16: // signed int 16
            case Code.INT32: // signed int 32
            case Code.INT64: // signed int 64
                return MessageTypeFamily.INTEGER;
            case Code.FIXEXT1: // fixext 1
            case Code.FIXEXT2: // fixext 2
            case Code.FIXEXT4: // fixext 4
            case Code.FIXEXT8: // fixext 8
            case Code.FIXEXT16: // fixext 16
                return MessageTypeFamily.EXTENDED;
            case Code.STR8: // str 8
            case Code.STR16: // str 16
            case Code.STR32: // str 32
                return MessageTypeFamily.STRING;
            case Code.ARRAY16: // array 16
            case Code.ARRAY32: // array 32
                return MessageTypeFamily.ARRAY;
            case Code.MAP16: // map 16
            case Code.MAP32: // map 32
                return MessageTypeFamily.MAP;
            default:
                return MessageTypeFamily.UNKNOWN;
        }
    }

    private static byte[] table = new byte[256];
    private static MessageTypeFamily[] symbolTable = MessageTypeFamily.values();
    static {
        // Preparing symbol table (byte value -> ValueType ordinal)
        for(int b = 0; b <= 0xFF; ++b) {
            table[b] = (byte) toTypeFamily((byte) b).ordinal();
        }
    }

    public static MessageTypeFamily lookUp(final byte b) {
        return symbolTable[table[b & 0xFF]];
    }


}