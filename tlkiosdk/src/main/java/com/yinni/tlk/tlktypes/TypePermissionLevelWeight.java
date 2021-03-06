/*
 * Copyright (c) 2017-2018 PLACTAL.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yinni.tlk.tlktypes;


public class TypePermissionLevelWeight implements TLKType.Packer {
    private TypePermissionLevel mPermission;
    private short mWeight;

    /**
     * single active permissin 용 생성자
     * @param nameForActive
     */
    TypePermissionLevelWeight(String nameForActive ) {
        this( nameForActive, (short)1);
    }

    TypePermissionLevelWeight(String nameForActive, short weight ) {
        mPermission = new TypePermissionLevel( nameForActive, "active");
        mWeight = weight;
    }

    @Override
    public void pack(TLKType.Writer writer) {

        mPermission.pack(writer);

        writer.putShortLE( mWeight);
    }
}
