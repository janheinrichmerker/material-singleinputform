![Icon](http://i.imgur.com/7KY5gBi.png)

material-singleinputform
=======================
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-material--singleinputform-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1163)
[![JitPack](https://jitpack.io/v/com.heinrichreimersoftware/material-singleinputform.svg)](https://jitpack.io/#com.heinrichreimersoftware/material-singleinputform)
[![Build Status](https://travis-ci.org/janheinrichmerker/material-singleinputform.svg?branch=master)](https://travis-ci.org/janheinrichmerker/material-singleinputform)
[![License](https://img.shields.io/github/license/janheinrichmerker/material-singleinputform.svg)](https://github.com/janheinrichmerker/material-singleinputform/blob/master/LICENSE.txt)

A single EditText instead of a classical form.

This Library is a library implementation of  [flavienlaurent's "Single input form"][1]

_Very inspired by the [Minimal Format Interface][2]._

Implement any form with only one EditText. The idea here is to pack a multiple input form in one field. As a result, the user input is easier and a more fluent process.

Also, [TextSwitcher][3] got completely rediscovered to animate the form: title, error message. It's very basic but simple to use.

Here is a video demonstrating a sample form:
http://youtu.be/A99g6NDvn_w

Demo
----
A demo app is available on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.singleinputform.example">
	<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" />
</a>

Screenshots
-----------

| Checkbox input | E-mail input | Password input |
|:-:|:-:|:-:|
| ![Checkbox input](http://i.imgur.com/lsSIFuV.png) | ![E-mail input](http://i.imgur.com/JFB1tTq.png) | ![Password input](http://i.imgur.com/BiLn77T.png) |
| [_CheckBoxStep.java_][CBS] | [_TextStep.java_][TS] | [_TextStep.java_][TS] |

Dependency
----------
*material-singleinputform* is available on [**jitpack.io**](https://jitpack.io/#com.heinrichreimersoftware/material-singleinputform)

**Gradle dependency:**
````gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
````
````gradle
dependencies {
	compile 'com.heinrichreimersoftware:material-singleinputform:3.0.3'
}
````

How-To-Use
-----

**Step 1:** Your `Activity` must extend [`SingleInputFormActivity`][SIFA]:

````java
public class MainActivity extends SingleInputFormActivity {
	//...
}
````

**Step 2:** Implement abstract methods:

````java
public class MainActivity extends SingleInputFormActivity{
	private static final String DATA_KEY_EXAMPLE = "example";
	
	@Override
	protected List<Step> getSteps(Context context){
		List<Step> steps = new ArrayList<Step>();
		steps.add(new TextStep(
				context,
				DATA_KEY_EXAMPLE,
				InputType.TYPE_CLASS_TEXT,
				R.string.example,
				R.string.example_error,
				R.string.example_details)
		);
		//Add more steps here...
		return steps;
	}
	
	@Override
	protected void onFormFinished(Bundle data){
		//Get the form data
		String text = TextStep.text(data, DATA_KEY_EXAMPLE);
		//...
	}
}
````

**Step 3:** Theme:

````xml
<style name="YourThemeForSingleInputFormActivity" parent="Theme.AppCompat.Light.NoActionBar">
	<!-- Used for: input field background -->
	<item name="colorPrimary">@color/material_bordeaux_500</item>
	
	<!-- Used for: form progress color, status bar color (API 21+) -->
	<item name="colorPrimaryDark">@color/material_bordeaux_700</item>
	
	<!-- Used for: title text color, error text color -->
	<item name="android:textColorPrimary">@color/material_bordeaux_800</item>
	
	<!-- Used for: details text color, step indicator text color -->
	<item name="android:textColorSecondary">@color/material_black_54</item>
	
	<!-- Used for: input text color, input widget color -->
	<item name="android:textColorPrimaryInverse">@color/material_white_100</item>
	
	<!-- Used for: input widget color -->
	<item name="android:textColorSecondaryInverse">@color/material_white_70</item>
</style>
````

Open source libraries
-------

_material-singleinputform_ uses the following open source libraries or files:

* [singleinputform][4] by [@Flavien Laurent][5] (Apache License 2.0)
* [DiscreteSeekBar][6] f by [@Gustavo Claramunt][7] (Apache License 2.0)

License
-------

    Copyright 2016 Heinrich Reimer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    
[1]: https://github.com/flavienlaurent/singleinputform
[2]: https://github.com/codrops/MinimalForm
[3]: http://developer.android.com/reference/android/widget/TextSwitcher.html
[CBS]: https://github.com/janheinrichmerker/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/steps/CheckBoxStep.java
[TS]: https://github.com/janheinrichmerker/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/steps/TextStep.java
[SBS]: https://github.com/janheinrichmerker/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/steps/SeekBarStep.java
[SIFA]: https://github.com/janheinrichmerker/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/SingleInputFormActivity.java
[4]: https://github.com/flavienlaurent/singleinputform
[5]: https://github.com/flavienlaurent
[6]: https://github.com/AnderWeb/discreteSeekBar
[7]: https://github.com/AnderWeb
