/**
The MIT License (MIT)

Copyright (c) 2017 Mathieu GOULIN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */package info.kapable.utils.owanotifier.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathUtilsTest {
	@Test
	public void testSignNegative() {
		assertEquals("Sign of negtive number should be negative", -1, MathUtils.sign(-8.5));
	}

	@Test
	public void testSignPositive() {
		assertEquals("Sign of positive number should be positive", 1, MathUtils.sign(8.5));
	}

	@Test
	public void testSignZero() {
		assertEquals("Sign of zero should be zero", 0, MathUtils.sign(0));
	}

	@Test
	public void testClampMin() {
		assertEquals("Clamp should work for min", 5.0, MathUtils.clamp(2.0, 5.0, 7.0), TestUtils.TINY_DELTA);
	}

	@Test
	public void testClampMax() {
		assertEquals("Clamp should work for max", 7.0, MathUtils.clamp(10.0, 5.0, 7.0), TestUtils.TINY_DELTA);
	}

	@Test
	public void testClampMiddle() {
		assertEquals("Clamp should work for in between values", 6.0, MathUtils.clamp(6.0, 5.0, 7.0), TestUtils.TINY_DELTA);
	}
}
