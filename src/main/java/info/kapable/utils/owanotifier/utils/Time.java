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

/**
 * Represents a time, which can be used for the duration of a Notification, fade times, etc.
 */
public final class Time {
	private int m_milliseconds;
	private boolean m_infinite;

	private Time(int milliseconds, boolean infinite) {
		m_milliseconds = milliseconds;
		m_infinite = infinite;
	}

	/**
	 * @param seconds
	 *            the number of seconds. This is truncated at the millisecond.
	 * @return a Time of length seconds
	 */
	public static Time seconds(double seconds) {
		return new Time((int) (seconds * 1000), false);
	}

	/**
	 * @param milliseconds
	 *            the number of milliseconds
	 * @return a Time of length milliseconds
	 */
	public static Time milliseconds(int milliseconds) {
		return new Time(milliseconds, false);
	}

	/**
	 * Specifies an infinite length of Time.
	 *
	 * @return a Time representing infinity
	 */
	public static Time infinite() {
		return new Time(-1, true);
	}

	/**
	 * @param time
	 *            the Time to add to
	 * @return the sum of the two times
	 */
	public Time add(Time time) {
		return new Time(m_milliseconds + time.getMilliseconds(), m_infinite || time.isInfinite());
	}

	/**
	 * @return the number of seconds, of -1 if it is infinite
	 */
	public double getSeconds() {
		if (m_infinite)
			return -1;
		return (double) m_milliseconds / 1000;
	}

	/**
	 * @return the number of milliseconds, or -1 if it is infinite
	 */
	public int getMilliseconds() {
		if (m_infinite)
			return -1;
		return m_milliseconds;
	}

	/**
	 * @return whether or not the Time is infinite
	 */
	public boolean isInfinite() {
		return m_infinite;
	}
}
