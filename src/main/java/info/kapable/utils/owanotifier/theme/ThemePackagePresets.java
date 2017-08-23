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

import java.awt.Color;
import java.awt.Font;

public class ThemePackagePresets {
	private ThemePackagePresets() {
	}

	public static ThemePackage cleanLight() {
		ThemePackage pack = new ThemePackage();

		WindowTheme window = new WindowTheme();
		window.background = new Color(242, 255, 230);
		window.foreground = new Color(160, 205, 250);
		window.opacity = 0.8f;
		window.width = 300;
		window.height = 150;

		TextTheme text = new TextTheme();
		text.from = new Font("Arial", Font.BOLD, 10);
		text.title = new Font("Arial", Font.BOLD, 12);
		text.subtitle = new Font("Arial", Font.PLAIN, 10);
		text.titleColor = new Color(10, 10, 10);
		text.subtitleColor = new Color(10, 10, 10);
		text.fromColor = new Color(10, 10, 10);

		pack.setTheme(WindowTheme.class, window);
		pack.setTheme(TextTheme.class, text);

		return pack;
	}

	public static ThemePackage cleanDark() {
		ThemePackage pack = new ThemePackage();

		WindowTheme window = new WindowTheme();
		window.background = new Color(0, 0, 0);
		window.foreground = new Color(16, 124, 162);
		window.opacity = 0.8f;
		window.width = 300;
		window.height = 100;

		TextTheme text = new TextTheme();
		text.title = new Font("Arial", Font.BOLD, 22);
		text.subtitle = new Font("Arial", Font.PLAIN, 16);
		text.titleColor = new Color(200, 200, 200);
		text.subtitleColor = new Color(200, 200, 200);

		pack.setTheme(WindowTheme.class, window);
		pack.setTheme(TextTheme.class, text);

		return pack;
	}

	public static ThemePackage aqua() {
		ThemePackage pack = new ThemePackage();

		WindowTheme window = new WindowTheme();
		window.background = new Color(0, 191, 255);
		window.foreground = new Color(0, 30, 255);
		window.opacity = 0.5f;
		window.width = 300;
		window.height = 100;

		TextTheme text = new TextTheme();
		text.title = new Font("Comic Sans MS", Font.BOLD, 22);
		text.subtitle = new Font("Comic Sans MS", Font.PLAIN, 16);
		text.titleColor = new Color(10, 10, 10);
		text.subtitleColor = new Color(10, 10, 10);

		pack.setTheme(WindowTheme.class, window);
		pack.setTheme(TextTheme.class, text);

		return pack;
	}
}
