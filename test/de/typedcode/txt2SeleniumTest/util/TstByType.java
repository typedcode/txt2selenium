/**
 * MIT License
 * 
 * Copyright (c) 2018 Markus Fischer (www.typedcode.de)
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

package de.typedcode.txt2SeleniumTest.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import de.typedcode.txt2Selenium.util.ByType;

@NonNullByDefault
class TstByType {

    @Test
    void getByTypeId() {
        ByType type = ByType.getType( "id" );
        assertEquals( ByType.ID, type );
    }

    @Test
    void getByTypeName() {
        ByType type = ByType.getType( "name" );
        assertEquals( ByType.NAME, type );
    }

    @Test
    void getByTypeNameCaseInsensitive() {
        ByType type = ByType.getType( "nAmE" );
        assertEquals( ByType.NAME, type );
    }

    @Test
    void getByTypeXPath() {
        ByType type = ByType.getType( "xpath" );
        assertEquals( ByType.XPATH, type );
    }

    @Test
    void getByTypeUnknown() {
        Executable executable = () -> {
            ByType.getType( "unknown" );
        };

        assertThrows( RuntimeException.class, executable );
    }

}
