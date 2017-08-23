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
 */package info.kapable.utils.owanotifier.theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a collection of themes that can be accessed later.
 */
public class ThemePackage {
	private Map<Class<?>, Object> m_themes;

	public ThemePackage() {
		m_themes = new HashMap<Class<?>, Object>();
	}

	/**
	 * Sets the theme object related with the theme class. The first parameter should be the class of the second
	 * parameter.
	 *
	 * @param <T>
	 *            the Class of the theme
	 * @param themeClass
	 *            the Class of the theme to set
	 * @param theme
	 *            the theme to set
	 */
	public <T> void setTheme(Class<T> themeClass, T theme) {
		m_themes.put(themeClass, theme);
	}

	/**
	 * Gets the theme object related with the theme class.
	 *
	 * @param <T>
	 *            the Class of the theme
	 * @param themeClass
	 *            the Class of the theme to return
	 * @return the theme corresponding with the given Class
	 */
	public <T> T getTheme(Class<T> themeClass) {
		@SuppressWarnings("unchecked")
		T theme = (T) m_themes.get(themeClass);
		return theme;
	}
}
